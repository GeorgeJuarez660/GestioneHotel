/*
 * Script della pagina updateNavetta.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';
const CHIAVE_SESSIONE = 'navettaDaModificare';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente. Resta fuori il recupero dei dati della corsa.
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

const form = document.getElementById('formNavetta');
const esito = document.getElementById('esito');
const btnSubmit = document.getElementById('btnSubmit');
const messaggioCodice = document.getElementById('messaggioCodice');

// Codice con cui la navetta e' registrata adesso: viaggia come parametro della
// PUT, cosi' il service sa quale riga aggiornare anche se il codice cambia.
const codiceVecchio = new URLSearchParams(window.location.search).get('codice') || '';

messaggioCodice.textContent = codiceVecchio ? ('Corsa selezionata: ' + codiceVecchio) : 'Nessuna corsa selezionata.';

function riempiForm(navetta) {
  document.getElementById('codice').value = navetta.codice || '';
  document.getElementById('dataPartenza').value = navetta.dataPartenza || '';
  document.getElementById('oraPartenza').value = navetta.oraPartenza || '';
  document.getElementById('luogoPartenza').value = navetta.luogoPartenza || '';
  document.getElementById('luogoDestinazione').value = navetta.luogoDestinazione || '';
  document.getElementById('numPostiMax').value = navetta.numPostiMax || '';
  document.getElementById('numPostiDisp').value = navetta.numPostiDisp || '';
  document.getElementById('cfOperatoreInterno').value = navetta.cfOperatoreInterno || '';
}

/*
 * La dto arriva dalla pagina di elenco tramite sessionStorage. Se la pagina
 * viene ricaricata quel dato non c'e' piu': in quel caso si richiama
 * direttamente /goToUpdateNavetta con il codice preso dalla query string.
 */
async function caricaNavetta() {
  nascondiEsito(esito);

  const salvata = sessionStorage.getItem(CHIAVE_SESSIONE);

  if (salvata) {
    sessionStorage.removeItem(CHIAVE_SESSIONE);
    riempiForm(JSON.parse(salvata));
    return;
  }

  if (!codiceVecchio) {
    mostraEsito(esito, 'Nessuna corsa selezionata: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/goToUpdateNavetta/' + encodeURIComponent(codiceVecchio), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok && corpo) {
      riempiForm(corpo);
    } else {
      mostraEsito(esito, corpo || 'Corsa non trovata.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function gestisciModifica(event) {
  event.preventDefault();
  nascondiEsito(esito);

  if (!codiceVecchio) {
    mostraEsito(esito, 'Nessuna corsa selezionata: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  if (!(await accessoConsentito())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  const cfOperatoreInterno = document.getElementById('cfOperatoreInterno').value.trim();

  const corpoRichiesta = {
    codice: document.getElementById('codice').value.trim(),
    dataPartenza: document.getElementById('dataPartenza').value,
    oraPartenza: document.getElementById('oraPartenza').value,
    luogoDestinazione: document.getElementById('luogoDestinazione').value.trim(),
    luogoPartenza: document.getElementById('luogoPartenza').value.trim(),
    numPostiMax: document.getElementById('numPostiMax').value,
    numPostiDisp: document.getElementById('numPostiDisp').value,
    cfOperatoreInterno: cfOperatoreInterno || null
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Salvataggio in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/modifyNavetta?codiceNavetta=' + encodeURIComponent(codiceVecchio), {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Navetta modificata con successo.', 'success');
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante la modifica della navetta.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Salva modifiche';
  }
};

caricaNavetta();
