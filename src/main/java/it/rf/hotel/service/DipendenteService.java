package it.rf.hotel.service;

import it.rf.hotel.model.Dipendente;
import it.rf.hotel.repository.DipendenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;


    public List<Dipendente> trovaTuttiDipendenti() { return dipendenteRepository.findAll(); }


    public Dipendente trovaDipendente(Long id) { 

        Optional<Dipendente> dipendenteOpt = dipendenteRepository.findById(id); 
        Dipendente dipendente;

        if(dipendenteOpt.isPresent()){
            dipendente = dipendenteOpt.get();
        }
        else{
            dipendente = null;
        }
        
        return dipendente;
    }
    public Dipendente aggiornaDipendente(Long id, Dipendente dipendente) {
        if (!dipendenteRepository.existsById(id)) throw new IllegalArgumentException("Dipendente non trovato");
        return dipendenteRepository.save(dipendente);
    }
    public void eliminaDipendente(Long id) { dipendenteRepository.deleteById(id); }
}
