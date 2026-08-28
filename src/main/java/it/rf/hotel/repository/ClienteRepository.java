package it.rf.hotel.repository;

import it.rf.hotel.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    public Optional<Cliente> findByUsernameAndPassword(String username, String password);
    public Optional<Cliente> findByCodiceFiscale(String codiceFiscale);
    public long countByUsername(String username);
    public long countByCodiceFiscale(String codiceFiscale);
}
