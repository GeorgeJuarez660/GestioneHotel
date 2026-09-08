package it.rf.hotel.repository;

import it.rf.hotel.model.Dipendente;
import it.rf.hotel.model.OperatoreEsterno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface OperatoreEsternoRepository extends JpaRepository<OperatoreEsterno, Long> {
    public Optional<OperatoreEsterno> findByUsernameAndPasswordAndCodDipendenteEsterno(String username, String password, String codDipendenteEsterno);
    public long countByLingua(String lingua);

    public Optional<OperatoreEsterno> findByLingua(String lingua);

    @NativeQuery (value = "select d.codice_fiscale from operatori_esterni d left join prenotazioni p on(d.dipendente_id=p.dipendente_id) " + 
                "where d.lingua = ?1 " + 
                "group by d.codice_fiscale order by count(*) asc limit 1 ")
    public String findByLinguaAndPrenotazioni(String lingua);
    public Optional<OperatoreEsterno> findByCodiceFiscale(String codiceFiscale);
    public long countByUsernameAndPassword(String username, String password);
    public long countByCodiceFiscale(String codiceFiscale);
}
