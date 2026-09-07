package it.rf.hotel.repository;

import it.rf.hotel.model.Bevanda;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BevandaRepository extends JpaRepository<Bevanda, Long> {

    public long countByNome(String nome);

    public Optional<Bevanda> findByNome(String nome);

    public Optional<List<Bevanda>> findByNomeIn(List<String> nome);

    /*Spring Data richiedono una transazione attiva: senza, ottieni un InvalidDataAccessApiUsageException
     ("No EntityManager with actual transaction available for current thread"), oppure — se sei dentro
      una transazione in sola lettura — la delete viene semplicemente ignorata. */
    @Transactional
    public void deleteByNome(String nome);
}
