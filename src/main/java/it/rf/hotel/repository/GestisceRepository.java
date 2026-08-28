package it.rf.hotel.repository;

import it.rf.hotel.model.Gestisce;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GestisceRepository extends JpaRepository<Gestisce, Long> {

    public Long countByStanzaCodice(String codice);

    public Long countByPrenotazioneCodice(String codice);

    public Long countByPacchettoTipoPensione(String tipo);

    @NativeQuery(value = "SELECT COUNT(*) FROM gestisce g, prenotazioni p, stanze s WHERE g.prenotazione_id=p.prenotazione_id and g.stanza_id=s.stanza_id and p.data_inizio >= ?1 and p.data_fine <= ?2 and s.codice = ?3")
    public long countByPrenotazioneDataInizioAndPrenotazioneDataFineAndStanzaCodice(LocalDate dataInizio, LocalDate dataFine, String codiceStanza);


    public Optional<Gestisce> findByPrenotazioneCodice(String codice);

    /*Spring Data richiedono una transazione attiva: senza, ottieni un InvalidDataAccessApiUsageException
     ("No EntityManager with actual transaction available for current thread"), oppure — se sei dentro
      una transazione in sola lettura — la delete viene semplicemente ignorata. */
    @Transactional 
    public void deleteByPrenotazioneCodice(String codice);
}
