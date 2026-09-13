package it.rf.hotel.repository;

import it.rf.hotel.model.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {
    public Optional<Dipendente> findByUsernameAndPasswordAndCodDipendente(String username, String password, String codDipendente);
    public long countByLingua(String lingua);

    public Optional<Dipendente> findByLingua(String lingua);

    @NativeQuery (value = "select d.codice_fiscale from dipendenti d left join prenotazioni p on(d.dipendente_id=p.dipendente_id) " + 
                "where d.lingua = ?1 " + 
                "group by d.codice_fiscale order by count(*) asc limit 1 ")
    public String findByLinguaAndPrenotazioni(String lingua);

    public Optional<Dipendente> findByCodiceFiscale(String codiceFiscale);
    public long countByUsernameAndPassword(String username, String password);
    public long countByCodiceFiscale(String codiceFiscale);
}
