/*
 * Script della pagina updateStanza.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';
const CHIAVE_SESSIONE = 'stanzaDaModificare';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente. Resta fuori il recupero dei dati della stanza.
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

const form = document.getElementById('formStanza');
const esito = document.getElementById('esito');
const btnSubmit = document.getElementById('btnSubmit');
const messaggioCodice = document.getElementById('messaggioCodice');

// Codice con cui la stanza e' registrata adesso: viaggia come parametro della
// PUT, cosi' il service sa quale riga aggiornare anche se il codice cambia.
const codiceVecchio = new URLSearchParams(window.location.search).get('codice') || '';

messaggioCodice.textContent = codiceVecchio ? ('Stanza selezionata: ' + codiceVecchio) : 'Nessuna stanza selezionata.';

function riempiForm(stanza) {
  document.getElementById('codice').value = stanza.codice || '';
  document.getElementById('tipoStanza').value = stanza.tipoStanza || 'SINGOLA';
  document.getElementById('capienza').value = stanza.capienza != null ? stanza.capienza : '';
  document.getElementById('piano').value = stanza.piano != null ? stanza.piano : '';
  document.getElementById('prezzoBase').value = stanza.prezzoBase != null ? stanza.prezzoBase : '';
  document.getElementById('termoregolabile').value = stanza.termoregolabile ? 'true' : 'false';
  document.getElementById('note').value = stanza.note || '';
}

/*
 * La dto arriva dalla pagina di elenco tramite sessionStorage. Se la pagina
 * viene ricaricata quel dato non c'e' piu': in quel caso si richiama
 * direttamente /goToUpdateStanza con il codice preso dalla query string.
 */
async function caricaStanza() {
  nascondiEsito(esito);

  const salvata = sessionStorage.getItem(CHIAVE_SESSIONE);

  if (salvata) {
    sessionStorage.removeItem(CHIAVE_SESSIONE);
    riempiForm(JSON.parse(salvata));
    return;
  }

  if (!codiceVecchio) {
    mostraEsito(esito, 'Nessuna stanza selezionata: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/goToUpdateStanza/' + encodeURIComponent(codiceVecchio), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok && corpo) {
      riempiForm(corpo);
    } else {
      mostraEsito(esito, corpo || 'Stanza non trovata.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function gestisciModifica(event) {
  event.preventDefault();
  nascondiEsito(esito);

  if (!codiceVecchio) {
    mostraEsito(esito, 'Nessuna stanza selezionata: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  if (!(await accessoConsentito())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  const corpoRichiesta = {
    codice: document.getElementById('codice').value.trim(),
    tipoStanza: document.getElementById('tipoStanza').value.trim(),
    capienza: Number(document.getElementById('capienza').value),
    piano: Number(document.getElementById('piano').value),
    prezzoBase: Number(document.getElementById('prezzoBase').value),
    termoregolabile: document.getElementById('termoregolabile').value === 'true',
    note: document.getElementById('note').value.trim()
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Salvataggio in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/modifyStanza?codiceStanza=' + encodeURIComponent(codiceVecchio), {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Stanza modificata con successo.', 'success');
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante la modifica della stanza.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Salva modifiche';
  }
};

caricaStanza();
