/*
 * Script della pagina login-cliente.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const form = document.getElementById('formLoginCliente');
const esito = document.getElementById('esito');
const btnSubmit = document.getElementById('btnSubmit');
const PAGINA_OSPITE = API_BASE + '/homepage/cliente/';

async function gestisciLogin(event)  {
  event.preventDefault();
  nascondiEsito(esito);

  const username = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value;

  const params = new URLSearchParams({ username: username, password: password });

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Accesso in corso...';

  try {
    const response = await fetch(API_BASE + '/login/cliente?' + params.toString(), {
      method: 'GET'
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      // Il backend restituisce il token JWT come testo semplice.
      salvaToken(corpo);
      mostraEsito(esito, 'Accesso effettuato. Reindirizzamento in corso...', 'success');
      window.location.href = '/hotel/homepage/cliente/';
    } else {
      mostraEsito(esito, corpo || 'Credenziali non valide.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Accedi';
  }
};

const btnOspite = document.getElementById('btnOspite');

/*
 * Accesso come ospite: nessuna credenziale e nessun token: si controlla con
 * una fetch che la pagina risponda e poi ci si sposta.
 */
async function entraComeOspite() {
  nascondiEsito(esito);
  btnOspite.disabled = true;

  try {
    const response = await fetch(PAGINA_OSPITE, {
      method: 'GET',
      headers: { 'Accept': 'text/html' }
    });

    if (response.ok) {
      window.location.href = PAGINA_OSPITE;
      return;
    }

    const corpo = await leggiCorpoRisposta(response);
    mostraEsito(esito, corpo || 'Pagina non raggiungibile. Riprova piu tardi.', 'error');
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }

  btnOspite.disabled = false;
}

btnOspite.addEventListener('click', entraComeOspite);
