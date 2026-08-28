package it.rf.hotel.service;

import it.rf.hotel.dto.PacchettoDto;
import it.rf.hotel.exception.CodiceDuplicatoException;
import it.rf.hotel.exception.PacchettoReferencedException;
import it.rf.hotel.model.Pacchetto;
import it.rf.hotel.repository.GestisceRepository;
import it.rf.hotel.repository.PacchettoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PacchettoService {

    @Autowired
    private PacchettoRepository pacchettoRepository;

    @Autowired
    private GestisceRepository gestisceRepository;


    public String creaPacchetto(PacchettoDto dto) throws CodiceDuplicatoException {

        if (pacchettoRepository.countByTipoPensione(dto.getTipoPensione()) > 0L) {
            throw new CodiceDuplicatoException(dto.getTipoPensione());
        }
        else{

            Pacchetto pacchetto = new Pacchetto();
            pacchetto.setTipoPensione(dto.getTipoPensione());
            pacchetto.setNavetta(dto.isNavetta());
            pacchetto.setGuida(dto.isGuida());
            pacchetto.setPiscina(dto.isPiscina());
            pacchetto.setParcheggio(dto.isParcheggio());
            pacchetto.setColazione(dto.isColazione());
            pacchetto.setDescrizione(dto.getDescrizione());
            pacchetto.setPercentuale(dto.getPercentuale());

            pacchettoRepository.save(pacchetto);

        }

        return "PACCHETTO AGGIUNTO CON SUCCESSO";
    }

    public List<PacchettoDto> elencoPacchetti() { 

        List<PacchettoDto> pacchettiDtos = new ArrayList<>();
        List<Pacchetto> pacchetti = pacchettoRepository.findAll();

        for (Pacchetto pacchetto : pacchetti) {
            PacchettoDto dto = new PacchettoDto();
            dto.setTipoPensione(pacchetto.getTipoPensione());
            dto.setNavetta(pacchetto.getNavetta());
            dto.setGuida(pacchetto.getGuida());
            dto.setPiscina(pacchetto.getPiscina());
            dto.setParcheggio(pacchetto.getParcheggio());
            dto.setColazione(pacchetto.getColazione());
            dto.setDescrizione(pacchetto.getDescrizione());
            dto.setPercentuale(pacchetto.getPercentuale());
            pacchettiDtos.add(dto);
        }

        return pacchettiDtos;
    }

    public PacchettoDto trovaPacchetto(String tipoPacchetto) {

        Optional<Pacchetto> pacchettoOpt = pacchettoRepository.findByTipoPensione(tipoPacchetto);
        Pacchetto pacchetto;
        PacchettoDto pacchettoDto = null;

        if (pacchettoOpt.isPresent()) {
            pacchetto = pacchettoOpt.get();

            pacchettoDto = new PacchettoDto();
            pacchettoDto.setTipoPensione(pacchetto.getTipoPensione());
            pacchettoDto.setNavetta(pacchetto.getNavetta());
            pacchettoDto.setGuida(pacchetto.getGuida());
            pacchettoDto.setPiscina(pacchetto.getPiscina());
            pacchettoDto.setParcheggio(pacchetto.getParcheggio());
            pacchettoDto.setColazione(pacchetto.getColazione());
            pacchettoDto.setDescrizione(pacchetto.getDescrizione());
            pacchettoDto.setPercentuale(pacchetto.getPercentuale());
        }

        return pacchettoDto;
    }

    public Pacchetto trovaPacchettoPerTipo(String tipoPensione) {

        return pacchettoRepository.findByTipoPensione(tipoPensione).orElse(null);
    }

    public String aggiornaPacchetto(String codice, PacchettoDto dto) throws CodiceDuplicatoException {
        
        if(codice.equals(dto.getTipoPensione())){
            Pacchetto pacchetto = pacchettoRepository.findByTipoPensione(codice).orElse(null);
            pacchetto.setNavetta(dto.isNavetta());
            pacchetto.setGuida(dto.isGuida());
            pacchetto.setPiscina(dto.isPiscina());
            pacchetto.setParcheggio(dto.isParcheggio());
            pacchetto.setColazione(dto.isColazione());
            pacchetto.setDescrizione(dto.getDescrizione());
            pacchetto.setPercentuale(dto.getPercentuale());

            pacchettoRepository.save(pacchetto);
        }
        else{
            creaPacchetto(dto);
        }

        return "PACCHETTO MODIFICATO CON SUCCESSO";
    }

    public String eliminaPacchetto(String tipoPensione) throws PacchettoReferencedException { 
        Long count = gestisceRepository.countByPacchettoTipoPensione(tipoPensione);
        if (count > 0) {
            throw new PacchettoReferencedException(tipoPensione);
        }
        else{
            pacchettoRepository.deleteByTipoPensione(tipoPensione);
        }
        
        return "PACCHETTO RIMOSSO CON SUCCESSO";
    }
}
