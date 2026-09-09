/*
 * Script della pagina showFeedbackDipendente.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente.
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

const esitoElenco = document.getElementById('esitoElenco');
const corpoTabella = document.getElementById('corpoTabella');

function mostraRigaVuota(messaggio) {
  corpoTabella.innerHTML = '';
  const riga = document.createElement('tr');
  const cella = document.createElement('td');
  cella.className = 'data-table__empty';
  cella.colSpan = 3;
  cella.textContent = messaggio;
  riga.appendChild(cella);
  corpoTabella.appendChild(riga);
}

function creaRiga(feedback, indice) {
  const riga = document.createElement('tr');

  const cellaIndice = document.createElement('td');
  cellaIndice.textContent = indice;
  riga.appendChild(cellaIndice);

  const cellaCf = document.createElement('td');
  cellaCf.className = 'cella-cf';
  cellaCf.textContent = feedback.nomeCliente + ' ' + feedback.cognomeCliente || '-';
  riga.appendChild(cellaCf);

  const cellaNote = document.createElement('td');
  cellaNote.className = 'cella-note';
  cellaNote.textContent = feedback.note || '-';
  riga.appendChild(cellaNote);

  return riga;
}

// Lettura senza condizioni: l'endpoint del dipendente restituisce i feedback
// di tutti i clienti.
async function leggiFeedback() {
  nascondiEsito(esitoElenco);
  mostraRigaVuota('Caricamento in corso...');

  if (!(await accessoConsentito())) {
    mostraRigaVuota('Nessun dato disponibile.');
    mostraEsito(esitoElenco, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/readFeedback', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraRigaVuota('Nessun dato disponibile.');
      mostraEsito(esitoElenco, corpo || 'Errore durante la lettura dei feedback.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraRigaVuota('Nessun feedback registrato.');
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

document.getElementById('btnAggiorna').addEventListener('click', leggiFeedback);

leggiFeedback();
