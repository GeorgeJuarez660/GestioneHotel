package it.rf.hotel.service;

import it.rf.hotel.model.Cliente;
import it.rf.hotel.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    public List<Cliente> trovaTuttiClienti() { return clienteRepository.findAll(); }


    public Cliente trovaCliente(Long id) { 

        Optional<Cliente> clienteOpt = clienteRepository.findById(id); 
        Cliente cliente;

        if(clienteOpt.isPresent()){
            cliente = clienteOpt.get();
        }
        else{
            cliente = null;
        }
        
        return cliente;
    }
    public Cliente aggiornaCliente(Long id, Cliente cliente) {
        if (!clienteRepository.existsById(id)) throw new IllegalArgumentException("Cliente non trovato");
        return clienteRepository.save(cliente);
    }
    public void eliminaCliente(Long id) { clienteRepository.deleteById(id); }
}
