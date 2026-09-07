package it.rf.hotel.service;

import it.rf.hotel.dto.StanzaDto;
import it.rf.hotel.exception.CodiceDuplicatoException;
import it.rf.hotel.exception.StanzaReferencedException;
import it.rf.hotel.model.Stanza;
import it.rf.hotel.model.TipoStanza;
import it.rf.hotel.repository.GestisceRepository;
import it.rf.hotel.repository.StanzaRepository;
import it.rf.hotel.repository.TipoStanzaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StanzaService {

    @Autowired
    private StanzaRepository stanzaRepository;

    @Autowired
    private TipoStanzaRepository tipoStanzaRepository;

    @Autowired
    private GestisceRepository gestisceRepository;

    public String creaStanza(StanzaDto dto) throws CodiceDuplicatoException {

        if (stanzaRepository.countByCodice(dto.getCodice()) > 0L) {
            throw new CodiceDuplicatoException(dto.getCodice());
        }
        else{

            Stanza stanza = new Stanza();
            stanza.setCodice(dto.getCodice());

            if(dto.getTipoStanza().equals("SINGOLA") && dto.getCapienza() == null){
                stanza.setCapienza(1);
            }
            else if(dto.getTipoStanza().equals("DOPPIA") && dto.getCapienza() == null){
                stanza.setCapienza(2);
            }
            else if(dto.getTipoStanza().equals("TRIPLA") && dto.getCapienza() == null){
                stanza.setCapienza(3);
            }
            else if(dto.getTipoStanza().equals("FAMILIARE") && dto.getCapienza() == null){
                stanza.setCapienza(4);
            }
            else if(dto.getTipoStanza().equals("MATRIMONIALE") && dto.getCapienza() == null){
                stanza.setCapienza(2);
            }
            else{
                stanza.setCapienza(dto.getCapienza());
            }
            stanza.setPiano(dto.getPiano());
            stanza.setPrezzoBase(dto.getPrezzoBase());
            stanza.setTermoregolabile(dto.getTermoregolabile());
            stanza.setNote(dto.getNote());


            TipoStanza tipo = tipoStanzaRepository.findByTipo(dto.getTipoStanza()).orElse(null);
            stanza.setTipo(tipo);

            stanzaRepository.save(stanza);

        }

        return "STANZA AGGIUNTA CON SUCCESSO";
    }

    public List<StanzaDto> elencoStanze() {

        List<Stanza> stanze = stanzaRepository.findAll();
        List<StanzaDto> elenco = new ArrayList<>();

        for(Stanza stanza : stanze){
            StanzaDto dto = new StanzaDto();
            dto.setCodice(stanza.getCodice());
            dto.setCapienza(stanza.getCapienza());
            dto.setPiano(stanza.getPiano());
            dto.setPrezzoBase(stanza.getPrezzoBase());
            dto.setTermoregolabile(stanza.getTermoregolabile());
            dto.setNote(stanza.getNote());

            if (stanza.getTipo() != null) {
                dto.setTipoStanza(stanza.getTipo().getTipo());
            }

            elenco.add(dto);
        }

        return elenco;
    }

    public StanzaDto trovaStanza(String codiceStanza) {

        Optional<Stanza> stanzaOpt = stanzaRepository.findByCodice(codiceStanza);
        Stanza stanza;
        StanzaDto dto = null;

        if (stanzaOpt.isPresent()) {
            stanza = stanzaOpt.get();

            dto = new StanzaDto();
            dto.setCodice(stanza.getCodice());
            dto.setCapienza(stanza.getCapienza());
            dto.setPiano(stanza.getPiano());
            dto.setPrezzoBase(stanza.getPrezzoBase());
            dto.setTermoregolabile(stanza.getTermoregolabile());
            dto.setNote(stanza.getNote());

            if (stanza.getTipo() != null) {
                dto.setTipoStanza(stanza.getTipo().getTipo());
            }
        }

        return dto;
    }

    public String aggiornaStanza(String codice, StanzaDto dto) throws CodiceDuplicatoException {
        if(codice.equals(dto.getCodice())){
            Stanza stanza = stanzaRepository.findByCodice(codice).orElse(null);
            stanza.setCapienza(dto.getCapienza());
            stanza.setPiano(dto.getPiano());
            stanza.setPrezzoBase(dto.getPrezzoBase());
            stanza.setTermoregolabile(dto.getTermoregolabile());
            stanza.setNote(dto.getNote());

            TipoStanza tipo = tipoStanzaRepository.findByTipo(dto.getTipoStanza()).orElse(null);
            stanza.setTipo(tipo);

            stanzaRepository.save(stanza);
        }
        else{
            if (stanzaRepository.countByCodice(dto.getCodice()) > 0L) {
                throw new CodiceDuplicatoException(dto.getCodice());
            }
            else{

                Stanza stanza = new Stanza();
                stanza.setCodice(dto.getCodice());
                stanza.setCapienza(dto.getCapienza());
                stanza.setPiano(dto.getPiano());
                stanza.setPrezzoBase(dto.getPrezzoBase());
                stanza.setTermoregolabile(dto.getTermoregolabile());
                stanza.setNote(dto.getNote());


                TipoStanza tipo = tipoStanzaRepository.findByTipo(dto.getTipoStanza()).orElse(null);
                stanza.setTipo(tipo);

                stanzaRepository.save(stanza);

            }
        }

        return "STANZA MODIFICATA CON SUCCESSO";
        
    }

    public String eliminaStanza(String codice) throws StanzaReferencedException  { 
        Long count = gestisceRepository.countByStanzaCodice(codice);
        if (count > 0) {
            throw new StanzaReferencedException(codice);
        }
        else{
            stanzaRepository.deleteByCodice(codice); 

        }
        
        return "STANZA RIMOSSA CON SUCCESSO";
    }
}
