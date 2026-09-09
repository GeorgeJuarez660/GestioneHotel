/*
 * Script della pagina setServizioGuida.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente. Restano fuori la lettura delle guide e gli
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

const form = document.getElementById('formGuida');
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

function nominativoInterno(guida) {
  if (!guida.nomeOperatoreInterno && !guida.cognomeOperatoreInterno) return '-';

  return (guida.nomeOperatoreInterno || '') + ' ' + (guida.cognomeOperatoreInterno || '');
}

function creaRiga(guida) {
  const riga = document.createElement('tr');
  const valori = [
    guida.codice,
    guida.data,
    guida.ora,
    guida.luogo,
    nominativoInterno(guida),
    guida.cfOperatoreEsterno || '-'
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
    modificaGuida(guida.codice);
  });
  cellaAzioni.appendChild(btnModifica);

  const btnElimina = document.createElement('button');
  btnElimina.type = 'button';
  btnElimina.className = 'btn btn-secondary btn-small';
  btnElimina.style.color = 'var(--color-rust)';
  btnElimina.textContent = 'Elimina';
  btnElimina.addEventListener('click', function () {
    eliminaGuida(guida.codice);
  });
  cellaAzioni.appendChild(btnElimina);
  riga.appendChild(cellaAzioni);

  return riga;
}

/*
 * Il click chiede la dto a /goToUpdateGuida e la consegna alla pagina di
 * modifica tramite sessionStorage; il codice viaggia anche in query string,
 * cosi' updateGuida.html sa quale riga sta modificando.
 */
async function modificaGuida(codice) {
  nascondiEsito(esitoElenco);

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/goToUpdateGuida/' + encodeURIComponent(codice), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok || !corpo) {
      mostraEsito(esitoElenco, corpo || 'Visita non trovata.', 'error');
      return;
    }

    sessionStorage.setItem('guidaDaModificare', JSON.stringify(corpo));
    window.location.href = '/hotel/dipendente/modifica-guida?codice=' + encodeURIComponent(codice);
  } catch (errore) {
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function eliminaGuida(codice) {
  nascondiEsito(esitoElenco);

  // Se l'utente annulla il pop-up non si chiama l'endpoint.
  if (!confirm('Sei sicuro di voler eliminare la guida ' + codice + '?')) return;

  if (!(await accessoConsentito())) {
    mostraEsito(esitoElenco, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/removeGuida?codice=' + encodeURIComponent(codice), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      // Prima si ricarica l'elenco, poi si mostra l'esito: leggiGuide()
      // comincia nascondendo il messaggio.
      await leggiGuide();
      mostraEsito(esitoElenco, corpo || 'Guida eliminata con successo.', 'success');
    } else {
      mostraEsito(esitoElenco, corpo || 'Errore durante la rimozione della guida.', 'error');
    }
  } catch (errore) {
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function leggiGuide() {
  nascondiEsito(esitoElenco);
  mostraRigaVuota('Caricamento in corso...');

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/readGuide', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraRigaVuota('Nessun dato disponibile.');
      mostraEsito(esitoElenco, corpo || 'Errore durante la lettura delle guide.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraRigaVuota('Nessuna visita pianificata.');
      return;
    }

    corpoTabella.innerHTML = '';
    corpo.forEach(function (guida) {
      corpoTabella.appendChild(creaRiga(guida));
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

  const cfOperatoreInterno = document.getElementById('cfOperatoreInterno').value.trim();
  const cfOperatoreEsterno = document.getElementById('cfOperatoreEsterno').value.trim();

  const corpoRichiesta = {
    codice: document.getElementById('codice').value.trim(),
    data: document.getElementById('data').value,
    ora: document.getElementById('ora').value,
    luogo: document.getElementById('luogo').value.trim(),
    cfOperatoreInterno: cfOperatoreInterno || null,
    cfOperatoreEsterno: cfOperatoreEsterno || null
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Inserimento in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/addGuida', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Guida aggiunta con successo.', 'success');
      form.reset();
      leggiGuide();
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante l\'inserimento della guida.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Aggiungi guida';
  }
};

document.getElementById('btnAggiorna').addEventListener('click', leggiGuide);

leggiGuide();
