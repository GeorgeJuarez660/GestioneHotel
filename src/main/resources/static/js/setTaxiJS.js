/*
 * Script della pagina setTaxi.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

/*
 * La stessa pagina serve due rotte: /hotel/dipendente/taxi e
 * /hotel/cliente/taxi. Il ruolo si ricava dal percorso e decide endpoint,
 * link di ritorno e provenienza dell'intestatario.
 */
const RUOLO_CLIENTE = window.location.pathname.indexOf('/cliente/') !== -1;

const ENDPOINT = API_BASE + (RUOLO_CLIENTE ? '/homepage/cliente' : '/homepage/dipendente');
const PERCORSO_ELENCO = RUOLO_CLIENTE ? '/readTaxiByCliente' : '/readTaxi';
const HOMEPAGE = RUOLO_CLIENTE ? '/hotel/homepage/cliente/' : '/hotel/homepage/dipendente/';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

const form = document.getElementById('formTaxi');
const esito = document.getElementById('esito');
const esitoElenco = document.getElementById('esitoElenco');
const btnSubmit = document.getElementById('btnSubmit');
const corpoTabella = document.getElementById('corpoTabella');
const campoPrenotazione = document.getElementById('campoPrenotazione');
const selectPrenotazione = document.getElementById('codicePrenotazione');
const nomePossessore = document.getElementById('nomePossessore');
const cognomePossessore = document.getElementById('cognomePossessore');
const cfPossessore = document.getElementById('cfPossessore');

// Prenotazioni gia' lette, per risalire all'intestatario senza richiamare il server.
let prenotazioni = [];

function formattaEuro(valore) {
  return Number(valore || 0).toFixed(2) + ' €';
}

function intestazioneHeader() {
  document.getElementById('linkIndietro').href = HOMEPAGE;
  document.getElementById('areaCorrente').textContent = RUOLO_CLIENTE ? 'Area Cliente' : 'Area Dipendente';

  if (RUOLO_CLIENTE) {
    // Il cliente vede solo le proprie corse e non puo' cancellarle.
    document.getElementById('titoloElenco').textContent = 'Le mie corse';
    document.getElementById('colonnaAzioni').hidden = true;
    campoPrenotazione.hidden = true;
  }
}

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire l'utente collegato. Sul lato cliente restituisce anche nome e
 * cognome, che servono come intestatario della corsa.
 */
async function utenteCollegato() {
  try {
    const response = await fetch(ENDPOINT + '/check', {
      method: 'GET',
      headers: { 'Authorization': 'Bearer ' + (recuperaToken() || '') }
    });

    if (!response.ok) return null;

    const corpo = await leggiCorpoRisposta(response);

    if (corpo === null || typeof corpo !== 'object' || corpo.id == null) return null;

    return corpo;
  } catch (errore) {
    return null;
  }
}

/* ---------- Intestatario della corsa ---------- */

/*
 * Il possessore non e' un campo libero: lato cliente e' l'utente collegato,
 * lato dipendente e' l'intestatario della prenotazione scelta. In entrambi i
 * casi i due campi restano in sola lettura.
 */
function aggiornaPossessore() {
  const codice = selectPrenotazione.value;
  const prenotazione = prenotazioni.find(function (voce) {
    return voce.codice === codice;
  });

  cfPossessore.value = prenotazione ? (prenotazione.cfCliente || '') : '';
  nomePossessore.value = prenotazione ? (prenotazione.nomeCliente || '') : '';
  cognomePossessore.value = prenotazione ? (prenotazione.cognomeCliente || '') : '';
}

async function caricaPossessoreCliente() {
  const cliente = await utenteCollegato();

  if (!cliente) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  cfPossessore.value = cliente.codiceFiscale || '';
  nomePossessore.value = cliente.nome || '';
  cognomePossessore.value = cliente.cognome || '';
}

async function leggiPrenotazioni() {
  try {
    const response = await fetch(ENDPOINT + '/readPrenotazioni', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok || !Array.isArray(corpo)) {
      selectPrenotazione.innerHTML = '<option value="">Nessuna prenotazione disponibile</option>';
      return;
    }

    prenotazioni = corpo;
    selectPrenotazione.innerHTML = '';

    const vuota = document.createElement('option');
    vuota.value = '';
    vuota.textContent = 'Seleziona una prenotazione';
    selectPrenotazione.appendChild(vuota);

    corpo.forEach(function (prenotazione) {
      const opzione = document.createElement('option');
      opzione.value = prenotazione.codice;
      opzione.textContent = prenotazione.codice + ' - ' +
        ((prenotazione.nomeCliente || '') + ' ' + (prenotazione.cognomeCliente || '')).trim();
      selectPrenotazione.appendChild(opzione);
    });

    aggiornaPossessore();
  } catch (errore) {
    selectPrenotazione.innerHTML = '<option value="">Nessuna prenotazione disponibile</option>';
  }
}

