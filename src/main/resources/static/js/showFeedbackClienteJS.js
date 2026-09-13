/*
 * Script della pagina showFeedbackCliente.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_CLIENTE = API_BASE + '/homepage/cliente';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso: /check restituisce il cliente collegato. Serve sia
 * come controllo (se manca, accesso negato) sia come fonte del codice
 * fiscale da passare a /readFeedback, che lo vuole nel percorso.
 */
async function recuperaCliente() {
  try {
    const response = await fetch(ENDPOINT_CLIENTE + '/check', {
      method: 'GET',
      headers: { 'Authorization': 'Bearer ' + (recuperaToken() || '') }
    });

    if (!response.ok) return null;

    const corpo = await leggiCorpoRisposta(response);

    if (corpo === null || typeof corpo !== 'object' || corpo.id == null) return null;

    return corpo;
  } catch (errore) {
    return null;
  }
}

const avvisoToken = document.getElementById('avvisoToken');
const messaggioBenvenuto = document.getElementById('messaggioBenvenuto');
const esitoElenco = document.getElementById('esitoElenco');
const corpoTabella = document.getElementById('corpoTabella');

function mostraRigaVuota(messaggio) {
  corpoTabella.innerHTML = '';
  const riga = document.createElement('tr');
  const cella = document.createElement('td');
  cella.className = 'data-table__empty';
  cella.colSpan = 2;
  cella.textContent = messaggio;
  riga.appendChild(cella);
  corpoTabella.appendChild(riga);
}

function creaRiga(feedback, indice) {
  const riga = document.createElement('tr');

  const cellaIndice = document.createElement('td');
  cellaIndice.className = 'cella-indice';
  cellaIndice.textContent = indice;
  riga.appendChild(cellaIndice);

  const cellaNote = document.createElement('td');
  cellaNote.className = 'cella-note';
  cellaNote.textContent = feedback.note || '-';
  riga.appendChild(cellaNote);

  return riga;
}

/*
 * Due passaggi: prima /check per sapere chi e' collegato, poi il suo codice
 * fiscale finisce nel percorso di /readFeedback. Senza cliente non si
 * interroga nemmeno il secondo endpoint.
 */
async function leggiFeedback() {
  nascondiEsito(esitoElenco);
  mostraRigaVuota('Caricamento in corso...');

  const cliente = await recuperaCliente();

  if (!cliente) {
    avvisoToken.hidden = false;
    mostraRigaVuota('Nessun dato disponibile.');
    mostraEsito(esitoElenco, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  avvisoToken.hidden = true;

  const codiceFiscale = cliente.codiceFiscale;

  if (!codiceFiscale) {
    mostraRigaVuota('Nessun dato disponibile.');
    mostraEsito(esitoElenco, 'Codice fiscale non disponibile per il cliente collegato.', 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_CLIENTE + '/readFeedback/' + encodeURIComponent(codiceFiscale), {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': 'Bearer ' + (recuperaToken() || '')
      }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraRigaVuota('Nessun dato disponibile.');
      mostraEsito(esitoElenco, corpo || 'Errore durante la lettura dei feedback.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraRigaVuota('Non hai ancora lasciato nessun feedback.');
      return;
    }

    corpoTabella.innerHTML = '';
    corpo.forEach(function (feedback, posizione) {
      corpoTabella.appendChild(creaRiga(feedback, posizione + 1));
    });
  } catch (errore) {
    mostraRigaVuota('Nessun dato disponibile.');
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

const token = recuperaToken();

if (!token) {
  avvisoToken.hidden = false;
  messaggioBenvenuto.textContent = 'Nessun accesso effettuato.';
} else {
  const username = decodificaUsernameToken(token);
  messaggioBenvenuto.textContent = username ? ('Accesso effettuato come ' + username) : 'Accesso effettuato.';
}

document.getElementById('btnAggiorna').addEventListener('click', leggiFeedback);

leggiFeedback();
