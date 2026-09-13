package it.rf.hotel.service;

import it.rf.hotel.dto.ClienteRequest;
import it.rf.hotel.dto.DipendenteRequest;
import it.rf.hotel.exception.CFDuplicatoException;
import it.rf.hotel.exception.NotClienteFoundExpcetion;
import it.rf.hotel.exception.NotDipendenteFoundException;
import it.rf.hotel.exception.UnderageUtenteException;
import it.rf.hotel.model.CategoriaDipendente;
import it.rf.hotel.model.Cliente;
import it.rf.hotel.model.Dipendente;
import it.rf.hotel.model.OperatoreEsterno;
import it.rf.hotel.repository.CategoriaDipendenteRepository;
import it.rf.hotel.repository.ClienteRepository;
import it.rf.hotel.repository.DipendenteRepository;
import it.rf.hotel.repository.OperatoreEsternoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class LoginRegisterService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private OperatoreEsternoRepository operatoreEsternoRepository;

    @Autowired
    private CategoriaDipendenteRepository categoriaDipendenteRepository;

    public String registraCliente(ClienteRequest dto) throws CFDuplicatoException, UnderageUtenteException {
        Cliente cliente;
        String esito;

        if (clienteRepository.countByCodiceFiscale(dto.getCodiceFiscale()) > 0L) {
            throw new CFDuplicatoException(dto.getCodiceFiscale());
        }

        int anni = Period.between(dto.getDataNascita(), LocalDate.now()).getYears();
        
        // È maggiorenne se è nato prima o esattamente 18 anni fa
        if (anni < 18) {
            throw new UnderageUtenteException();
        }
        else{
            cliente = new Cliente();
            cliente.setNome(dto.getNome());
            cliente.setCognome(dto.getCognome());
            cliente.setCodiceFiscale(dto.getCodiceFiscale());
            cliente.setDataNascita(dto.getDataNascita());
            cliente.setLingua(dto.getLingua());
            cliente.setUsername(dto.getUsername());
            cliente.setPassword(dto.getPassword());
            clienteRepository.save(cliente);

            esito = "REGISTRAZIONE AVVENUTA CON SUCCESSO";
        }
        
        return esito;
    }

    public String registraDipendente(DipendenteRequest dto) throws CFDuplicatoException, UnderageUtenteException {
        Dipendente dipendente;
        OperatoreEsterno operatoreEsterno;
        String esito;

        if (dipendenteRepository.countByCodiceFiscale(dto.getCodiceFiscale()) > 0L) {
            throw new CFDuplicatoException(dto.getCodiceFiscale());
        }

        int anni = Period.between(dto.getDataNascita(), LocalDate.now()).getYears();
        
        // È maggiorenne se è nato prima o esattamente 18 anni fa
        if (anni < 18) {
            throw new UnderageUtenteException();
        }
        else{
            if(dto.getCodDipendente() != null){
                if(dto.getCodDipendente().startsWith("EXT")){
                    operatoreEsterno = new OperatoreEsterno();
                    operatoreEsterno.setNome(dto.getNome());
                    operatoreEsterno.setCognome(dto.getCognome());
                    operatoreEsterno.setCodiceFiscale(dto.getCodiceFiscale());
                    operatoreEsterno.setDataNascita(dto.getDataNascita());
                    operatoreEsterno.setUsername(dto.getUsername());
                    operatoreEsterno.setPassword(dto.getPassword());
                    operatoreEsterno.setLingua(dto.getLingua());
                    operatoreEsterno.setCodDipendenteEsterno(dto.getCodDipendente());

                    operatoreEsternoRepository.save(operatoreEsterno);
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
                    dipendente.setCodDipendente(dto.getCodDipendente());

                    CategoriaDipendente categoria = categoriaDipendenteRepository.findByTipo(dto.getCategoria()).orElse(null);
                    dipendente.setCategoria(categoria);

                    dipendenteRepository.save(dipendente);
                }
            }
            

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

    public Dipendente loginDipendente(String username, String password, String codDipendente) throws NotDipendenteFoundException {
        Dipendente dipendente = null;
        Optional<Dipendente> dipendenteOptional = dipendenteRepository.findByUsernameAndPasswordAndCodDipendente(username, password, codDipendente);

        if(dipendenteOptional.isPresent()){
            dipendente = dipendenteOptional.get();

        }
        else{
            throw new NotDipendenteFoundException(username, password, codDipendente);
        }
        return dipendente;
    }

    public OperatoreEsterno loginDipendenteEsterno(String username, String password, String codDipendente) throws NotDipendenteFoundException {
        OperatoreEsterno operatoreEsterno = null;
        Optional<OperatoreEsterno> operatoreEsternoOptional = operatoreEsternoRepository.findByUsernameAndPasswordAndCodDipendenteEsterno(username, password, codDipendente);

        if(operatoreEsternoOptional.isPresent()){
            operatoreEsterno = operatoreEsternoOptional.get();

        }
        else{
            throw new NotDipendenteFoundException(username, password, codDipendente);
        }
        return operatoreEsterno;
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
