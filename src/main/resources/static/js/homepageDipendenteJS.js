/*
 * Script della pagina homepageDipendente.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';

const avvisoDati = document.getElementById('avvisoDati');
const messaggioBenvenuto = document.getElementById('messaggioBenvenuto');
const esitoRiepilogo = document.getElementById('esitoRiepilogo');

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

const dipendente = recuperaToken();

if (!dipendente) {
  messaggioBenvenuto.textContent = 'Nessun accesso effettuato.';
} else {
  const username = decodificaUsernameToken(dipendente);
  messaggioBenvenuto.textContent = username ? ('Benvenuto, ' + username) : 'Accesso effettuato.';
}

/*
 * Verifica dell'accesso: /check deve restituire un dipendente. Il token da
 * solo non basta, puo' essere scaduto o non appartenere allo staff.
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

/*
 * Oltre alla classe si toglie anche l'href: cosi' la card resta inerte pure
 * da tastiera o se qualcuno forza il pointer-events da devtools.
 */
function bloccaCard() {
  document.querySelectorAll('.action-card').forEach(function (card) {
    card.classList.add('action-card--bloccata');
    card.removeAttribute('href');
    card.setAttribute('aria-disabled', 'true');
    card.setAttribute('tabindex', '-1');
  });
}

/*
 * Riepilogo: ogni card mostra quanti elementi restituisce il rispettivo
 * endpoint di lettura. L'inserimento vero e proprio (/addStanza e
 * /addNavetta) avviene nelle pagine dedicate raggiungibili dalle card.
 */
async function contaElementi(percorso, contatore) {
  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + percorso, {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok && Array.isArray(corpo)) {
      contatore.textContent = corpo.length;
    } else {
      contatore.textContent = '-';
      mostraEsito(esitoRiepilogo, corpo || 'Errore durante la lettura dei dati.', 'error');
    }
  } catch (errore) {
    contatore.textContent = '-';
    mostraEsito(esitoRiepilogo, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
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
  window.location.href = '/hotel/login-dipendente';
}

function caricaContatori() {
  contaElementi('/readPrenotazioni', document.getElementById('contatorePrenotazioni'));
  contaElementi('/readNavette', document.getElementById('contatoreNavette'));
  contaElementi('/readGuide', document.getElementById('contatoreGuide'));
  contaElementi('/readPiscine', document.getElementById('contatorePiscine'));
  contaElementi('/readStanze', document.getElementById('contatoreStanze'));
  contaElementi('/readPacchetti', document.getElementById('contatorePacchetti'));
  contaElementi('/readBevande', document.getElementById('contatoreBevande'));
  contaElementi('/readTaxi', document.getElementById('contatoreTaxi'));
  contaElementi('/readFeedback', document.getElementById('contatoreFeedback'));
}

async function verificaAccesso() {
  if (await accessoConsentito()) {
    caricaContatori();
    return;
  }

  // Accesso non riconosciuto: card spente e nessuna lettura dei riepiloghi.
  bloccaCard();
  avvisoDati.hidden = false;
  messaggioBenvenuto.textContent = 'Nessun accesso effettuato.';
  mostraEsito(esitoRiepilogo, MESSAGGIO_ACCESSO_NEGATO, 'error');
}

document.getElementById('logout').addEventListener('click', function () {
  logout();
});

verificaAccesso();
