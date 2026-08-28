/*
 * common.js - utility condivise dalle pagine del Gestionale Hotel
 *
 * Conservazione "semplice" del token: il token JWT restituito dal login
 * del cliente viene salvato in localStorage cosi' com'e', senza cifratura
 * ne' meccanismi di refresh. E' sufficiente per richiamare gli endpoint
 * protetti che si aspettano l'header "Authorization: Bearer <token>".
 */

const API_BASE = '/hotel';

const TOKEN_STORAGE_KEY = 'hotelClienteToken';

/* ---------- Token cliente ---------- */

function salvaToken(token) {
  localStorage.setItem(TOKEN_STORAGE_KEY, token);
}

function recuperaToken() {
  console.log('recuperaToken: ', localStorage.getItem(TOKEN_STORAGE_KEY));
  return localStorage.getItem(TOKEN_STORAGE_KEY);
}

// NB: nessuna pagina richiama ancora questa funzione: il logout non e'
// stato collegato lato frontend (vedi logout.html).
function rimuoviToken() {
  localStorage.removeItem(TOKEN_STORAGE_KEY);
}

/* ---------- Dati dipendente (il login dipendente non genera un token) ---------- */

/*function salvaDipendente(dipendente) {
  localStorage.setItem(DIPENDENTE_STORAGE_KEY, JSON.stringify(dipendente));
}

function recuperaDipendente() {
  const raw = localStorage.getItem(DIPENDENTE_STORAGE_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw);
  } catch (_) {
    return null;
  }
}

// NB: come rimuoviToken(), non ancora richiamata da nessuna pagina.
function rimuoviDipendente() {
  localStorage.removeItem(DIPENDENTE_STORAGE_KEY);
}*/

/* ---------- Lettura "al volo" dello username dal JWT (solo per mostrarlo a video) ---------- */

function decodificaUsernameToken(token) {
  try {
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64.replace(/-/g, '+').replace(/_/g, '/'));
    const payload = JSON.parse(payloadJson);
    return payload.sub || null;
  } catch (_) {
    return null;
  }
}

/* ---------- Helper per le risposte fetch ---------- */

// Le risposte degli endpoint sono a volte testo semplice, a volte JSON
// (es. l'elenco di errori di validazione): proviamo prima il JSON.
async function leggiCorpoRisposta(response) {
  const testo = await response.text();
  if (!testo) return '';
  try {
    return JSON.parse(testo);
  } catch (_) {
    return testo;
  }
}

/*
 * Mostra un messaggio di esito dentro un contenitore.
 * tipo: 'success' | 'error' | 'info'
 * messaggio: stringa oppure array di stringhe (es. errori di validazione)
 */
function mostraEsito(container, messaggio, tipo) {
  if (!container) return;
  tipo = tipo || 'info';
  container.textContent = '';
  container.className = 'esito esito--' + tipo;

  if (Array.isArray(messaggio)) {
    const lista = document.createElement('ul');
    messaggio.forEach(function (voce) {
      const li = document.createElement('li');
      li.textContent = voce;
      lista.appendChild(li);
    });
    container.appendChild(lista);
  } else if (typeof messaggio === 'object' && messaggio !== null) {
    container.textContent = JSON.stringify(messaggio);
  } else {
    container.textContent = messaggio;
  }

  container.hidden = false;
}

function nascondiEsito(container) {
  if (!container) return;
  container.hidden = true;
  container.textContent = '';
}
