package it.rf.hotel.service;

import it.rf.hotel.dto.RegisterClienteRequest;
import it.rf.hotel.dto.RegisterDipendenteRequest;
import it.rf.hotel.exception.CFDuplicatoException;
import it.rf.hotel.exception.NotClienteFoundExpcetion;
import it.rf.hotel.exception.NotDipendenteFoundException;
import it.rf.hotel.model.CategoriaDipendente;
import it.rf.hotel.model.Cliente;
import it.rf.hotel.model.Dipendente;
import it.rf.hotel.repository.CategoriaDipendenteRepository;
import it.rf.hotel.repository.ClienteRepository;
import it.rf.hotel.repository.DipendenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginRegisterService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private CategoriaDipendenteRepository categoriaDipendenteRepository;

    public String registraCliente(RegisterClienteRequest dto) throws CFDuplicatoException {
        Cliente cliente;
        String esito;

        if (clienteRepository.countByCodiceFiscale(dto.getCodiceFiscale()) > 0L) {
            throw new CFDuplicatoException(dto.getCodiceFiscale());
        }
        else{
            cliente = new Cliente();
            cliente.setNome(dto.getNome());
            cliente.setCognome(dto.getCognome());
            cliente.setCodiceFiscale(dto.getCodiceFiscale());
            cliente.setDataNascita(dto.getDataNascita());
            cliente.setUsername(dto.getUsername());
            cliente.setPassword(dto.getPassword());
            clienteRepository.save(cliente);

            esito = "REGISTRAZIONE AVVENUTA CON SUCCESSO";
        }
        
        return esito;
    }

    public String registraDipendente(RegisterDipendenteRequest dto) throws CFDuplicatoException {
        Dipendente dipendente;
        String esito;

        if (dipendenteRepository.countByCodiceFiscale(dto.getCodiceFiscale()) > 0L) {
            throw new CFDuplicatoException(dto.getCodiceFiscale());
        }
        else{

            dipendente = new Dipendente();
            dipendente.setNome(dto.getNome());
            dipendente.setCognome(dto.getCognome());
            dipendente.setCodiceFiscale(dto.getCodiceFiscale());
            dipendente.setDataNascita(dto.getDataNascita());
            dipendente.setUsername(dto.getUsername());
            dipendente.setPassword(dto.getPassword());
            dipendente.setLingua(dto.getLingua());
            
            CategoriaDipendente categoria = categoriaDipendenteRepository.findByTipo(dto.getCategoria()).orElse(null);
            dipendente.setCategoria(categoria);

            dipendenteRepository.save(dipendente);
            esito = "REGISTRAZIONE AVVENUTA CON SUCCESSO";
        }

        return esito;
        
    }

    public Cliente loginCliente(String username, String password) throws NotClienteFoundExpcetion{

        Cliente cliente = null;
        Optional<Cliente> clienteOpt = clienteRepository.findByUsernameAndPassword(username, password);

        if(clienteOpt.isPresent()){
            cliente = clienteOpt.get();

        }
        else{
            throw new NotClienteFoundExpcetion(username, password);
        }
        return cliente;
    }

    public Dipendente loginDipendente(String username, String password) throws NotDipendenteFoundException {
        Dipendente dipendente = null;
        Optional<Dipendente> dipendenteOptional = dipendenteRepository.findByUsernameAndPassword(username, password);

        if(dipendenteOptional.isPresent()){
            dipendente = dipendenteOptional.get();

        }
        else{
            throw new NotDipendenteFoundException(username, password);
        }
        return dipendente;
    }

    public List<Cliente> trovaTuttiClienti() { return clienteRepository.findAll(); }
    public Optional<Cliente> trovaCliente(Long id) { return clienteRepository.findById(id); }
    public Cliente aggiornaCliente(Long id, Cliente cliente) {
        if (!clienteRepository.existsById(id)) throw new IllegalArgumentException("Cliente non trovato");
        return clienteRepository.save(cliente);
    }
    public void eliminaCliente(Long id) { clienteRepository.deleteById(id); }
    public List<Dipendente> trovaTuttiDipendenti() { return dipendenteRepository.findAll(); }
    public Optional<Dipendente> trovaDipendente(Long id) { return dipendenteRepository.findById(id); }
    public Dipendente aggiornaDipendente(Long id, Dipendente dipendente) {
        if (!dipendenteRepository.existsById(id)) throw new IllegalArgumentException("Dipendente non trovato");
        return dipendenteRepository.save(dipendente);
    }
    public void eliminaDipendente(Long id) { dipendenteRepository.deleteById(id); }
}
