package it.rf.hotel.repository;

import it.rf.hotel.model.CategoriaDipendente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaDipendenteRepository extends JpaRepository<CategoriaDipendente, Long> {
    
    public Optional<CategoriaDipendente> findByTipo(String tipo);

}
