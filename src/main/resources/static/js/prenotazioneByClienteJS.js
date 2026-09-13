/*
 * Script della pagina prenotazioneByCliente.html.
 * Estratto dal template: richiede common.js, caricato prima.
 *
 * Stessa tabella della pagina del dipendente, ma sugli endpoint del cliente e
 * con la sola azione di eliminazione: l'ospite non modifica le prenotazioni.
 */

const ENDPOINT_CLIENTE = API_BASE + '/homepage/cliente';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * /check restituisce il cliente collegato: serve come controllo di accesso e
 * come fonte del codice fiscale, che l'endpoint di lettura vuole nel percorso.
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

const esitoElenco = document.getElementById('esitoElenco');
const corpoTabella = document.getElementById('corpoTabella');

function mostraRigaVuota(messaggio) {
  corpoTabella.innerHTML = '';
  const riga = document.createElement('tr');
  const cella = document.createElement('td');
  cella.className = 'data-table__empty';
  cella.colSpan = 10;
  cella.textContent = messaggio;
  riga.appendChild(cella);
  corpoTabella.appendChild(riga);
}

/*
 * Completa una riga con i dati che l'elenco non porta con se'. In caso di
 * errore le celle restano con il trattino, senza far saltare la tabella.
 */
function creaCella(riga, valore) {
  const cella = document.createElement('td');
  cella.textContent = (valore === null || valore === undefined || valore === '') ? '-' : valore;
  riga.appendChild(cella);

  return cella;
}

function nominativo(nome, cognome) {
  const parti = [];

  if (nome) parti.push(nome);
  if (cognome) parti.push(cognome);

  return parti.join(' ');
}

/*
 * Raccoglie i valori diversi di un campo dentro il gruppo: se la prenotazione
 * arriva su piu' righe (per esempio una stanza per riga) i valori che
 * cambiano vengono mostrati insieme invece di perderne alcuni.
 */
function valoriDistinti(gruppo, estrai) {
  const valori = [];

  gruppo.forEach(function (voce) {
    const valore = estrai(voce);

    if (valore === null || valore === undefined || valore === '') return;

    const testo = String(valore);
    if (valori.indexOf(testo) === -1) valori.push(testo);
  });

  return valori.join(' / ');
}

// Una riga per codice: le voci con lo stesso codice finiscono nello stesso gruppo.
function raggruppaPerCodice(prenotazioni) {
  const gruppi = new Map();

  prenotazioni.forEach(function (prenotazione) {
    const codice = prenotazione.codice;

    if (!gruppi.has(codice)) gruppi.set(codice, []);

    gruppi.get(codice).push(prenotazione);
  });

  return Array.from(gruppi.values());
}

/*
 * Restituisce due righe: quella dei dati e, se ci sono note, una riga sotto
 * che occupa tutta la larghezza. Il chiamante le inserisce con un solo
 * appendChild perche' viaggiano dentro un frammento.
 */
function creaRigaNote(testo) {
  const riga = document.createElement('tr');
  riga.className = 'riga-note';

  const cella = document.createElement('td');
  cella.colSpan = 10;

  const etichetta = document.createElement('span');
  etichetta.className = 'riga-note__etichetta';
  etichetta.textContent = 'Note';

  cella.appendChild(etichetta);
  cella.appendChild(document.createTextNode(testo));
  riga.appendChild(cella);

  return riga;
}

function creaRiga(gruppo) {
  const prenotazione = gruppo[0];
  const frammento = document.createDocumentFragment();
  const riga = document.createElement('tr');

  creaCella(riga, prenotazione.codice);
  creaCella(riga, valoriDistinti(gruppo, function (voce) { return nominativo(voce.nomeCliente, voce.cognomeCliente); }));
  creaCella(riga, valoriDistinti(gruppo, function (voce) { return voce.dataPrenotazione; }));
  creaCella(riga, valoriDistinti(gruppo, function (voce) { return voce.dataInizio; }));
  creaCella(riga, valoriDistinti(gruppo, function (voce) { return voce.dataFine; }));
  creaCella(riga, valoriDistinti(gruppo, function (voce) { return voce.prezzoTotale; }));
  creaCella(riga, valoriDistinti(gruppo, function (voce) { return voce.prezzoEffettivo; }));
  creaCella(riga, valoriDistinti(gruppo, function (voce) { return voce.statoPrenotazione; }));
  creaCella(riga, valoriDistinti(gruppo, function (voce) { return nominativo(voce.nomeReceptionist, voce.cognomeReceptionist); }));

  const cellaAzioni = document.createElement('td');

  const btnElimina = document.createElement('button');
  btnElimina.type = 'button';
  btnElimina.className = 'btn btn-secondary btn-small';
  btnElimina.style.color = 'var(--color-rust)';
  btnElimina.textContent = 'Elimina';
  btnElimina.addEventListener('click', function () {
    eliminaPrenotazione(prenotazione.codice);
  });
  cellaAzioni.appendChild(btnElimina);
  riga.appendChild(cellaAzioni);

  frammento.appendChild(riga);

  const note = valoriDistinti(gruppo, function (voce) { return voce.note; });

  if (note && note !== '-') {
    frammento.appendChild(creaRigaNote(note));
  }

  return frammento;
}

async function eliminaPrenotazione(codice) {
  nascondiEsito(esitoElenco);

  // Se l'utente annulla il pop-up non si chiama l'endpoint.
  if (!confirm('Sei sicuro di voler eliminare la prenotazione ' + codice + '?')) return;

  if (!(await recuperaCliente())) {
    mostraEsito(esitoElenco, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_CLIENTE + '/removePrenotazione?codice=' + encodeURIComponent(codice), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      // Prima si ricarica l'elenco, poi si mostra l'esito: caricaPrenotazioni()
      // comincia nascondendo il messaggio.
      await caricaPrenotazioni();
      mostraEsito(esitoElenco, corpo || 'Prenotazione eliminata con successo.', 'success');
    } else {
      mostraEsito(esitoElenco, corpo || 'Errore durante la rimozione della prenotazione.', 'error');
    }
  } catch (errore) {
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function caricaPrenotazioni() {
  nascondiEsito(esitoElenco);
  mostraRigaVuota('Caricamento in corso...');

  const cliente = await recuperaCliente();

  if (!cliente) {
    mostraRigaVuota('Nessun dato disponibile.');
    mostraEsito(esitoElenco, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  if (!cliente.codiceFiscale) {
    mostraRigaVuota('Nessun dato disponibile.');
    mostraEsito(esitoElenco, 'Codice fiscale non disponibile per il cliente collegato.', 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_CLIENTE + '/readPrenotazioni/' + encodeURIComponent(cliente.codiceFiscale), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraRigaVuota('Nessun dato disponibile.');
      mostraEsito(esitoElenco, corpo || 'Errore durante la lettura delle prenotazioni.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraRigaVuota('Non hai ancora nessuna prenotazione.');
      return;
    }

    corpoTabella.innerHTML = '';
    raggruppaPerCodice(corpo).forEach(function (gruppo) {
      corpoTabella.appendChild(creaRiga(gruppo));
    });
  } catch (errore) {
    mostraRigaVuota('Nessun dato disponibile.');
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

document.getElementById('btnAggiorna').addEventListener('click', caricaPrenotazioni);

caricaPrenotazioni();
