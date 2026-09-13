/*
 * Script della pagina updatePrenotazione.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';
const CHIAVE_SESSIONE = 'prenotazioneDaModificare';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente. Qui vale sia per la lettura della prenotazione
 * sia per il salvataggio delle modifiche.
 */
async function accessoConsentito() {
  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/check', {
      method: 'GET',
      headers: { 'Authorization': 'Bearer ' + (recuperaToken() || '') }
    });

    if (!response.ok) return false;

    const corpo = await leggiCorpoRisposta(response);

    return corpo !== null && typeof corpo === 'object' && corpo.id != null;
  } catch (errore) {
    return false;
  }
}

const form = document.getElementById('formPrenotazione');
const esito = document.getElementById('esito');
const btnSubmit = document.getElementById('btnSubmit');
const messaggioCodice = document.getElementById('messaggioCodice');

// Codice con cui la prenotazione e' registrata adesso: viaggia come parametro
// della PUT, cosi' il service sa quale riga aggiornare anche se il codice cambia.
const codiceVecchio = new URLSearchParams(window.location.search).get('codice') || '';

messaggioCodice.textContent = codiceVecchio ? ('Prenotazione selezionata: ' + codiceVecchio) : 'Nessuna prenotazione selezionata.';

/*
 * PrenotazioneReqCheck e' la dto modificabile, nonche' il corpo della PUT:
 * l'endpoint la restituisce cosi' com'e', senza involucri.
 */
function riempiForm(reqCheck) {
  if (!reqCheck) return;

  document.getElementById('codicePrenotazione').value = reqCheck.codicePrenotazione || '';
  document.getElementById('cfCliente').value = reqCheck.cfCliente || '';
  document.getElementById('dataCheckIn').value = reqCheck.dataCheckIn || '';
  document.getElementById('dataCheckOut').value = reqCheck.dataCheckOut || '';
  document.getElementById('prezzoTotale').value = reqCheck.prezzoTotale != null ? reqCheck.prezzoTotale : '';
  document.getElementById('prezzoEffettivo').value = reqCheck.prezzoEffettivo != null ? reqCheck.prezzoEffettivo : '';
  document.getElementById('statoPrenotazione').value = reqCheck.statoPrenotazione || '';
  document.getElementById('statoPagamento').value = reqCheck.statoPagamento || '';
  document.getElementById('numPersone').value = reqCheck.numPersone != null ? reqCheck.numPersone : '';
  document.getElementById('note').value = reqCheck.note || '';

  // Servizi collegati alla prenotazione: navetta, guida e piscina.
  document.getElementById('codiceNavetta').value = reqCheck.codiceNavetta || '';
  document.getElementById('codiceGuida').value = reqCheck.codiceGuida || '';
  document.getElementById('codicePiscina').value = reqCheck.codicePiscina || '';
}

/*
 * La PrenotazioneReqCheck arriva dalla pagina di elenco tramite sessionStorage.
 * Se la pagina viene ricaricata quel dato non c'e' piu': in quel caso si
 * richiama direttamente /goToUpdatePrenotazione con il codice della query string.
 */
