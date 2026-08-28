package it.rf.hotel.repository;

import it.rf.hotel.model.Navetta;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NavettaRepository extends JpaRepository<Navetta, Long> {

    public long countByCodice(String codice);

    public Optional<Navetta> findByCodice(String codice);

    public Optional<List<Navetta>> findByCodiceIn(List<String> codice);

    /*Spring Data richiedono una transazione attiva: senza, ottieni un InvalidDataAccessApiUsageException
     ("No EntityManager with actual transaction available for current thread"), oppure — se sei dentro
      una transazione in sola lettura — la delete viene semplicemente ignorata. */
    @Transactional 
    public void deleteByCodice(String codice);
}
