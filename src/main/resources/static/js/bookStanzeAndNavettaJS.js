/*
 * Script della pagina bookStanzeAndNavetta.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

/*
 * Le letture di stanze, navette, guide, piscine e pacchetti usano gli endpoint
 * gia' esposti dal lato dipendente: sono gli unici presenti e SecurityConfig
 * lascia aperto l'intero ramo /hotel/**.
 */
const ENDPOINT_LETTURE = API_BASE + '/homepage/dipendente';
const ENDPOINT_CLIENTE = API_BASE + '/homepage/cliente';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

// Foto dei servizi, servite da static/img.
const FOTO = {
  stanza: '/img/stanza.jpg',
  navetta: '/img/navetta.jpg',
  guida: '/img/guida.jpg',
  piscina: '/img/piscina.jpg'
};

// Supplemento usato quando il pacchetto non ne indica uno proprio.
const SUPPLEMENTO_PREDEFINITO = 10;

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un cliente. Restano fuori le letture dei servizi e dei
 * pacchetti e gli spostamenti fra le pagine.
 */
async function accessoConsentito() {
  try {
    const response = await fetch(ENDPOINT_CLIENTE + '/check', {
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

const corpoPacchetti = document.getElementById('corpoPacchetti');
const corpoStanze = document.getElementById('corpoStanze');
const corpoNavette = document.getElementById('corpoNavette');
const corpoGuide = document.getElementById('corpoGuide');
const corpoPiscine = document.getElementById('corpoPiscine');

const esitoPacchetti = document.getElementById('esitoPacchetti');
const esitoStanze = document.getElementById('esitoStanze');
const esitoNavette = document.getElementById('esitoNavette');
const esitoGuide = document.getElementById('esitoGuide');
const esitoPiscine = document.getElementById('esitoPiscine');

const notaNavette = document.getElementById('notaNavette');
const notaGuide = document.getElementById('notaGuide');
const notaPiscine = document.getElementById('notaPiscine');

const sezioneNavette = document.getElementById('sezioneNavette');
const sezioneGuide = document.getElementById('sezioneGuide');
const sezionePiscine = document.getElementById('sezionePiscine');

const riepilogoPacchetto = document.getElementById('riepilogoPacchetto');
const riepilogoStanza = document.getElementById('riepilogoStanza');
const riepilogoNavette = document.getElementById('riepilogoNavette');
const riepilogoGuida = document.getElementById('riepilogoGuida');
const riepilogoPiscina = document.getElementById('riepilogoPiscina');
const riepilogoNotti = document.getElementById('riepilogoNotti');
const riepilogoSupplemento = document.getElementById('riepilogoSupplemento');
const riepilogoTotale = document.getElementById('riepilogoTotale');
const etichettaSupplemento = document.getElementById('etichettaSupplemento');
const hintCapienza = document.getElementById('hintCapienza');

// Il pacchetto resta a scelta singola: e' lui a decidere il resto.
let pacchettoScelto = null;

// Stanze e servizi si scelgono con le caselle di spunta: possono essere piu' di uno.
let stanzeScelte = [];
let navetteScelte = [];
let guideScelte = [];
let piscineScelte = [];

/*
 * Aggiunge o toglie un elemento dalla lista dei selezionati, confrontando
 * per codice.
 */
function aggiornaSelezione(elenco, elemento, selezionato) {
  if (selezionato) {
    elenco.push(elemento);
    return elenco;
  }

  return elenco.filter(function (scelto) {
    return scelto.codice !== elemento.codice;
  });
}

function elencoCodici(elenco) {
  if (elenco.length === 0) return 'nessuna';

  return elenco.map(function (elemento) { return elemento.codice; }).join(', ');
}

// Posti totali offerti dalle stanze selezionate.
function capienzaTotale() {
  let posti = 0;

  stanzeScelte.forEach(function (stanza) {
    if (stanza.capienza) posti = posti + Number(stanza.capienza);
  });

  return posti;
}

/* ---------- Costruzione delle tabelle ---------- */

function mostraVuoto(corpo, messaggio) {
  corpo.innerHTML = '';
  const testo = document.createElement('p');
  testo.className = 'scelta-vuoto';
  testo.textContent = messaggio;
  corpo.appendChild(testo);
}

/*
 * Card orizzontale: a sinistra la spunta, la foto (dove prevista) e il nome,
 * a destra tutti gli altri dati. La card e' una <label>, quindi si seleziona
 * cliccandola per intero e non solo sul quadratino.
 *
 * "preselezionata" imposta .checked da codice, che pero' non fa scattare
 * l'evento change: chi chiama aggiorna anche lo stato della pagina.
 */
function creaCard(opzioni) {
  const card = document.createElement('label');
  card.className = 'scelta-card' + (opzioni.foto ? '' : ' scelta-card--senza-foto');

  const scelta = document.createElement('input');
  scelta.type = opzioni.gruppo ? 'radio' : 'checkbox';
  if (opzioni.gruppo) scelta.name = opzioni.gruppo;
  scelta.checked = opzioni.preselezionata === true;

  function segnalaScelta() {
    card.classList.toggle('scelta-card--scelta', scelta.checked);
  }

  scelta.addEventListener('change', function () {
    // Con i radio la card che perde la scelta va ripulita a mano.
    if (opzioni.gruppo) {
      document.getElementsByName(opzioni.gruppo).forEach(function (altro) {
        altro.closest('.scelta-card').classList.toggle('scelta-card--scelta', altro.checked);
      });
    } else {
      segnalaScelta();
    }

    opzioni.alCambiare(scelta.checked);
  });

  card.appendChild(scelta);

  if (opzioni.foto) {
    const foto = document.createElement('img');
    foto.className = 'scelta-card__foto';
    foto.src = opzioni.foto;
    foto.alt = '';
    card.appendChild(foto);
  }

  const nome = document.createElement('div');
  nome.className = 'scelta-card__nome';

  const titolo = document.createElement('span');
  titolo.className = 'scelta-card__titolo';
  titolo.textContent = opzioni.titolo;
  nome.appendChild(titolo);

  if (opzioni.sottotitolo) {
    const sottotitolo = document.createElement('span');
    sottotitolo.className = 'scelta-card__sottotitolo';
    sottotitolo.textContent = opzioni.sottotitolo;
    nome.appendChild(sottotitolo);
  }

  card.appendChild(nome);

  const dati = document.createElement('div');
  dati.className = 'scelta-card__dati';

  opzioni.dati.forEach(function (voce) {
    const dato = document.createElement('div');
    dato.className = 'scelta-dato';

    const etichetta = document.createElement('span');
    etichetta.className = 'scelta-dato__etichetta';
    etichetta.textContent = voce[0];

    const valore = document.createElement('span');
    const grezzo = voce[1];
    valore.textContent = (grezzo === null || grezzo === undefined || grezzo === '') ? '-' : grezzo;

    dato.appendChild(etichetta);
    dato.appendChild(valore);
    dati.appendChild(dato);
  });

  card.appendChild(dati);
  segnalaScelta();

  return card;
}

/* ---------- Servizi consentiti dal pacchetto ---------- */

function impostaSezione(sezione, corpo, nota, consentito, nomeServizio) {
  corpo.querySelectorAll('input').forEach(function (input) {
    input.disabled = !consentito;
    if (!consentito) input.checked = false;

    const card = input.closest('.scelta-card');

    if (card) {
      card.classList.toggle('scelta-card--bloccata', !consentito);
      card.classList.toggle('scelta-card--scelta', input.checked);
    }
  });

  if (!pacchettoScelto) {
    nota.textContent = 'Scegli prima un pacchetto per poter selezionare ' + nomeServizio + '.';
    nota.className = 'esito esito--info';
  } else if (consentito) {
    nota.textContent = 'Servizio compreso nel pacchetto ' + pacchettoScelto.tipoPensione + '.';
    nota.className = 'esito esito--success';
  } else {
    nota.textContent = 'Il pacchetto ' + pacchettoScelto.tipoPensione + ' non comprende questo servizio.';
    nota.className = 'esito esito--error';
  }

  if (consentito) {
    sezione.classList.remove('panel--bloccato');
  } else {
    sezione.classList.add('panel--bloccato');
  }
}

/*
 * Abilita o blocca le tre sezioni dei servizi in base a cosa comprende il
 * pacchetto scelto, azzerando le selezioni che non sono piu' ammesse.
 */
function applicaPermessiPacchetto() {
  const navettaOk = !!(pacchettoScelto && pacchettoScelto.navetta);
  const guidaOk = !!(pacchettoScelto && pacchettoScelto.guida);
  const piscinaOk = !!(pacchettoScelto && pacchettoScelto.piscina);

  if (!navettaOk) navetteScelte = [];
  if (!guidaOk) guideScelte = [];
  if (!piscinaOk) piscineScelte = [];

  impostaSezione(sezioneNavette, corpoNavette, notaNavette, navettaOk, 'le corse navetta');
  impostaSezione(sezioneGuide, corpoGuide, notaGuide, guidaOk, 'le visite guidate');
  impostaSezione(sezionePiscine, corpoPiscine, notaPiscine, piscinaOk, 'la piscina');
}

/* ---------- Calcolo del riepilogo ---------- */

function contaNotti() {
  const inizio = document.getElementById('dataInizio').value;
  const fine = document.getElementById('dataFine').value;

  if (!inizio || !fine) return 0;

  const millisecondi = new Date(fine) - new Date(inizio);
  const notti = Math.round(millisecondi / (1000 * 60 * 60 * 24));

  return notti > 0 ? notti : 0;
}

function percentualePacchetto() {
  if (!pacchettoScelto) return 0;

  return pacchettoScelto.percentuale != null ? Number(pacchettoScelto.percentuale) : SUPPLEMENTO_PREDEFINITO;
}

function calcolaTotale() {
  const notti = contaNotti();
  let totale = 0;

  // Con piu' stanze selezionate il soggiorno costa la somma dei prezzi base.
  stanzeScelte.forEach(function (stanza) {
    if (stanza.prezzoBase) {
      totale = totale + Number(stanza.prezzoBase) * notti;
    }
  });

  // Il pacchetto scelto si addebita come maggiorazione sul soggiorno.
  const percentuale = percentualePacchetto();
  const supplemento = totale * (percentuale / 100);
  totale = totale + supplemento;

  etichettaSupplemento.textContent = percentuale > 0
    ? ('Supplemento pacchetto (' + percentuale + '%)')
    : 'Supplemento pacchetto';

  riepilogoNotti.textContent = notti;
  riepilogoSupplemento.textContent = supplemento.toFixed(2);
  riepilogoTotale.textContent = totale.toFixed(2);

  return Number(totale.toFixed(2));
}

function aggiornaRiepilogo() {
  applicaPermessiPacchetto();

  riepilogoPacchetto.textContent = pacchettoScelto ? pacchettoScelto.tipoPensione : 'nessuno';
  riepilogoStanza.textContent = elencoCodici(stanzeScelte);
  riepilogoNavette.textContent = elencoCodici(navetteScelte);
  riepilogoGuida.textContent = elencoCodici(guideScelte);
  riepilogoPiscina.textContent = elencoCodici(piscineScelte);

  // Il campo persone si adegua ai posti offerti dalle stanze selezionate.
  const campoPersone = document.getElementById('numPersone');
  const posti = capienzaTotale();

  if (posti > 0) {
    campoPersone.max = posti;
    hintCapienza.textContent = 'Posti disponibili con le stanze scelte: ' + posti + '.';
  } else {
    campoPersone.removeAttribute('max');
    hintCapienza.textContent = 'Non puo\' superare la capienza delle stanze scelte.';
  }

  calcolaTotale();
}

/* ---------- Lettura dei pacchetti ---------- */

async function leggiPacchetti() {
  nascondiEsito(esitoPacchetti);
  mostraVuoto(corpoPacchetti, 'Caricamento in corso...');
  pacchettoScelto = null;
  aggiornaRiepilogo();

  try {
    const response = await fetch(ENDPOINT_LETTURE + '/readPacchetti', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraVuoto(corpoPacchetti, 'Nessun dato disponibile.');
      mostraEsito(esitoPacchetti, corpo || 'Errore durante la lettura dei pacchetti.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraVuoto(corpoPacchetti, 'Nessun pacchetto disponibile.');
      return;
    }

    corpoPacchetti.innerHTML = '';
    corpo.forEach(function (pacchetto, indice) {
      const primo = indice === 0;

      // Il primo pacchetto e' anche quello attivo all'apertura della pagina.
      if (primo) pacchettoScelto = pacchetto;

      corpoPacchetti.appendChild(creaCard({
        gruppo: 'pacchetto',
        preselezionata: primo,
        titolo: pacchetto.tipoPensione,
        sottotitolo: pacchetto.descrizione,
        dati: [
          ['Colazione', pacchetto.colazione ? 'Si' : 'No'],
          ['Navetta', pacchetto.navetta ? 'Si' : 'No'],
          ['Guida', pacchetto.guida ? 'Si' : 'No'],
          ['Piscina', pacchetto.piscina ? 'Si' : 'No'],
          ['Parcheggio', pacchetto.parcheggio ? 'Si' : 'No'],
          ['Supplemento', '+' + (pacchetto.percentuale != null ? pacchetto.percentuale : SUPPLEMENTO_PREDEFINITO) + '%']
        ],
        alCambiare: function () {
          pacchettoScelto = pacchetto;
          aggiornaRiepilogo();
        }
      }));
    });

    aggiornaRiepilogo();
  } catch (errore) {
    mostraVuoto(corpoPacchetti, 'Nessun dato disponibile.');
    mostraEsito(esitoPacchetti, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

/* ---------- Lettura delle stanze ---------- */

async function leggiStanze() {
  nascondiEsito(esitoStanze);
  mostraVuoto(corpoStanze, 'Caricamento in corso...');
  stanzeScelte = [];
  aggiornaRiepilogo();

  try {
    const response = await fetch(ENDPOINT_LETTURE + '/readStanze', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraVuoto(corpoStanze, 'Nessun dato disponibile.');
      mostraEsito(esitoStanze, corpo || 'Errore durante la lettura delle stanze.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraVuoto(corpoStanze, 'Nessuna stanza disponibile.');
      return;
    }

    corpoStanze.innerHTML = '';
    corpo.forEach(function (stanza, indice) {
      const prima = indice === 0;

      // La prima stanza parte gia' selezionata, come il primo pacchetto.
      if (prima) stanzeScelte = aggiornaSelezione(stanzeScelte, stanza, true);

      corpoStanze.appendChild(creaCard({
        foto: FOTO.stanza,
        preselezionata: prima,
        titolo: stanza.codice,
        sottotitolo: stanza.tipoStanza,
        dati: [
          ['Capienza', stanza.capienza],
          ['Piano', stanza.piano],
          ['Prezzo base', stanza.prezzoBase],
          ['Termoreg.', stanza.termoregolabile ? 'Si' : 'No']
        ],
        alCambiare: function (selezionata) {
          stanzeScelte = aggiornaSelezione(stanzeScelte, stanza, selezionata);
          aggiornaRiepilogo();
        }
      }));
    });

    // La stanza preselezionata entra nel totale e nella capienza.
    aggiornaRiepilogo();
  } catch (errore) {
    mostraVuoto(corpoStanze, 'Nessun dato disponibile.');
    mostraEsito(esitoStanze, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

/* ---------- Lettura delle navette (scelta multipla) ---------- */

async function leggiNavette() {
  nascondiEsito(esitoNavette);
  mostraVuoto(corpoNavette, 'Caricamento in corso...');
  navetteScelte = [];
  aggiornaRiepilogo();

  try {
    const response = await fetch(ENDPOINT_LETTURE + '/readNavette', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraVuoto(corpoNavette, 'Nessun dato disponibile.');
      mostraEsito(esitoNavette, corpo || 'Errore durante la lettura delle navette.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraVuoto(corpoNavette, 'Nessuna corsa disponibile.');
      return;
    }

    corpoNavette.innerHTML = '';
    corpo.forEach(function (navetta) {
      corpoNavette.appendChild(creaCard({
        foto: FOTO.navetta,
        titolo: navetta.codice,
        sottotitolo: navetta.luogoPartenza + ' \u2192 ' + navetta.luogoDestinazione,
        dati: [
          ['Data', navetta.dataPartenza],
          ['Ora', navetta.oraPartenza],
          ['Partenza', navetta.luogoPartenza],
          ['Destinazione', navetta.luogoDestinazione]
        ],
        alCambiare: function (selezionata) {
          navetteScelte = aggiornaSelezione(navetteScelte, navetta, selezionata);
          aggiornaRiepilogo();
        }
      }));
    });

    aggiornaRiepilogo();
  } catch (errore) {
    mostraVuoto(corpoNavette, 'Nessun dato disponibile.');
    mostraEsito(esitoNavette, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

/* ---------- Lettura delle guide ---------- */

async function leggiGuide() {
  nascondiEsito(esitoGuide);
  mostraVuoto(corpoGuide, 'Caricamento in corso...');
  guideScelte = [];
  aggiornaRiepilogo();

  try {
    const response = await fetch(ENDPOINT_LETTURE + '/readGuide', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraVuoto(corpoGuide, 'Nessun dato disponibile.');
      mostraEsito(esitoGuide, corpo || 'Errore durante la lettura delle guide.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraVuoto(corpoGuide, 'Nessuna visita disponibile.');
      return;
    }

    corpoGuide.innerHTML = '';
    corpo.forEach(function (guida) {
      corpoGuide.appendChild(creaCard({
        foto: FOTO.guida,
        titolo: guida.codice,
        sottotitolo: guida.luogo,
        dati: [
          ['Data', guida.data],
          ['Ora', guida.ora],
          ['Luogo', guida.luogo]
        ],
        alCambiare: function (selezionata) {
          guideScelte = aggiornaSelezione(guideScelte, guida, selezionata);
          aggiornaRiepilogo();
        }
      }));
    });

    aggiornaRiepilogo();
  } catch (errore) {
    mostraVuoto(corpoGuide, 'Nessun dato disponibile.');
    mostraEsito(esitoGuide, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

/* ---------- Lettura delle piscine ---------- */

async function leggiPiscine() {
  nascondiEsito(esitoPiscine);
  mostraVuoto(corpoPiscine, 'Caricamento in corso...');
  piscineScelte = [];
  aggiornaRiepilogo();

  try {
    const response = await fetch(ENDPOINT_LETTURE + '/readPiscine', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraVuoto(corpoPiscine, 'Nessun dato disponibile.');
      mostraEsito(esitoPiscine, corpo || 'Errore durante la lettura delle piscine.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraVuoto(corpoPiscine, 'Nessuna piscina disponibile.');
      return;
    }

    corpoPiscine.innerHTML = '';
    corpo.forEach(function (piscina) {
      corpoPiscine.appendChild(creaCard({
        foto: FOTO.piscina,
        titolo: piscina.codice,
        sottotitolo: (piscina.larghezza != null && piscina.lunghezza != null)
          ? (piscina.larghezza + ' x ' + piscina.lunghezza + ' m')
          : '',
        dati: [
          ['Larghezza', piscina.larghezza],
          ['Lunghezza', piscina.lunghezza]
        ],
        alCambiare: function (selezionata) {
          piscineScelte = aggiornaSelezione(piscineScelte, piscina, selezionata);
          aggiornaRiepilogo();
        }
      }));
    });

    aggiornaRiepilogo();
  } catch (errore) {
    mostraVuoto(corpoPiscine, 'Nessun dato disponibile.');
    mostraEsito(esitoPiscine, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

/* ---------- Invio della prenotazione ---------- */

/*
 * PrenotazioneResponse porta un solo codice per stanza e per servizio: le
 * selezioni oltre la prima finiscono qui, cosi' la scelta non va persa.
 */
function componiNote() {
  const parti = [];

  if (pacchettoScelto) parti.push('Pacchetto: ' + pacchettoScelto.tipoPensione);
  if (stanzeScelte.length > 0) parti.push('Stanze: ' + elencoCodici(stanzeScelte));
  if (navetteScelte.length > 0) parti.push('Navette: ' + elencoCodici(navetteScelte));
  if (guideScelte.length > 0) parti.push('Guide: ' + elencoCodici(guideScelte));
  if (piscineScelte.length > 0) parti.push('Piscine: ' + elencoCodici(piscineScelte));

  const note = document.getElementById('note').value.trim();
  if (note) parti.push('Note: ' + note);

  return parti.join(' | ').substring(0, 1000);
}

/*
 * Elenca le voci che l'endpoint non riesce a collegare, per dirlo a video
 * invece di perderle in silenzio.
 */
function selezioniNonInviate() {
  const scartate = [];

  if (stanzeScelte.length > 1) scartate.push('stanze');
  if (navetteScelte.length > 1) scartate.push('navette');
  if (guideScelte.length > 1) scartate.push('guide');
  if (piscineScelte.length > 1) scartate.push('piscine');

  return scartate;
}

function primoCodice(elenco) {
  return elenco.length > 0 ? elenco[0].codice : null;
}

async function gestisciPrenotazione(event) {
  event.preventDefault();
  nascondiEsito(esito);

  if (!(await accessoConsentito())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  if (!pacchettoScelto) {
    mostraEsito(esito, 'Seleziona un pacchetto prima di confermare.', 'error');
    return;
  }

  if (stanzeScelte.length === 0) {
    mostraEsito(esito, 'Seleziona almeno una stanza prima di confermare.', 'error');
    return;
  }

  if (contaNotti() <= 0) {
    mostraEsito(esito, 'La data di fine deve essere successiva alla data di inizio.', 'error');
    return;
  }

  const numPersone = Number(document.getElementById('numPersone').value);

  if (!numPersone || numPersone < 1) {
    mostraEsito(esito, 'Indica il numero di persone.', 'error');
    return;
  }

  const posti = capienzaTotale();

  if (posti > 0 && numPersone > posti) {
    mostraEsito(esito, 'Le stanze scelte offrono ' + posti + ' posti: seleziona altre stanze o riduci il numero di persone.', 'error');
    return;
  }

  const oggi = new Date();

  const corpoRichiesta = {
    // Il codice non e' un campo del form: viene generato qui, ma resta
    // obbligatorio lato DTO.
    codice: 'PR-' + oggi.getTime(),
    dataPrenotazione: oggi.toISOString().substring(0, 10),
    prezzoTotale: calcolaTotale(),
    prezzoEffettivo: calcolaTotale(),
    dataInizio: document.getElementById('dataInizio').value,
    dataFine: document.getElementById('dataFine').value,
    numPersone: numPersone,
    note: componiNote(),
    codiceStanza: primoCodice(stanzeScelte),
    codiceNavetta: primoCodice(navetteScelte),
    codiceGuida: primoCodice(guideScelte),
    codicePiscina: primoCodice(piscineScelte),
    tipoPacchetto: pacchettoScelto.tipoPensione
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Prenotazione in corso...';

  try {
    const response = await fetch(ENDPOINT_CLIENTE + '/addPrenotazione', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + (recuperaToken() || '')
      },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      let messaggio = corpo || 'Prenotazione registrata con successo.';

      // L'endpoint accetta un solo codice per tipo: gli altri restano nelle note.
      const scartate = selezioniNonInviate();

      if (scartate.length > 0) {
        messaggio = messaggio + ' Attenzione: per ' + scartate.join(', ') +
          ' e\' stata collegata solo la prima selezione; le altre sono riportate nelle note.';
      }

      mostraEsito(esito, messaggio, 'success');
      form.reset();
      pacchettoScelto = null;
      stanzeScelte = [];
      navetteScelte = [];
      guideScelte = [];
      piscineScelte = [];
      leggiPacchetti();
      leggiStanze();
      leggiNavette();
      leggiGuide();
      leggiPiscine();
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante la prenotazione.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Conferma prenotazione';
  }
};

document.getElementById('btnAggiornaPacchetti').addEventListener('click', leggiPacchetti);
document.getElementById('btnAggiornaStanze').addEventListener('click', leggiStanze);
document.getElementById('btnAggiornaNavette').addEventListener('click', leggiNavette);
document.getElementById('btnAggiornaGuide').addEventListener('click', leggiGuide);
document.getElementById('btnAggiornaPiscine').addEventListener('click', leggiPiscine);
document.getElementById('dataInizio').addEventListener('change', calcolaTotale);
document.getElementById('dataFine').addEventListener('change', calcolaTotale);

leggiPacchetti();
leggiStanze();
leggiNavette();
leggiGuide();
leggiPiscine();
