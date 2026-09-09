/*
 * Script della pagina insertBevanda.html.
 * Estratto dal template: richiede common.js, caricato prima.
 */

const ENDPOINT_DIPENDENTE = API_BASE + '/homepage/dipendente';

const MESSAGGIO_ACCESSO_NEGATO = 'ACCESSO NEGATO: effettua di nuovo l\'accesso.';

/*
 * Verifica dell'accesso prima delle operazioni protette: /check deve
 * restituire un dipendente. Restano fuori la lettura delle bevande e gli
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

const form = document.getElementById('formBevanda');
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

function creaRiga(bevanda) {
  const riga = document.createElement('tr');
  const valori = [
    bevanda.nome,
    bevanda.quantitaBase,
    Number(bevanda.prezzoBase).toFixed(2) + ' €',
    bevanda.alcolico ? 'Si' : 'No'
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
    modificaBevanda(bevanda.nome);
  });
  cellaAzioni.appendChild(btnModifica);

  const btnElimina = document.createElement('button');
  btnElimina.type = 'button';
  btnElimina.className = 'btn btn-secondary btn-small';
  btnElimina.style.color = 'var(--color-rust)';
  btnElimina.textContent = 'Elimina';
  btnElimina.addEventListener('click', function () {
    eliminaBevanda(bevanda.nome);
  });
  cellaAzioni.appendChild(btnElimina);
  riga.appendChild(cellaAzioni);

  return riga;
}

/*
 * Il click chiede la dto a /goToUpdateBevanda e la consegna alla pagina di
 * modifica tramite sessionStorage; il nome viaggia anche in query string,
 * cosi' updateBevanda.html sa quale riga sta modificando.
 */
async function modificaBevanda(nome) {
  nascondiEsito(esitoElenco);

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/goToUpdateBevanda/' + encodeURIComponent(nome), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok || !corpo) {
      mostraEsito(esitoElenco, corpo || 'Bevanda non trovata.', 'error');
      return;
    }

    sessionStorage.setItem('bevandaDaModificare', JSON.stringify(corpo));
    window.location.href = '/hotel/dipendente/modifica-bevanda?codice=' + encodeURIComponent(nome);
  } catch (errore) {
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function eliminaBevanda(nome) {
  nascondiEsito(esitoElenco);

  // Se l'utente annulla il pop-up non si chiama l'endpoint.
  if (!confirm('Sei sicuro di voler eliminare la bevanda ' + nome + '?')) return;

  if (!(await accessoConsentito())) {
    mostraEsito(esitoElenco, MESSAGGIO_ACCESSO_NEGATO, 'error');
    return;
  }

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/removeBevanda?codice=' + encodeURIComponent(nome), {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      // Prima si ricarica l'elenco, poi si mostra l'esito: leggiBevande()
      // comincia nascondendo il messaggio.
      await leggiBevande();
      mostraEsito(esitoElenco, corpo || 'Bevanda eliminata con successo.', 'success');
    } else {
      mostraEsito(esitoElenco, corpo || 'Errore durante la rimozione della bevanda.', 'error');
    }
  } catch (errore) {
    mostraEsito(esitoElenco, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  }
}

async function leggiBevande() {
  nascondiEsito(esitoElenco);
  mostraRigaVuota('Caricamento in corso...');

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/readBevande', {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    });

    const corpo = await leggiCorpoRisposta(response);

    if (!response.ok) {
      mostraRigaVuota('Nessun dato disponibile.');
      mostraEsito(esitoElenco, corpo || 'Errore durante la lettura delle bevande.', 'error');
      return;
    }

    if (!Array.isArray(corpo) || corpo.length === 0) {
      mostraRigaVuota('Nessuna bevanda a listino.');
      return;
    }

    corpoTabella.innerHTML = '';
    corpo.forEach(function (bevanda) {
      corpoTabella.appendChild(creaRiga(bevanda));
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
    nome: document.getElementById('nome').value.trim(),
    quantitaBase: document.getElementById('quantitaBase').value,
    prezzoBase: document.getElementById('prezzoBase').value,
    alcolico: document.getElementById('alcolico').checked
  };

  btnSubmit.disabled = true;
  btnSubmit.textContent = 'Inserimento in corso...';

  try {
    const response = await fetch(ENDPOINT_DIPENDENTE + '/addBevanda', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(corpoRichiesta)
    });

    const corpo = await leggiCorpoRisposta(response);

    if (response.ok) {
      mostraEsito(esito, corpo || 'Bevanda aggiunta con successo.', 'success');
      form.reset();
      leggiBevande();
    } else {
      // In caso di errori di validazione il backend restituisce un array di messaggi.
      mostraEsito(esito, corpo || 'Errore durante l\'inserimento della bevanda.', 'error');
    }
  } catch (errore) {
    mostraEsito(esito, 'Impossibile contattare il server. Riprova piu tardi.', 'error');
  } finally {
    btnSubmit.disabled = false;
    btnSubmit.textContent = 'Aggiungi bevanda';
  }
};

document.getElementById('btnAggiorna').addEventListener('click', leggiBevande);

leggiBevande();
