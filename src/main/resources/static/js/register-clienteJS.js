/*
 * Script della pagina register-cliente.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const form = document.getElementById('formRegisterCliente');
const esito = document.getElementById('esito');
const btnSubmit = document.getElementById('btnSubmit');

async function gestisciRegistrazione(event) {
  event.preventDefault();
  nascondiEsito(esito);

  const corpoRichiesta = {
    nome: document.getElementById('nome').value.trim(),
    cognome: document.getElementById('cognome').value.trim(),
    codiceFiscale: document.getElementById('codiceFiscale').value.trim(),
    dataNascita: document.getElementById('dataNascita').value,
    lingua: document.getElementById('lingua').value.trim(),
    username: document.getElementById('username').value.trim(),
    password: document.getElementById('password').value
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Registrazione in corso...';

  try {
    const response = await fetch(API_BASE + '/register/cliente', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Registrazione avvenuta con successo.', 'success');
      form.reset();
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante la registrazione.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Registrati';
  }
};
