/*
 * Script della pagina updatePacchetto.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';
const CHIAVE_SESSIONE = 'pacchettoDaModificare';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente. Resta fuori il recupero dei dati del pacchetto.
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

const form = document.getElementById('formPacchetto');
const esito = document.getElementById('esito');
const btnSubmit = document.getElementById('btnSubmit');
const messaggioCodice = document.getElementById('messaggioCodice');

// Tipo con cui il pacchetto e' registrato adesso: viaggia come parametro della
// PUT, cosi' il service sa quale riga aggiornare anche se il tipo cambia.
const tipoVecchio = new URLSearchParams(window.location.search).get('tipoPensione') || '';

messaggioCodice.textContent = tipoVecchio ? ('Pacchetto selezionato: ' + tipoVecchio) : 'Nessun pacchetto selezionato.';

function riempiForm(pacchetto) {
  document.getElementById('tipoPensione').value = pacchetto.tipoPensione || 'PERNOTTAMENTO';
  document.getElementById('colazione').checked = pacchetto.colazione === true;
  document.getElementById('navetta').checked = pacchetto.navetta === true;
  document.getElementById('guida').checked = pacchetto.guida === true;
  document.getElementById('piscina').checked = pacchetto.piscina === true;
  document.getElementById('parcheggio').checked = pacchetto.parcheggio === true;
  document.getElementById('percentuale').value = pacchetto.percentuale || null;
  document.getElementById('descrizione').value = pacchetto.descrizione || '';
}

/*
 * La dto arriva dalla pagina di elenco tramite sessionStorage. Se la pagina
 * viene ricaricata quel dato non c'e' piu': in quel caso si richiama
 * direttamente /goToUpdatePacchetto con il tipo preso dalla query string.
 */
async function caricaPacchetto() {
  nascondiEsito(esito);

  const salvato = sessionStorage.getItem(CHIAVE_SESSIONE);

  if (salvato) {
    sessionStorage.removeItem(CHIAVE_SESSIONE);
    riempiForm(JSON.parse(salvato));
    return;
  }

  if (!tipoVecchio) {
    mostraEsito(esito, 'Nessun pacchetto selezionato: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/goToUpdatePacchetto/' + encodeURIComponent(tipoVecchio), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok && corpo) {
      riempiForm(corpo);
    } else {
      mostraEsito(esito, corpo || 'Pacchetto non trovato.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function gestisciModifica(event) {
  event.preventDefault();
  nascondiEsito(esito);

  if (!tipoVecchio) {
    mostraEsito(esito, 'Nessun pacchetto selezionato: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  if (!(await accessoConsentito())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  const corpoRichiesta = {
    tipoPensione: document.getElementById('tipoPensione').value,
    colazione: document.getElementById('colazione').checked,
    navetta: document.getElementById('navetta').checked,
    guida: document.getElementById('guida').checked,
    piscina: document.getElementById('piscina').checked,
    parcheggio: document.getElementById('parcheggio').checked,
    percentuale: document.getElementById('percentuale').value,
    descrizione: document.getElementById('descrizione').value.trim()
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Salvataggio in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/modifyPacchetto?tipoPacchetto=' + encodeURIComponent(tipoVecchio), {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Pacchetto modificato con successo.', 'success');
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante la modifica del pacchetto.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Salva modifiche';
  }
};

caricaPacchetto();
