package it.rf.hotel.repository;

import it.rf.hotel.model.StatoPagamento;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatoPagamentoRepository extends JpaRepository<StatoPagamento, Long> {

    public Optional<StatoPagamento> findByStato(String tipo);

}
