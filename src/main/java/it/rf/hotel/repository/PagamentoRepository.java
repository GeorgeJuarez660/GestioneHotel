package it.rf.hotel.repository;

import it.rf.hotel.model.Pagamento;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    public Optional<Pagamento> findByPrenotazioneCodice(String codice);

    public Long countByPrenotazioneCodice(String codice);

    /*Spring Data richiedono una transazione attiva: senza, ottieni un InvalidDataAccessApiUsageException
     ("No EntityManager with actual transaction available for current thread"), oppure — se sei dentro
      una transazione in sola lettura — la delete viene semplicemente ignorata. */
    @Transactional 
    public void deleteByPrenotazioneCodice(String codice);

}