/* ---------- Elenco delle corse ---------- */

function mostraRigaVuota(messaggio) {
  corpoTabella.innerHTML = '';
  const riga = document.createElement('tr');
  const cella = document.createElement('td');
  cella.className = 'data-table__empty';
  cella.colSpan = RUOLO_CLIENTE ? 7 : 8;
  cella.textContent = messaggio;
  riga.appendChild(cella);
  corpoTabella.appendChild(riga);
}

function creaRiga(taxi) {
  const riga = document.createElement('tr');
  const possessore = ((taxi.nomePossessore || '') + ' ' + (taxi.cognomePossessore || '')).trim();
  const valori = [
    taxi.data,
    taxi.ora,
    taxi.luogoPartenza,
    taxi.luogoDestinazione,
    formattaEuro(taxi.prezzo),
    taxi.numPersone,
    possessore || '-'
  ];

  valori.forEach(function (valore) {
    const cella = document.createElement('td');
    cella.textContent = valore;
    riga.appendChild(cella);
  });

  // La rimozione esiste solo fra gli endpoint del dipendente.
  if (!RUOLO_CLIENTE) {
    const cellaAzioni = document.createElement('td');

    const btnElimina = document.createElement('button');
    btnElimina.type = 'button';
    btnElimina.className = 'btn btn-secondary btn-small';
    btnElimina.style.color = 'var(--color-rust)';
    btnElimina.textContent = 'Elimina';
    btnElimina.addEventListener('click', function () {
      eliminaTaxi(taxi.id);
    });
    cellaAzioni.appendChild(btnElimina);
    riga.appendChild(cellaAzioni);
  }

  return riga;
}

async function eliminaTaxi(id) {
  nascondiEsito(esitoElenco);

  // Se l'utente annulla il pop-up non si chiama l'endpoint.
  if (!confirm('Sei sicuro di voler eliminare questa corsa in taxi?')) return;

  if (!(await utenteCollegato())) {
    mostraEsito(esitoElenco, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT + '/removeTaxi?id=' + encodeURIComponent(id), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      // Prima si ricarica l'elenco, poi si mostra l'esito: leggiTaxi()
      // comincia nascondendo il messaggio.
      await leggiTaxi();
      mostraEsito(esitoElenco, corpo || 'Corsa eliminata con successo.', 'success');
    } else {
      mostraEsito(esitoElenco, corpo || 'Errore durante la rimozione della corsa.', 'error');
    }
  } catch (errore) {
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function leggiTaxi() {
  nascondiEsito(esitoElenco);
  mostraRigaVuota('Caricamento in corso...');

  try {
    const response = await fetch(ENDPOINT + PERCORSO_ELENCO, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': 'Bearer ' + (recuperaToken() || '')
      }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraRigaVuota('Nessun dato disponibile.');
      mostraEsito(esitoElenco, corpo || 'Errore durante la lettura delle corse.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraRigaVuota(RUOLO_CLIENTE ? 'Non hai ancora prenotato corse.' : 'Nessuna corsa registrata.');
      return;
    }

    corpoTabella.innerHTML = '';
    corpo.forEach(function (taxi) {
      corpoTabella.appendChild(creaRiga(taxi));
    });
  } catch (errore) {
    mostraRigaVuota('Nessun dato disponibile.');
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function gestisciInserimento(event) {
  event.preventDefault();
  nascondiEsito(esito);

  if (!(await utenteCollegato())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  /*
   * TaxiRequest lega la corsa al soggiorno tramite nome e cognome
   * dell'intestatario: e' quello che il service usa per risalire a Gestisce.
   */
  const corpoRichiesta = {
    data: document.getElementById('data').value,
    ora: document.getElementById('ora').value,
    luogoPartenza: document.getElementById('luogoPartenza').value.trim(),
    luogoDestinazione: document.getElementById('luogoDestinazione').value.trim(),
    numPersone: document.getElementById('numPersone').value,
    cfPossessore: cfPossessore.value.trim()
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Inserimento in corso...';

  try {
    const response = await fetch(ENDPOINT + '/addTaxi', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + (recuperaToken() || '')
      },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Taxi aggiunto con successo.', 'success');
      form.reset();
      ripristinaPossessore();
      leggiTaxi();
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante l\'inserimento del taxi.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Aggiungi taxi';
  }
};

// form.reset() svuota anche i campi in sola lettura: vanno ricompilati.
function ripristinaPossessore() {
  if (RUOLO_CLIENTE) {
    caricaPossessoreCliente();
  } else {
    aggiornaPossessore();
  }
}

selectPrenotazione.addEventListener('change', aggiornaPossessore);
document.getElementById('btnAggiorna').addEventListener('click', leggiTaxi);

intestazioneHeader();

if (RUOLO_CLIENTE) {
  caricaPossessoreCliente();
} else {
  leggiPrenotazioni();
}

leggiTaxi();
