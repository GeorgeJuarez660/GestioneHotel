/*
 * Script della pagina updateBevanda.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';
const CHIAVE_SESSIONE = 'bevandaDaModificare';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente. Resta fuori il recupero dei dati della bevanda.
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

const form = document.getElementById('formBevanda');
const esito = document.getElementById('esito');
const btnSubmit = document.getElementById('btnSubmit');
const messaggioCodice = document.getElementById('messaggioCodice');

// Nome con cui la bevanda e' registrata adesso: viaggia come parametro della
// PUT, cosi' il service sa quale riga aggiornare anche se il nome cambia.
const codiceVecchio = new URLSearchParams(window.location.search).get('codice') || '';

messaggioCodice.textContent = codiceVecchio ? ('Bevanda selezionata: ' + codiceVecchio) : 'Nessuna bevanda selezionata.';

function riempiForm(bevanda) {
  document.getElementById('nome').value = bevanda.nome || '';
  document.getElementById('quantitaBase').value = bevanda.quantitaBase != null ? bevanda.quantitaBase : '';
  document.getElementById('prezzoBase').value = bevanda.prezzoBase != null ? bevanda.prezzoBase : '';
  document.getElementById('alcolico').checked = bevanda.alcolico === true;
}

/*
 * La dto arriva dalla pagina di elenco tramite sessionStorage. Se la pagina
 * viene ricaricata quel dato non c'e' piu': in quel caso si richiama
 * direttamente /goToUpdateBevanda con il nome preso dalla query string.
 */
async function caricaBevanda() {
  nascondiEsito(esito);

  const salvata = sessionStorage.getItem(CHIAVE_SESSIONE);

  if (salvata) {
    sessionStorage.removeItem(CHIAVE_SESSIONE);
    riempiForm(JSON.parse(salvata));
    return;
  }

  if (!codiceVecchio) {
    mostraEsito(esito, 'Nessuna bevanda selezionata: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/goToUpdateBevanda/' + encodeURIComponent(codiceVecchio), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok && corpo) {
      riempiForm(corpo);
    } else {
      mostraEsito(esito, corpo || 'Bevanda non trovata.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function gestisciModifica(event) {
  event.preventDefault();
  nascondiEsito(esito);

  if (!codiceVecchio) {
    mostraEsito(esito, 'Nessuna bevanda selezionata: torna all\'elenco e premi Modifica.', 'error');
    return;
  }

  if (!(await accessoConsentito())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  const corpoRichiesta = {
    nome: document.getElementById('nome').value.trim(),
    quantitaBase: document.getElementById('quantitaBase').value,
    prezzoBase: document.getElementById('prezzoBase').value,
    alcolico: document.getElementById('alcolico').checked
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Salvataggio in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/modifyBevanda?codiceBevanda=' + encodeURIComponent(codiceVecchio), {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Bevanda modificata con successo.', 'success');
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante la modifica della bevanda.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Salva modifiche';
  }
};

caricaBevanda();
