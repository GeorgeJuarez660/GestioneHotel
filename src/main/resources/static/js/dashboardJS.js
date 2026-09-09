/*
 * Script della pagina dashboard.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const PAGINA_LOGIN = API_BASE + '/login-cliente';
const PAGINA_OSPITE = API_BASE + '/homepage/cliente/';

const esito = document.getElementById('esito');
const btnLogin = document.getElementById('btnLogin');
const btnOspite = document.getElementById('btnOspite');

/*
 * Prima di spostarsi si interroga l'endpoint con una fetch: se il server
 * risponde male si mostra l'errore qui, invece di lasciare l'utente su una
 * pagina di errore del browser.
 */
async function vaiA(percorso, bottone) {
  nascondiEsito(esito);
  bottone.disabled = true;

  try {
    const response = await fetch(percorso, {
      method: 'GET',
      headers: { 'Accept': 'text/html' }
    });

    if (response.ok) {
      window.location.href = percorso;
      return;
    }

    const corpo = await leggiCorpoRisposta(response);
    mostraEsito(esito, corpo || 'Pagina non raggiungibile. Riprova piu tardi.', 'error');
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }

  bottone.disabled = false;
}

btnLogin.addEventListener('click', function () {
  vaiA(PAGINA_LOGIN, btnLogin);
});

// Accesso come ospite: nessun token richiesto, la homepage cliente gestisce
// gia' il caso "nessun accesso effettuato".
btnOspite.addEventListener('click', function () {
  vaiA(PAGINA_OSPITE, btnOspite);
});
