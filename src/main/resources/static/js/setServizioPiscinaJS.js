/*
 * Script della pagina setServizioPiscina.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente. Restano fuori la lettura delle piscine e gli
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

const form = document.getElementById('formPiscina');
const esito = document.getElementById('esito');
const esitoElenco = document.getElementById('esitoElenco');
const btnSubmit = document.getElementById('btnSubmit');
const corpoTabella = document.getElementById('corpoTabella');

function mostraRigaVuota(messaggio) {
  corpoTabella.innerHTML = '';
  const riga = document.createElement('tr');
  const cella = document.createElement('td');
  cella.className = 'data-table__empty';
  cella.colSpan = 5;
  cella.textContent = messaggio;
  riga.appendChild(cella);
  corpoTabella.appendChild(riga);
}

function nominativoInterno(piscina) {
  if (!piscina.nomeOperatoreInterno && !piscina.cognomeOperatoreInterno) return '-';

  return (piscina.nomeOperatoreInterno || '') + ' ' + (piscina.cognomeOperatoreInterno || '');
}

function creaRiga(piscina) {
  const riga = document.createElement('tr');
  const valori = [
    piscina.codice,
    piscina.larghezza,
    piscina.lunghezza,
    nominativoInterno(piscina)
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
    modificaPiscina(piscina.codice);
  });
  cellaAzioni.appendChild(btnModifica);

  const btnElimina = document.createElement('button');
  btnElimina.type = 'button';
  btnElimina.className = 'btn btn-secondary btn-small';
  btnElimina.style.color = 'var(--color-rust)';
  btnElimina.textContent = 'Elimina';
  btnElimina.addEventListener('click', function () {
    eliminaPiscina(piscina.codice);
  });
  cellaAzioni.appendChild(btnElimina);
  riga.appendChild(cellaAzioni);

  return riga;
}

/*
 * Il click chiede la dto a /goToUpdatePiscina e la consegna alla pagina di
 * modifica tramite sessionStorage; il codice viaggia anche in query string,
 * cosi' updatePiscina.html sa quale riga sta modificando.
 */
async function modificaPiscina(codice) {
  nascondiEsito(esitoElenco);

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/goToUpdatePiscina/' + encodeURIComponent(codice), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok || !corpo) {
      mostraEsito(esitoElenco, corpo || 'Piscina non trovata.', 'error');
      return;
    }

    sessionStorage.setItem('piscinaDaModificare', JSON.stringify(corpo));
    window.location.href = '/hotel/dipendente/modifica-piscina?codice=' + encodeURIComponent(codice);
  } catch (errore) {
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function eliminaPiscina(codice) {
  nascondiEsito(esitoElenco);

  // Se l'utente annulla il pop-up non si chiama l'endpoint.
  if (!confirm('Sei sicuro di voler eliminare la piscina ' + codice + '?')) return;

  if (!(await accessoConsentito())) {
    mostraEsito(esitoElenco, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/removePiscina?codice=' + encodeURIComponent(codice), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      // Prima si ricarica l'elenco, poi si mostra l'esito: leggiPiscine()
      // comincia nascondendo il messaggio.
      await leggiPiscine();
      mostraEsito(esitoElenco, corpo || 'Piscina eliminata con successo.', 'success');
    } else {
      mostraEsito(esitoElenco, corpo || 'Errore durante la rimozione della piscina.', 'error');
    }
  } catch (errore) {
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function leggiPiscine() {
  nascondiEsito(esitoElenco);
  mostraRigaVuota('Caricamento in corso...');

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/readPiscine', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraRigaVuota('Nessun dato disponibile.');
      mostraEsito(esitoElenco, corpo || 'Errore durante la lettura delle piscine.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraRigaVuota('Nessuna piscina registrata.');
      return;
    }

    corpoTabella.innerHTML = '';
    corpo.forEach(function (piscina) {
      corpoTabella.appendChild(creaRiga(piscina));
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

  const corpoRichiesta = {
    codice: document.getElementById('codice').value.trim(),
    larghezza: Number(document.getElementById('larghezza').value),
    lunghezza: Number(document.getElementById('lunghezza').value),
    cfOperatoreInterno: cfOperatoreInterno || null
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Inserimento in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/addPiscina', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Piscina aggiunta con successo.', 'success');
      form.reset();
      leggiPiscine();
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante l\'inserimento della piscina.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Aggiungi piscina';
  }
};

document.getElementById('btnAggiorna').addEventListener('click', leggiPiscine);

leggiPiscine();
