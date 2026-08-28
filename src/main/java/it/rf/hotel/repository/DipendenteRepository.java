package it.rf.hotel.repository;

import it.rf.hotel.model.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {
    public Optional<Dipendente> findByUsernameAndPassword(String username, String password);
    public Optional<Dipendente> findByCodiceFiscale(String codiceFiscale);
    public long countByUsernameAndPassword(String username, String password);
    public long countByCodiceFiscale(String codiceFiscale);
}
