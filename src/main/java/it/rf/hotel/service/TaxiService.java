package it.rf.hotel.service;

import it.rf.hotel.dto.TaxiRequest;
import it.rf.hotel.exception.NotClienteFoundExpcetion;
import it.rf.hotel.model.Gestisce;
import it.rf.hotel.model.Taxi;
import it.rf.hotel.repository.GestisceRepository;
import it.rf.hotel.repository.TaxiRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaxiService {

    @Autowired
    private TaxiRepository taxiRepository;

    @Autowired
    private GestisceRepository gestisceRepository;


    public String creaTaxi(TaxiRequest dto) throws NotClienteFoundExpcetion {

        Gestisce gestisce = gestisceRepository.findByNomeAndCognome(dto.getNomePossessore(), dto.getCognomePossessore()).orElse(null);

        if (gestisce == null) {
            throw new NotClienteFoundExpcetion(dto.getNomePossessore(), dto.getCognomePossessore());
        }
        else{

            Taxi taxi = new Taxi();
            taxi.setData(dto.getData());
            taxi.setOra(dto.getOra());
            taxi.setLuogoPartenza(dto.getLuogoPartenza());
            taxi.setLuogoDestinazione(dto.getLuogoDestinazione());
            taxi.setPrezzo(dto.getPrezzo());
            taxi.setNumPersone(dto.getNumPersone());
            taxi.setGestisce(gestisce);
            taxiRepository.save(taxi);

        }

        return "TAXI AGGIUNTO CON SUCCESSO";   
    }

    public List<TaxiRequest> elencoTaxi() {

        List<Taxi> corse = taxiRepository.findAll();
        List<TaxiRequest> elenco = new ArrayList<>();

        for(Taxi taxi : corse){
            elenco.add(costruisciDto(taxi));
        }

        return elenco;
    }

    public List<TaxiRequest> elencoTaxiPrenotazione(String nomePossessore, String cognomePossessore) {

        List<Taxi> corse = taxiRepository.findByNomeAndCognome(nomePossessore, cognomePossessore).orElse(null);
        List<TaxiRequest> elenco = new ArrayList<>();

        for(Taxi taxi : corse){
            elenco.add(costruisciDto(taxi));
        }

        return elenco;
    }

    public TaxiRequest trovaTaxi(Long id) {

        Optional<Taxi> taxiOpt = taxiRepository.findById(id);
        TaxiRequest dto = null;

        if (taxiOpt.isPresent()) {
            dto = costruisciDto(taxiOpt.get());
        }

        return dto;
    }

    public String aggiornaTaxi(Long id, TaxiRequest dto) {

        Taxi taxi = taxiRepository.findById(id).orElse(null);
        taxi.setData(dto.getData());
        taxi.setOra(dto.getOra());
        taxi.setLuogoPartenza(dto.getLuogoPartenza());
        taxi.setLuogoDestinazione(dto.getLuogoDestinazione());
        taxi.setPrezzo(dto.getPrezzo());
        taxi.setNumPersone(dto.getNumPersone());

        // La corsa puo' essere spostata su un'altra prenotazione.
        if (dto.getNomePossessore() != null && dto.getCognomePossessore() != null) {
            Gestisce gestisce = gestisceRepository.findByNomeAndCognome(dto.getNomePossessore(), dto.getCognomePossessore()).orElse(null);

            if (gestisce == null) {
                return "PRENOTAZIONE NON TROVATA";
            }

            taxi.setGestisce(gestisce);
        }

        taxiRepository.save(taxi);

        return "TAXI MODIFICATO CON SUCCESSO";
    }

    public String eliminaTaxi(Long id) {

        taxiRepository.deleteById(id);

        return "TAXI ELIMINATO CON SUCCESSO";
    }

    /* Totale delle corse addebitate su un soggiorno, usato per il conto. */
    public BigDecimal totaleTaxi(String codicePrenotazione) {

        List<Taxi> corse = taxiRepository.findByGestiscePrenotazioneCodice(codicePrenotazione);
        BigDecimal totale = BigDecimal.ZERO;

        for(Taxi taxi : corse){
            if (taxi.getPrezzo() != null) {
                totale = totale.add(taxi.getPrezzo());
            }
        }

        return totale;
    }

    private TaxiRequest costruisciDto(Taxi taxi) {

        TaxiRequest dto = new TaxiRequest();
        dto.setId(taxi.getId());
        dto.setData(taxi.getData());
        dto.setOra(taxi.getOra());
        dto.setLuogoPartenza(taxi.getLuogoPartenza());
        dto.setLuogoDestinazione(taxi.getLuogoDestinazione());
        dto.setPrezzo(taxi.getPrezzo());
        dto.setNumPersone(taxi.getNumPersone());

        if (taxi.getGestisce() != null && taxi.getGestisce().getPrenotazione() != null) {
            if (taxi.getGestisce().getPrenotazione().getCliente() != null) {
                dto.setNomePossessore(taxi.getGestisce().getPrenotazione().getCliente().getNome());
                dto.setCognomePossessore(taxi.getGestisce().getPrenotazione().getCliente().getCognome());
            }
        }

        return dto;
    }
}
