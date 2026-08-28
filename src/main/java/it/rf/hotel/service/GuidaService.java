package it.rf.hotel.service;

import it.rf.hotel.dto.GuidaDto;
import it.rf.hotel.exception.CodiceDuplicatoException;
import it.rf.hotel.exception.GuidaReferencedException;
import it.rf.hotel.model.Dipendente;
import it.rf.hotel.model.Guida;
import it.rf.hotel.repository.DipendenteRepository;
import it.rf.hotel.repository.GuidaRepository;
import it.rf.hotel.repository.IncludeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GuidaService {

    @Autowired
    private GuidaRepository guidaRepository;

    @Autowired
    private IncludeRepository includeRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;


    public String creaGuida(GuidaDto dto) throws CodiceDuplicatoException {

        if (guidaRepository.countByCodice(dto.getCodice()) > 0L) {
            throw new CodiceDuplicatoException(dto.getCodice());
        }
        else{

            Guida guida = new Guida();
            guida.setCodice(dto.getCodice());
            guida.setData(dto.getData());
            guida.setOra(dto.getOra());
            guida.setLuogo(dto.getLuogo());

            if (dto.getCfOperatoreInterno() != null) {
                Dipendente operatoreInterno = dipendenteRepository.findByCodiceFiscale(dto.getCfOperatoreInterno()).orElse(null);
                guida.setOperatoreInterno(operatoreInterno);
            }

            /*if (dto.getOperatoreEsternoId() != null) {
                OperatoreEsterno operatoreEsterno = entityManager.getReference(OperatoreEsterno.class, dto.getOperatoreEsternoId());
                navetta.setOperatoreEsterno(operatoreEsterno);
            }*/

            guidaRepository.save(guida);

        }

        return "GUIDA AGGIUNTA CON SUCCESSO";
    }

    public List<GuidaDto> elencoGuide() {

        List<Guida> guide = guidaRepository.findAll();
        List<GuidaDto> elenco = new ArrayList<>();

        for(Guida guida : guide){
            GuidaDto dto = new GuidaDto();
            dto.setCodice(guida.getCodice());
            dto.setData(guida.getData());
            dto.setOra(guida.getOra());
            dto.setLuogo(guida.getLuogo());

            if (guida.getOperatoreInterno() != null) {
                dto.setCfOperatoreInterno(guida.getOperatoreInterno().getCodiceFiscale());
                dto.setNomeOperatoreInterno(guida.getOperatoreInterno().getNome());
                dto.setCognomeOperatoreInterno(guida.getOperatoreInterno().getCognome());
            }

            /*if (navetta.getOperatoreEsterno() != null) {
                dto.setOperatoreEsternoId(navetta.getOperatoreEsterno().getId());
            }*/

            elenco.add(dto);
        }

        return elenco;
    }

    public GuidaDto trovaGuida(String codiceGuida) {

        Optional<Guida> guidaOpt = guidaRepository.findByCodice(codiceGuida);
        Guida guida;
        GuidaDto dto = null;

        if (guidaOpt.isPresent()) {
            guida = guidaOpt.get();
            dto = new GuidaDto();

            dto.setCodice(guida.getCodice());
            dto.setData(guida.getData());
            dto.setOra(guida.getOra());
            dto.setLuogo(guida.getLuogo());

            if (guida.getOperatoreInterno() != null) {
                dto.setCfOperatoreInterno(guida.getOperatoreInterno().getCodiceFiscale());
                dto.setNomeOperatoreInterno(guida.getOperatoreInterno().getNome());
                dto.setCognomeOperatoreInterno(guida.getOperatoreInterno().getCognome());
            }
        }

        return dto;
    }

    public String aggiornaGuida(String codice, GuidaDto dto) throws CodiceDuplicatoException {

        if(codice.equals(dto.getCodice())){
            Guida guida = guidaRepository.findByCodice(codice).orElse(null);
            guida.setData(dto.getData());
            guida.setOra(dto.getOra());
            guida.setLuogo(dto.getLuogo());

            if (dto.getCfOperatoreInterno() != null) {
                Dipendente operatoreInterno = dipendenteRepository.findByCodiceFiscale(dto.getCfOperatoreInterno()).orElse(null);
                guida.setOperatoreInterno(operatoreInterno);
            }

            /*if (dto.getOperatoreEsternoId() != null) {
                OperatoreEsterno operatoreEsterno = entityManager.getReference(OperatoreEsterno.class, dto.getOperatoreEsternoId());
                navetta.setOperatoreEsterno(operatoreEsterno);
            }*/

            guidaRepository.save(guida);
        }
        else{
            creaGuida(dto);
        }

        return "GUIDA MODIFICATA CON SUCCESSO";
    }

    public String eliminaGuida(String codice) throws GuidaReferencedException  { 

        Long count = includeRepository.countByGuidaCodice(codice);
        if (count > 0) {
            throw new GuidaReferencedException(codice);
        }
        else{
            guidaRepository.deleteByCodice(codice); 

        }

        return "GUIDA ELIMINATA CON SUCCESSO";
    }
}
