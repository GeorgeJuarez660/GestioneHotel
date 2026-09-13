package it.rf.hotel.service;

import it.rf.hotel.dto.BevandaResponse;
import it.rf.hotel.exception.BevandaReferencedException;
import it.rf.hotel.exception.CodiceDuplicatoException;
import it.rf.hotel.model.Bevanda;
import it.rf.hotel.repository.BevandaRepository;
import it.rf.hotel.repository.ConsumaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BevandaService {

    @Autowired
    private BevandaRepository bevandaRepository;

    @Autowired
    private ConsumaRepository consumaRepository;


    /* La bevanda non ha un codice: la chiave di business e' il nome, quindi
       e' su quello che si controllano i duplicati. */
    public String creaBevanda(BevandaResponse dto) {

        if (bevandaRepository.countByNome(dto.getNome()) > 0L) {
            Bevanda bevanda = bevandaRepository.findByNome(dto.getNome()).orElse(null);
            bevanda.setQuantitaBase(bevanda.getQuantitaBase()+1);
            bevandaRepository.save(bevanda);
        }
        else{

            Bevanda bevanda = new Bevanda();
            bevanda.setNome(dto.getNome());
            bevanda.setQuantitaBase(dto.getQuantitaBase());
            bevanda.setPrezzoBase(dto.getPrezzoBase());
            bevanda.setAlcolico(dto.getAlcolico() != null ? dto.getAlcolico() : false);

            bevandaRepository.save(bevanda);

        }

        return "BEVANDA AGGIUNTA CON SUCCESSO";
    }

    public List<BevandaResponse> elencoBevande() {

        List<Bevanda> bevande = bevandaRepository.findAll();
        List<BevandaResponse> elenco = new ArrayList<>();

        for(Bevanda bevanda : bevande){
            BevandaResponse dto = new BevandaResponse();
            dto.setNome(bevanda.getNome());
            dto.setQuantitaBase(bevanda.getQuantitaBase());
            dto.setPrezzoBase(bevanda.getPrezzoBase());
            dto.setAlcolico(bevanda.getAlcolico());

            elenco.add(dto);
        }

        return elenco;
    }

    public BevandaResponse trovaBevanda(String nomeBevanda) {

        Optional<Bevanda> bevandaOpt = bevandaRepository.findByNome(nomeBevanda);
        Bevanda bevanda;
        BevandaResponse dto = null;

        if (bevandaOpt.isPresent()) {
            bevanda = bevandaOpt.get();
            dto = new BevandaResponse();

            dto.setNome(bevanda.getNome());
            dto.setQuantitaBase(bevanda.getQuantitaBase());
            dto.setPrezzoBase(bevanda.getPrezzoBase());
            dto.setAlcolico(bevanda.getAlcolico());
        }

        return dto;
    }

    public String aggiornaBevanda(String nome, BevandaResponse dto) throws CodiceDuplicatoException {

        if(nome.equals(dto.getNome())){
            Bevanda bevanda = bevandaRepository.findByNome(nome).orElse(null);
            bevanda.setQuantitaBase(dto.getQuantitaBase());
            bevanda.setPrezzoBase(dto.getPrezzoBase());
            bevanda.setAlcolico(dto.getAlcolico() != null ? dto.getAlcolico() : false);

            bevandaRepository.save(bevanda);
        }
        else{

            creaBevanda(dto);
        }

        return "BEVANDA MODIFICATA CON SUCCESSO";
    }

    public String eliminaBevanda(String nome) throws BevandaReferencedException {

        Long count = consumaRepository.countByBevandaNome(nome);
        if (count > 0) {
            throw new BevandaReferencedException(nome);
        }
        else{
            bevandaRepository.deleteByNome(nome);

        }

        return "BEVANDA ELIMINATA CON SUCCESSO";
    }
}