async function caricaPrenotazione() {
  nascondiEsito(esito);

  const salvata = sessionStorage.getItem(CHIAVE_SESSIONE);

  if (salvata) {
    sessionStorage.removeItem(CHIAVE_SESSIONE);
    riempiForm(JSON.parse(salvata));
    return;
  }

  if (!codiceVecchio) {
    mostraEsito(esito, 'Nessuna prenotazione selezionata: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  if (!(await accessoConsentito())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/goToUpdatePrenotazione/' + encodeURIComponent(codiceVecchio), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok && corpo) {
      riempiForm(corpo);
    } else {
      mostraEsito(esito, corpo || 'Prenotazione non trovata.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function gestisciModifica(event) {
  event.preventDefault();
  nascondiEsito(esito);

  if (!codiceVecchio) {
    mostraEsito(esito, 'Nessuna prenotazione selezionata: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  if (!(await accessoConsentito())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  const prezzoEffettivo = document.getElementById('prezzoEffettivo').value.trim();
  const statoPagamento = document.getElementById('statoPagamento').value.trim();
  const numPersone = document.getElementById('numPersone').value.trim();
  const codiceNavetta = document.getElementById('codiceNavetta').value.trim();
  const codiceGuida = document.getElementById('codiceGuida').value.trim();
  const codicePiscina = document.getElementById('codicePiscina').value.trim();

  const corpoRichiesta = {
    codicePrenotazione: document.getElementById('codicePrenotazione').value.trim(),
    cfCliente: document.getElementById('cfCliente').value.trim(),
    dataCheckIn: document.getElementById('dataCheckIn').value,
    dataCheckOut: document.getElementById('dataCheckOut').value,
    prezzoTotale: Number(document.getElementById('prezzoTotale').value),
    prezzoEffettivo: prezzoEffettivo !== '' ? Number(prezzoEffettivo) : null,
    statoPrenotazione: document.getElementById('statoPrenotazione').value.trim(),
    statoPagamento: statoPagamento || null,
    numPersone: numPersone !== '' ? Number(numPersone) : null,
    codiceNavetta: codiceNavetta || null,
    codiceGuida: codiceGuida || null,
    codicePiscina: codicePiscina || null,
    note: document.getElementById('note').value.trim()
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Salvataggio in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/modifyPrenotazione?codicePrenotazione=' + encodeURIComponent(codiceVecchio), {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Prenotazione modificata con successo.', 'success');
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante la modifica della prenotazione.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Salva modifiche';
  }
};

/* ---------- Conto della camera: consumazioni e taxi ---------- */

const esitoBevande = document.getElementById('esitoBevande');
const esitoTaxi = document.getElementById('esitoTaxi');
const elencoBevande = document.getElementById('elencoBevande');
const elencoTaxi = document.getElementById('elencoTaxi');
const riepilogoBevande = document.getElementById('riepilogoBevande');
const totaleBevande = document.getElementById('totaleBevande');
const totaleTaxi = document.getElementById('totaleTaxi');
const btnConfermaConsumazione = document.getElementById('btnConfermaConsumazione');
const btnConfermaTaxi = document.getElementById('btnConfermaTaxi');

// Intestatario della prenotazione: i due endpoint di conferma lo vogliono
// come codice fiscale, l'elenco delle corse si filtra su nome e cognome.
let possessore = null;

function formattaEuro(valore) {
  return Number(valore || 0).toFixed(2) + ' €';
}

function mostraElencoVuoto(contenitore, messaggio) {
  contenitore.innerHTML = '';
  const vuoto = document.createElement('p');
  vuoto.className = 'riepilogo-vuoto';
  vuoto.textContent = messaggio;
  contenitore.appendChild(vuoto);
}

function aggiungiRigaRiepilogo(contenitore, voce, importo) {
  const riga = document.createElement('div');
  riga.className = 'riepilogo-riga';

  const testo = document.createElement('span');
  testo.className = 'riepilogo-riga__voce';
  testo.textContent = voce;

  const valore = document.createElement('span');
  valore.textContent = formattaEuro(importo);

  riga.appendChild(testo);
  riga.appendChild(valore);
  contenitore.appendChild(riga);
}

/*
 * Il codice fiscale arriva da /readPrenotazioni; se la lettura non e' ancora
 * andata a buon fine si ripiega sul campo del form, che caricaPrenotazione()
 * ha gia' compilato.
 */
function codiceFiscalePossessore() {
  if (possessore && possessore.cf) return possessore.cf;

  return document.getElementById('cfCliente').value.trim();
}

/* ---------- Sezione consumazione bevande ---------- */

/*
 * Preventivo a schermo: il prezzo effettivo si calcola come nel service
 * (prezzo base per quantita' ordinata). Le righe di consuma le scrive
 * /confirmConsumazione, non questa funzione.
 */
function aggiornaRiepilogoBevande() {
  let sommaTotale = 0;

  riepilogoBevande.innerHTML = '';

  elencoBevande.querySelectorAll('input[type="checkbox"]').forEach(function (spunta) {
    const campoQuantita = document.getElementById('quantita-' + spunta.dataset.indice);
    campoQuantita.disabled = !spunta.checked;

    if (!spunta.checked) return;

    const quantita = Number(campoQuantita.value) || 0;
    const prezzoEffettivo = Number(spunta.dataset.prezzoBase) * quantita;

    sommaTotale = sommaTotale + prezzoEffettivo;
    aggiungiRigaRiepilogo(riepilogoBevande, spunta.dataset.nome + ' x' + quantita, prezzoEffettivo);
  });

  if (riepilogoBevande.children.length === 0) {
    mostraElencoVuoto(riepilogoBevande, 'Nessuna bevanda selezionata.');
  }

  totaleBevande.textContent = formattaEuro(sommaTotale);

  return sommaTotale;
}

// Corpo della PUT /confirmConsumazione: una ConsumaRequest per bevanda spuntata.
function consumazioniSelezionate() {
  const consumazioni = [];

  elencoBevande.querySelectorAll('input[type="checkbox"]').forEach(function (spunta) {
    if (!spunta.checked) return;

    const quantita = Number(document.getElementById('quantita-' + spunta.dataset.indice).value) || 0;

    if (quantita <= 0) return;

    consumazioni.push({
      nomeBevanda: spunta.dataset.nome,
      quantitaOrdinata: quantita
    });
  });

  return consumazioni;
}

function creaRigaBevanda(bevanda, indice) {
  const riga = document.createElement('div');
  riga.className = 'check-row';

  const spunta = document.createElement('input');
  spunta.type = 'checkbox';
  spunta.id = 'bevanda-' + indice;
  spunta.dataset.indice = indice;
  spunta.dataset.nome = bevanda.nome;
  spunta.dataset.prezzoBase = bevanda.prezzoBase;
  spunta.addEventListener('change', aggiornaRiepilogoBevande);

  const etichetta = document.createElement('label');
  etichetta.htmlFor = spunta.id;
  etichetta.textContent = bevanda.nome + ' - ' + formattaEuro(bevanda.prezzoBase);

  const campoQuantita = document.createElement('input');
  campoQuantita.type = 'number';
  campoQuantita.id = 'quantita-' + indice;
  campoQuantita.min = '1';
  campoQuantita.value = '1';
  campoQuantita.disabled = true;
  campoQuantita.addEventListener('input', aggiornaRiepilogoBevande);

  riga.appendChild(spunta);
  riga.appendChild(etichetta);
  riga.appendChild(campoQuantita);

  return riga;
}

async function leggiBevande() {
  mostraElencoVuoto(elencoBevande, 'Caricamento in corso...');

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/readBevande', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok || !Array.isArray(corpo) || corpo.length === 0) {
      mostraElencoVuoto(elencoBevande, 'Nessuna bevanda a listino.');
      aggiornaRiepilogoBevande();
      return;
    }

    elencoBevande.innerHTML = '';
    corpo.forEach(function (bevanda, indice) {
      elencoBevande.appendChild(creaRigaBevanda(bevanda, indice));
    });

    aggiornaRiepilogoBevande();
  } catch (errore) {
    mostraElencoVuoto(elencoBevande, 'Nessun dato disponibile.');
    mostraEsito(esitoBevande, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function confermaConsumazione() {
  nascondiEsito(esitoBevande);

  const consumazioni = consumazioniSelezionate();

  if (consumazioni.length === 0) {
    mostraEsito(esitoBevande, 'Nessuna bevanda selezionata.', 'info');
    return;
  }

  const cf = codiceFiscalePossessore();

  if (!cf) {
    mostraEsito(esitoBevande, 'Intestatario della prenotazione non individuato.', 'error');
    return;
  }

  if (!(await accessoConsentito())) {
    mostraEsito(esitoBevande, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  btnConfermaConsumazione.disabled = true;
  btnConfermaConsumazione.textContent = 'Addebito in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/confirmConsumazione?cfPossessore=' + encodeURIComponent(cf), {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(consumazioni)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esitoBevande, corpo || 'Consumazioni addebitate con successo.', 'success');
      // Le giacenze sono cambiate: si rilegge il listino.
      leggiBevande();
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esitoBevande, corpo || 'Errore durante l\'addebito delle consumazioni.', 'error');
    }
  } catch (errore) {
    mostraEsito(esitoBevande, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnConfermaConsumazione.disabled = false;
    btnConfermaConsumazione.textContent = 'Conferma consumazioni';
  }
}

/* ---------- Sezione corse in taxi ---------- */

/*
 * Qui non si sceglie nulla: /confirmTaxi addebita tutte le corse ancora in
 * sospeso dell'intestatario, quindi la riga mostra solo lo stato e il totale
 * somma le corse non ancora addebitate.
 */
function creaRigaTaxi(taxi) {
  const riga = document.createElement('div');
  riga.className = 'check-row';

  const tratta = (taxi.luogoPartenza || '?') + ' -> ' + (taxi.luogoDestinazione || '?');

  const etichetta = document.createElement('span');
  etichetta.style.flex = '1';
  etichetta.style.fontSize = '0.85rem';
  etichetta.textContent = tratta + ' (' + (taxi.data || '') + ') - ' + formattaEuro(taxi.prezzo);

  const stato = document.createElement('span');
  stato.className = 'stato-addebito ' + (taxi.addebitato ? 'stato-addebito--fatto' : 'stato-addebito--attesa');
  stato.textContent = taxi.addebitato ? 'Addebitato' : 'In sospeso';

  riga.appendChild(etichetta);
  riga.appendChild(stato);

  return riga;
}

async function leggiPossessore() {
  possessore = null;

  if (!codiceVecchio) return;

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/readPrenotazioni', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok || !Array.isArray(corpo)) return;

    const prenotazione = corpo.find(function (voce) {
      return voce.codice === codiceVecchio;
    });

    if (prenotazione) {
      possessore = {
        nome: prenotazione.nomeCliente || '',
        cognome: prenotazione.cognomeCliente || '',
        cf: prenotazione.cfCliente || ''
      };
    }
  } catch (errore) {
    possessore = null;
  }
}

async function leggiTaxi() {
  mostraElencoVuoto(elencoTaxi, 'Caricamento in corso...');
  totaleTaxi.textContent = formattaEuro(0);

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/readTaxi', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok || !Array.isArray(corpo)) {
      mostraElencoVuoto(elencoTaxi, 'Nessuna corsa disponibile.');
      return;
    }

    if (!possessore) {
      mostraElencoVuoto(elencoTaxi, 'Intestatario della prenotazione non individuato.');
      return;
    }

    const corse = corpo.filter(function (taxi) {
      return taxi.nomePossessore === possessore.nome && taxi.cognomePossessore === possessore.cognome;
    });

    if (corse.length === 0) {
      mostraElencoVuoto(elencoTaxi, 'Nessuna corsa a nome di questo cliente.');
      return;
    }

    let sospese = 0;

    elencoTaxi.innerHTML = '';
    corse.forEach(function (taxi) {
      elencoTaxi.appendChild(creaRigaTaxi(taxi));

      if (!taxi.addebitato) {
        sospese = sospese + Number(taxi.prezzo || 0);
      }
    });

    totaleTaxi.textContent = formattaEuro(sospese);
  } catch (errore) {
    mostraElencoVuoto(elencoTaxi, 'Nessun dato disponibile.');
    mostraEsito(esitoTaxi, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function confermaTaxi() {
  nascondiEsito(esitoTaxi);

  const cf = codiceFiscalePossessore();

  if (!cf) {
    mostraEsito(esitoTaxi, 'Intestatario della prenotazione non individuato.', 'error');
    return;
  }

  if (!(await accessoConsentito())) {
    mostraEsito(esitoTaxi, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  btnConfermaTaxi.disabled = true;
  btnConfermaTaxi.textContent = 'Addebito in corso...';

  try {
    /*
     * L'endpoint dichiara un corpo List<ConsumaRequest> ma il service usa
     * solo il codice fiscale: si manda una lista vuota, che soddisfa
     * @RequestBody senza aggiungere nulla all'addebito.
     */
    const response = await fetch(ENDPOINT_DIPENDENTE + '/confirmTaxi?cfPossessore=' + encodeURIComponent(cf), {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify([])
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esitoTaxi, corpo || 'Corse addebitate con successo.', 'success');
      leggiTaxi();
    } else {
      mostraEsito(esitoTaxi, corpo || 'Errore durante l\'addebito delle corse.', 'error');
    }
  } catch (errore) {
    mostraEsito(esitoTaxi, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnConfermaTaxi.disabled = false;
    btnConfermaTaxi.textContent = 'Addebita corse';
  }
}

async function caricaTaxi() {
  nascondiEsito(esitoTaxi);

  await leggiPossessore();
  await leggiTaxi();
}

btnConfermaConsumazione.addEventListener('click', confermaConsumazione);
btnConfermaTaxi.addEventListener('click', confermaTaxi);

document.getElementById('btnAggiornaBevande').addEventListener('click', leggiBevande);
document.getElementById('btnAggiornaTaxi').addEventListener('click', caricaTaxi);

caricaPrenotazione();
leggiBevande();
caricaTaxi();
