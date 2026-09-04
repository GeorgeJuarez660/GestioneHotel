package it.rf.hotel.repository;

import it.rf.hotel.model.Comprende;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ComprendeRepository extends JpaRepository<Comprende, Long> {

    public Optional<Comprende> findByPrenotazioneCodice(String prenotazioneCodice);
    
    public Long countByNavettaCodice(String codice);

    public Long countByPrenotazioneCodice(String codice);

    /*Spring Data richiedono una transazione attiva: senza, ottieni un InvalidDataAccessApiUsageException
     ("No EntityManager with actual transaction available for current thread"), oppure — se sei dentro
      una transazione in sola lettura — la delete viene semplicemente ignorata. */
    @Transactional 
    public void deleteByPrenotazioneCodice(String codice);
}
