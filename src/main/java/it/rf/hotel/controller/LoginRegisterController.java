package it.rf.hotel.controller;

import it.rf.hotel.config.JWTConfig;
import it.rf.hotel.dto.ClienteRequest;
import it.rf.hotel.dto.DipendenteRequest;
import it.rf.hotel.exception.CFDuplicatoException;
import it.rf.hotel.exception.NotClienteFoundExpcetion;
import it.rf.hotel.exception.NotDipendenteFoundException;
import it.rf.hotel.exception.UnderageUtenteException;
import it.rf.hotel.model.Cliente;
import it.rf.hotel.model.Dipendente;
import it.rf.hotel.model.OperatoreEsterno;
import it.rf.hotel.service.LoginRegisterService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hotel")
public class LoginRegisterController {

    @Autowired
    private LoginRegisterService loginRegisterService;

    @Autowired 
    private JWTConfig jwtService;

    @PostMapping("/register/cliente")
    public ResponseEntity<String> registerCliente(@Valid @RequestBody ClienteRequest dto) {
        try {
            String esito = loginRegisterService.registraCliente(dto);

            return ResponseEntity.ok(esito);
        } catch (CFDuplicatoException exception) {
            System.out.println("Prova a inserire un altro CF");
			
			return ResponseEntity.status(HttpStatusCode.valueOf(409)).body("ERRORE DUPLICATO CF");
        } catch (UnderageUtenteException exception) {
            System.out.println("L'utente deve essere maggiorenne!");
			
			return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("ERRORE ETA' MINIMA");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE REGISTRAZIONE");
        }
    }

    @PostMapping("/register/dipendente")
    public ResponseEntity<String> registerDipendente(@Valid @RequestBody DipendenteRequest dto) {
        try {
            String esito = loginRegisterService.registraDipendente(dto);

            return ResponseEntity.ok(esito);
        } catch (CFDuplicatoException exception) {
            System.out.println("Prova a inserire un altro CF");
			
			return ResponseEntity.status(HttpStatusCode.valueOf(409)).body("ERRORE DUPLICATO CF");
        } catch (UnderageUtenteException exception) {
            System.out.println("L'utente deve essere maggiorenne!");
			
			return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("ERRORE ETA' MINIMA");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE REGISTRAZIONE");
        }
    }

    @GetMapping("/login/cliente")
    public ResponseEntity<?> loginCliente(@RequestParam String username, @RequestParam String password) {
        try {
            Cliente cliente = loginRegisterService.loginCliente(username, password); 
            
            String token = jwtService.generateTokenFromCliente(cliente);
            
            return ResponseEntity.ok(token);    

        } catch (NotClienteFoundExpcetion exception) {
            System.out.println("Prova a inserire un altro username e password");
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NON E' STATO TROVATO IL CLIENTE ");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERRORE");
        }
    }

    @GetMapping("/login/dipendente")
    public ResponseEntity<?> loginDipendente(@RequestParam String username, @RequestParam String password, @RequestParam String codDipendente) {
        try {
            String token = null;
            if(codDipendente != null){
                if(codDipendente.startsWith("EXT")){
                    OperatoreEsterno operatoreEsterno = loginRegisterService.loginDipendenteEsterno(username, password, codDipendente);
                    token = jwtService.generateTokenFromDipendente(operatoreEsterno);
                }
                else{
                    Dipendente dipendente = loginRegisterService.loginDipendente(username, password, codDipendente);
                    token = jwtService.generateTokenFromDipendente(dipendente);
                }
            }

            return ResponseEntity.ok(token); 

        } catch (NotDipendenteFoundException exception) {
            System.out.println("Prova a inserire un altro username, password e codice dipendente");
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NON E' STATO TROVATO IL DIPENDENTE ");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERRORE");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7); // toglie "Bearer "
        jwtService.invalidateToken(token);

        return ResponseEntity.ok("Logout effettuato con successo");
    }
    
}
