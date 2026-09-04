package it.rf.hotel.service;

import it.rf.hotel.dto.PiscinaDto;
import it.rf.hotel.exception.CodiceDuplicatoException;
import it.rf.hotel.exception.PiscinaReferencedException;
import it.rf.hotel.model.Dipendente;
import it.rf.hotel.model.Piscina;
import it.rf.hotel.repository.AccedeRepository;
import it.rf.hotel.repository.DipendenteRepository;
import it.rf.hotel.repository.PiscinaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PiscinaService
 {

    @Autowired
    private PiscinaRepository piscinaRepository;

    @Autowired
    private AccedeRepository accedeRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;


    public String creaPiscina(PiscinaDto dto) throws CodiceDuplicatoException {

        if (piscinaRepository.countByCodice(dto.getCodice()) > 0L) {
            throw new CodiceDuplicatoException(dto.getCodice());
        }
        else{

            Piscina piscina = new Piscina();
            piscina.setCodice(dto.getCodice());
            piscina.setLarghezza(dto.getLarghezza());
            piscina.setLunghezza(dto.getLunghezza());

            if (dto.getCfOperatoreInterno() != null) {
                Dipendente operatoreInterno = dipendenteRepository.findByCodiceFiscale(dto.getCfOperatoreInterno()).orElse(null);
                piscina.setOperatoreInterno(operatoreInterno);
            }

            /*if (dto.getOperatoreEsternoId() != null) {
                OperatoreEsterno operatoreEsterno = entityManager.getReference(OperatoreEsterno.class, dto.getOperatoreEsternoId());
                navetta.setOperatoreEsterno(operatoreEsterno);
            }*/

            piscinaRepository.save(piscina);

        }

        return "PISCINA AGGIUNTA CON SUCCESSO";
    }

    public List<PiscinaDto> elencoPiscine() {

        List<Piscina> piscine = piscinaRepository.findAll();
        List<PiscinaDto> elenco = new ArrayList<>();

        for(Piscina piscina : piscine){
            PiscinaDto dto = new PiscinaDto();
            dto.setCodice(piscina.getCodice());
            dto.setLarghezza(piscina.getLarghezza());
            dto.setLunghezza(piscina.getLunghezza());

            if (piscina.getOperatoreInterno() != null) {
                dto.setCfOperatoreInterno(piscina.getOperatoreInterno().getCodiceFiscale());
                dto.setNomeOperatoreInterno(piscina.getOperatoreInterno().getNome());
                dto.setCognomeOperatoreInterno(piscina.getOperatoreInterno().getCognome());
            }

            /*if (navetta.getOperatoreEsterno() != null) {
                dto.setOperatoreEsternoId(navetta.getOperatoreEsterno().getId());
            }*/

            elenco.add(dto);
        }

        return elenco;
    }

    public PiscinaDto trovaPiscina(String codiceGuida) {

        Optional<Piscina> piscinaOpt = piscinaRepository.findByCodice(codiceGuida);
        Piscina piscina;
        PiscinaDto dto = null;

        if (piscinaOpt.isPresent()) {
            piscina = piscinaOpt.get();
            dto = new PiscinaDto();

            dto.setCodice(piscina.getCodice());
            dto.setLarghezza(piscina.getLarghezza());
            dto.setLunghezza(piscina.getLunghezza());

            if (piscina.getOperatoreInterno() != null) {
                dto.setCfOperatoreInterno(piscina.getOperatoreInterno().getCodiceFiscale());
                dto.setNomeOperatoreInterno(piscina.getOperatoreInterno().getNome());
                dto.setCognomeOperatoreInterno(piscina.getOperatoreInterno().getCognome());
            }
        }

        return dto;
    }

    public String aggiornaPiscina(String codice, PiscinaDto dto) throws CodiceDuplicatoException {

        if(codice.equals(dto.getCodice())){
            Piscina piscina = piscinaRepository.findByCodice(codice).orElse(null);
            piscina.setLarghezza(dto.getLarghezza());
            piscina.setLunghezza(dto.getLunghezza());

            if (dto.getCfOperatoreInterno() != null) {
                Dipendente operatoreInterno = dipendenteRepository.findByCodiceFiscale(dto.getCfOperatoreInterno()).orElse(null);
                piscina.setOperatoreInterno(operatoreInterno);
            }

            /*if (dto.getOperatoreEsternoId() != null) {
                OperatoreEsterno operatoreEsterno = entityManager.getReference(OperatoreEsterno.class, dto.getOperatoreEsternoId());
                navetta.setOperatoreEsterno(operatoreEsterno);
            }*/

            piscinaRepository.save(piscina);
        }
        else{
            creaPiscina(dto);
        }

        return "PISCINA MODIFICATA CON SUCCESSO";
    }

    public String eliminaPiscina(String codice) throws PiscinaReferencedException  { 

        Long count = accedeRepository.countByPiscinaCodice(codice);
        if (count > 0) {
            throw new PiscinaReferencedException(codice);
        }
        else{
            piscinaRepository.deleteByCodice(codice); 

        }

        return "PISCINA ELIMINATA CON SUCCESSO";
    }
}
