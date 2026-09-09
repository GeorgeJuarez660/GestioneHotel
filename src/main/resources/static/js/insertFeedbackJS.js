/*
 * Script della pagina insertFeedback.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_CLIENTE = API_BASE + '/homepage/cliente';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un cliente.
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

const form = document.getElementById('formFeedback');
const esito = document.getElementById('esito');
const btnSubmit = document.getElementById('btnSubmit');

async function gestisciInserimento(event) {
  event.preventDefault();
  nascondiEsito(esito);

  if (!(await accessoConsentito())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  const corpoRichiesta = {
    note: document.getElementById('note').value.trim()
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Invio in corso...';

  try {
    const response = await fetch(ENDPOINT_CLIENTE + '/addFeedback', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + (recuperaToken() || '')
      },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Feedback inviato con successo.', 'success');
      form.reset();
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante l\'invio del feedback.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Invia feedback';
  }
};
