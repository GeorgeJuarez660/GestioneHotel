package it.rf.hotel.service;

import it.rf.hotel.dto.NavettaDto;
import it.rf.hotel.exception.CodiceDuplicatoException;
import it.rf.hotel.exception.NavettaReferencedException;
import it.rf.hotel.model.Dipendente;
import it.rf.hotel.model.Navetta;
import it.rf.hotel.model.OperatoreEsterno;
import it.rf.hotel.repository.ComprendeRepository;
import it.rf.hotel.repository.DipendenteRepository;
import it.rf.hotel.repository.NavettaRepository;
import it.rf.hotel.repository.OperatoreEsternoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NavettaService {

    @Autowired
    private NavettaRepository navettaRepository;

    @Autowired
    private ComprendeRepository comprendeRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private OperatoreEsternoRepository operatoreEsternoRepository;


    public String creaNavetta(NavettaDto dto) throws CodiceDuplicatoException {

        if (navettaRepository.countByCodice(dto.getCodice()) > 0L) {
            throw new CodiceDuplicatoException(dto.getCodice());
        }
        else{

            Navetta navetta = new Navetta();
            navetta.setCodice(dto.getCodice());
            navetta.setDataPartenza(dto.getDataPartenza());
            navetta.setOraPartenza(dto.getOraPartenza());
            navetta.setLuogoDestinazione(dto.getLuogoDestinazione());
            navetta.setLuogoPartenza(dto.getLuogoPartenza());
            navetta.setNumPostiMax(dto.getNumPostiMax());
            navetta.setNumPostiDisp(dto.getNumPostiDisp());

            if (dto.getCfOperatoreInterno() != null) {
                Dipendente operatoreInterno = dipendenteRepository.findByCodiceFiscale(dto.getCfOperatoreInterno()).orElse(null);
                navetta.setOperatoreInterno(operatoreInterno);
            }

            /*if (dto.getOperatoreEsternoId() != null) {
                OperatoreEsterno operatoreEsterno = entityManager.getReference(OperatoreEsterno.class, dto.getOperatoreEsternoId());
                navetta.setOperatoreEsterno(operatoreEsterno);
            }*/

            navettaRepository.save(navetta);

        }

        return "NAVETTA AGGIUNTA CON SUCCESSO";
    }

    public List<NavettaDto> elencoNavette() {

        List<Navetta> navette = navettaRepository.findAll();
        List<NavettaDto> elenco = new ArrayList<>();

        for(Navetta navetta : navette){
            NavettaDto dto = new NavettaDto();
            dto.setCodice(navetta.getCodice());
            dto.setDataPartenza(navetta.getDataPartenza());
            dto.setOraPartenza(navetta.getOraPartenza());
            dto.setLuogoDestinazione(navetta.getLuogoDestinazione());
            dto.setLuogoPartenza(navetta.getLuogoPartenza());
            dto.setNumPostiDisp(navetta.getNumPostiDisp());
            dto.setNumPostiMax(navetta.getNumPostiMax());

            if (navetta.getOperatoreInterno() != null) {
                dto.setCfOperatoreInterno(navetta.getOperatoreInterno().getCodiceFiscale());
                dto.setNomeOperatoreInterno(navetta.getOperatoreInterno().getNome());
                dto.setCognomeOperatoreInterno(navetta.getOperatoreInterno().getCognome());
            }

            /*if (navetta.getOperatoreEsterno() != null) {
                dto.setOperatoreEsternoId(navetta.getOperatoreEsterno().getId());
            }*/

            elenco.add(dto);
        }

        return elenco;
    }

    public NavettaDto trovaNavetta(String codiceNavetta) {

        Optional<Navetta> navettaOpt = navettaRepository.findByCodice(codiceNavetta);
        Navetta navetta;
        NavettaDto dto = null;

        if (navettaOpt.isPresent()) {
            navetta = navettaOpt.get();
            dto = new NavettaDto();

            dto.setCodice(navetta.getCodice());
            dto.setDataPartenza(navetta.getDataPartenza());
            dto.setOraPartenza(navetta.getOraPartenza());
            dto.setLuogoDestinazione(navetta.getLuogoDestinazione());
            dto.setLuogoPartenza(navetta.getLuogoPartenza());
            dto.setNumPostiDisp(navetta.getNumPostiDisp());
            dto.setNumPostiMax(navetta.getNumPostiMax());

            if (navetta.getOperatoreInterno() != null) {
                dto.setCfOperatoreInterno(navetta.getOperatoreInterno().getCodiceFiscale());
                dto.setNomeOperatoreInterno(navetta.getOperatoreInterno().getNome());
                dto.setCognomeOperatoreInterno(navetta.getOperatoreInterno().getCognome());
            }
        }

        return dto;
    }

    public String aggiornaNavetta(String codice, NavettaDto dto) throws CodiceDuplicatoException {

        if(codice.equals(dto.getCodice())){
            Navetta navetta = navettaRepository.findByCodice(codice).orElse(null);
            navetta.setDataPartenza(dto.getDataPartenza());
            navetta.setOraPartenza(dto.getOraPartenza());
            navetta.setLuogoDestinazione(dto.getLuogoDestinazione());
            navetta.setLuogoPartenza(dto.getLuogoPartenza());
            navetta.setNumPostiMax(dto.getNumPostiMax());
            navetta.setNumPostiDisp(dto.getNumPostiDisp());

            if (dto.getCfOperatoreInterno() != null) {
                Dipendente operatoreInterno = dipendenteRepository.findByCodiceFiscale(dto.getCfOperatoreInterno()).orElse(null);
                navetta.setOperatoreInterno(operatoreInterno);
            }

            if (dto.getCfOperatoreEsterno() != null) {
                OperatoreEsterno operatoreEsterno = operatoreEsternoRepository.findByCodiceFiscale(dto.getCfOperatoreEsterno()).orElse(null);
                navetta.setOperatoreEsterno(operatoreEsterno);
            }

            navettaRepository.save(navetta);
        }
        else{
            
            creaNavetta(dto);
        }

        return "NAVETTA MODIFICATA CON SUCCESSO";
    }

    public String eliminaNavetta(String codice) throws NavettaReferencedException  { 

        Long count = comprendeRepository.countByNavettaCodice(codice);
        if (count > 0) {
            throw new NavettaReferencedException(codice);
        }
        else{
            navettaRepository.deleteByCodice(codice); 

        }

        return "NAVETTA ELIMINATA CON SUCCESSO";
    }
}
