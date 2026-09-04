package it.rf.hotel.repository;

import it.rf.hotel.model.MetodoPagamento;
import it.rf.hotel.model.Pagamento;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodoPagamentoRepository extends JpaRepository<MetodoPagamento, Long> {

    public Optional<MetodoPagamento> findByTipo(String tipo);

}
