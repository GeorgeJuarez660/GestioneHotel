package it.rf.hotel.repository;

import it.rf.hotel.model.Taxi;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TaxiRepository extends JpaRepository<Taxi, Long> {

    public long countByGestiscePrenotazioneCodice(String codice);

    /* Sulla stessa prenotazione si possono registrare piu' corse, quindi
       il risultato e' una lista e non un Optional. */
    public List<Taxi> findByGestiscePrenotazioneCodice(String codice);

    @NativeQuery(value = "SELECT t.* from taxi t, gestisce g, prenotazioni p, clienti c where t.gestisce_id=g.gestisce_id and g.prenotazione_id=p.prenotazione_id and p.cliente_id=c.cliente_id " + 
                " and c.nome = ?1 and c.cognome = ?2")
    public Optional<List<Taxi>> findByNomeAndCognome(String nome, String cognome);

    /*Spring Data richiedono una transazione attiva: senza, ottieni un InvalidDataAccessApiUsageException
     ("No EntityManager with actual transaction available for current thread"), oppure — se sei dentro
      una transazione in sola lettura — la delete viene semplicemente ignorata. */
    @Transactional
    public void deleteByGestiscePrenotazioneCodice(String codice);
}
