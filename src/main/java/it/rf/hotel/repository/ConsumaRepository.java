package it.rf.hotel.repository;

import it.rf.hotel.model.Consuma;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ConsumaRepository extends JpaRepository<Consuma, Long> {

    /* A differenza di Comprende qui il risultato e' una lista: sullo stesso
       soggiorno si possono registrare piu' consumazioni. */
    public Consuma findByGestiscePrenotazioneCodiceAndBevandaNome(String prenotazioneCodice, String nomeBevanda);

    public Long countByBevandaNome(String nome);

    public Long countByGestiscePrenotazioneCodice(String codice);

    /*Spring Data richiedono una transazione attiva: senza, ottieni un InvalidDataAccessApiUsageException
     ("No EntityManager with actual transaction available for current thread"), oppure — se sei dentro
      una transazione in sola lettura — la delete viene semplicemente ignorata. */
    @Transactional
    public void deleteByGestiscePrenotazioneCodiceAndBevandaNome(String codice, String nomeBevanda);
}
