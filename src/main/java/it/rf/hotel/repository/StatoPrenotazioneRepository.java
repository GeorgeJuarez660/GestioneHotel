package it.rf.hotel.repository;

import it.rf.hotel.model.StatoPrenotazione;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatoPrenotazioneRepository extends JpaRepository<StatoPrenotazione, Long> {
    
    public Optional<StatoPrenotazione> findByStato(String stato);

}
