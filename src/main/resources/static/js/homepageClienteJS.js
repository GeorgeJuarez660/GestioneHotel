/*
 * Script della pagina homepageCliente.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const avvisoToken = document.getElementById('avvisoToken');
const messaggioBenvenuto = document.getElementById('messaggioBenvenuto');
const risultatoWrapper = document.getElementById('risultatoWrapper');
const risultato = document.getElementById('risultato');

const token = recuperaToken();

if (!token) {
  avvisoToken.hidden = false;
  messaggioBenvenuto.textContent = 'Nessun accesso effettuato.';
} else {
  const username = decodificaUsernameToken(token);
  messaggioBenvenuto.textContent = username ? ('Accesso effettuato come ' + username) : 'Accesso effettuato.';
}

async function chiamaEndpointProtetto(percorso) {
  risultatoWrapper.hidden = false;
  risultato.textContent = 'Richiesta in corso...';

  try {
    const response = await fetch(API_BASE + '/homepage/cliente/check', {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + (recuperaToken() || '')
      }
    });

    const corpo = await leggiCorpoRisposta(response);

    risultato.textContent = 'Stato HTTP: ' + response.status + '\n\n' +
      (typeof corpo === 'string' ? corpo : JSON.stringify(corpo, null, 2));

      window.location.href = percorso;
  } catch (errore) {
    risultato.textContent = 'Accesso negato! Riprova piu tardi.';
  }
}

async function logout() {
  const token = recuperaToken();
  if (token) {
    try {
      await fetch(API_BASE + '/hotel/logout', {
        method: 'GET',
        headers: { 'Authorization': 'Bearer ' + token }
      });
    } catch (e) {
      // anche se la chiamata fallisce, procedi comunque a pulire il client
    }
  }
  rimuoviToken();
  window.location.href = '/hotel/login-cliente';
}

document.getElementById('btnPrenota').addEventListener('click', function () {
  chiamaEndpointProtetto('/hotel/cliente/prenotazione');
});

document.getElementById('btnFeedback').addEventListener('click', function () {
  chiamaEndpointProtetto('/hotel/cliente/feedback');
});

document.getElementById('btnMieiFeedback').addEventListener('click', function () {
  chiamaEndpointProtetto('/hotel/cliente/miei-feedback');
});

document.getElementById('logout').addEventListener('click', function () {
  logout();
});
