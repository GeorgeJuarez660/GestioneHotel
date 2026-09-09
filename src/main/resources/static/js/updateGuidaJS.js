/*
 * Script della pagina updateGuida.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';
const CHIAVE_SESSIONE = 'guidaDaModificare';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente. Resta fuori il recupero dei dati della visita.
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

const form = document.getElementById('formGuida');
const esito = document.getElementById('esito');
const btnSubmit = document.getElementById('btnSubmit');
const messaggioCodice = document.getElementById('messaggioCodice');

// Codice con cui la guida e' registrata adesso: viaggia come parametro della
// PUT, cosi' il service sa quale riga aggiornare anche se il codice cambia.
const codiceVecchio = new URLSearchParams(window.location.search).get('codice') || '';

messaggioCodice.textContent = codiceVecchio ? ('Visita selezionata: ' + codiceVecchio) : 'Nessuna visita selezionata.';

function riempiForm(guida) {
  document.getElementById('codice').value = guida.codice || '';
  document.getElementById('data').value = guida.data || '';
  document.getElementById('ora').value = guida.ora || '';
  document.getElementById('luogo').value = guida.luogo || '';
  document.getElementById('cfOperatoreInterno').value = guida.cfOperatoreInterno || '';
  document.getElementById('cfOperatoreEsterno').value = guida.cfOperatoreEsterno || '';
}

/*
 * La dto arriva dalla pagina di elenco tramite sessionStorage. Se la pagina
 * viene ricaricata quel dato non c'e' piu': in quel caso si richiama
 * direttamente /goToUpdateGuida con il codice preso dalla query string.
 */
async function caricaGuida() {
  nascondiEsito(esito);

  const salvata = sessionStorage.getItem(CHIAVE_SESSIONE);

  if (salvata) {
    sessionStorage.removeItem(CHIAVE_SESSIONE);
    riempiForm(JSON.parse(salvata));
    return;
  }

  if (!codiceVecchio) {
    mostraEsito(esito, 'Nessuna visita selezionata: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/goToUpdateGuida/' + encodeURIComponent(codiceVecchio), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok && corpo) {
      riempiForm(corpo);
    } else {
      mostraEsito(esito, corpo || 'Visita non trovata.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function gestisciModifica(event) {
  event.preventDefault();
  nascondiEsito(esito);

  if (!codiceVecchio) {
    mostraEsito(esito, 'Nessuna visita selezionata: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  if (!(await accessoConsentito())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  const cfOperatoreInterno = document.getElementById('cfOperatoreInterno').value.trim();
  const cfOperatoreEsterno = document.getElementById('cfOperatoreEsterno').value.trim();

  const corpoRichiesta = {
    codice: document.getElementById('codice').value.trim(),
    data: document.getElementById('data').value,
    ora: document.getElementById('ora').value,
    luogo: document.getElementById('luogo').value.trim(),
    cfOperatoreInterno: cfOperatoreInterno || null,
    cfOperatoreEsterno: cfOperatoreEsterno || null
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Salvataggio in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/modifyGuida?codiceGuida=' + encodeURIComponent(codiceVecchio), {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Guida modificata con successo.', 'success');
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante la modifica della guida.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Salva modifiche';
  }
};

caricaGuida();
