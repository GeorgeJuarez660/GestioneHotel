/*
 * Script della pagina insertPacchetti.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente. Restano fuori la lettura dei pacchetti e gli
 * spostamenti fra le pagine.
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

const form = document.getElementById('formPacchetto');
const esito = document.getElementById('esito');
const esitoElenco = document.getElementById('esitoElenco');
const btnSubmit = document.getElementById('btnSubmit');
const corpoTabella = document.getElementById('corpoTabella');

function mostraRigaVuota(messaggio) {
  corpoTabella.innerHTML = '';
  const riga = document.createElement('tr');
  const cella = document.createElement('td');
  cella.className = 'data-table__empty';
  cella.colSpan = 7;
  cella.textContent = messaggio;
  riga.appendChild(cella);
  corpoTabella.appendChild(riga);
}

function creaRiga(pacchetto) {
  const riga = document.createElement('tr');
  const valori = [
    pacchetto.tipoPensione,
    pacchetto.colazione ? 'Inclusa' : 'Non inclusa',
    pacchetto.navetta ? 'Si' : 'No',
    pacchetto.guida ? 'Si' : 'No',
    pacchetto.piscina ? 'Si' : 'No',
    pacchetto.parcheggio ? 'Si' : 'No',
    pacchetto.percentuale || '-',
    pacchetto.descrizione || '-'
  ];

  valori.forEach(function (valore) {
    const cella = document.createElement('td');
    cella.textContent = valore;
    riga.appendChild(cella);
  });

  const cellaAzioni = document.createElement('td');

  const btnModifica = document.createElement('button');
  btnModifica.type = 'button';
  btnModifica.className = 'btn btn-secondary btn-small';
  btnModifica.style.marginRight = '0.5rem';
  btnModifica.textContent = 'Modifica';
  btnModifica.addEventListener('click', function () {
    modificaPacchetto(pacchetto.tipoPensione);
  });
  cellaAzioni.appendChild(btnModifica);

  const btnElimina = document.createElement('button');
  btnElimina.type = 'button';
  btnElimina.className = 'btn btn-secondary btn-small';
  btnElimina.style.color = 'var(--color-rust)';
  btnElimina.textContent = 'Elimina';
  btnElimina.addEventListener('click', function () {
    eliminaPacchetto(pacchetto.tipoPensione);
  });
  cellaAzioni.appendChild(btnElimina);
  riga.appendChild(cellaAzioni);

  return riga;
}

/*
 * Il click chiede la dto a /goToUpdatePacchetto e la consegna alla pagina di
 * modifica tramite sessionStorage; il tipo viaggia anche in query string,
 * cosi' updatePacchetto.html sa quale riga sta modificando.
 */
async function modificaPacchetto(tipoPensione) {
  nascondiEsito(esitoElenco);

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/goToUpdatePacchetto/' + encodeURIComponent(tipoPensione), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok || !corpo) {
      mostraEsito(esitoElenco, corpo || 'Pacchetto non trovato.', 'error');
      return;
    }

    sessionStorage.setItem('pacchettoDaModificare', JSON.stringify(corpo));
    window.location.href = '/hotel/dipendente/modifica-pacchetto?tipoPensione=' + encodeURIComponent(tipoPensione);
  } catch (errore) {
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function eliminaPacchetto(tipoPensione) {
  nascondiEsito(esitoElenco);

  // Se l'utente annulla il pop-up non si chiama l'endpoint.
  if (!confirm('Sei sicuro di voler eliminare il pacchetto ' + tipoPensione + '?')) return;

  if (!(await accessoConsentito())) {
    mostraEsito(esitoElenco, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/removePacchetto?tipoPensione=' + encodeURIComponent(tipoPensione), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      // Prima si ricarica l'elenco, poi si mostra l'esito: leggiPacchetti()
      // comincia nascondendo il messaggio.
      await leggiPacchetti();
      mostraEsito(esitoElenco, corpo || 'Pacchetto eliminato con successo.', 'success');
    } else {
      mostraEsito(esitoElenco, corpo || 'Errore durante la rimozione del pacchetto.', 'error');
    }
  } catch (errore) {
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function leggiPacchetti() {
  nascondiEsito(esitoElenco);
  mostraRigaVuota('Caricamento in corso...');

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/readPacchetti', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraRigaVuota('Nessun dato disponibile.');
      mostraEsito(esitoElenco, corpo || 'Errore durante la lettura dei pacchetti.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraRigaVuota('Nessun pacchetto registrato.');
      return;
    }

    corpoTabella.innerHTML = '';
    corpo.forEach(function (pacchetto) {
      corpoTabella.appendChild(creaRiga(pacchetto));
    });
  } catch (errore) {
    mostraRigaVuota('Nessun dato disponibile.');
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function gestisciInserimento(event) {
  event.preventDefault();
  nascondiEsito(esito);

  if (!(await accessoConsentito())) {
    mostraEsito(esito, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  const corpoRichiesta = {
    tipoPensione: document.getElementById('tipoPensione').value,
    colazione: document.getElementById('colazione').checked,
    navetta: document.getElementById('navetta').checked,
    guida: document.getElementById('guida').checked,
    piscina: document.getElementById('piscina').checked,
    parcheggio: document.getElementById('parcheggio').checked,
    percentuale: document.getElementById('percentuale').value,
    descrizione: document.getElementById('descrizione').value.trim()
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Inserimento in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/addPacchetto', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Pacchetto aggiunto con successo.', 'success');
      form.reset();
      leggiPacchetti();
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante l\'inserimento del pacchetto.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Aggiungi pacchetto';
  }
};

document.getElementById('btnAggiorna').addEventListener('click', leggiPacchetti);

leggiPacchetti();
