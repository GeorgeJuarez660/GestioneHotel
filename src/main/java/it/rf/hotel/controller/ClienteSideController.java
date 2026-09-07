package it.rf.hotel.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import it.rf.hotel.config.JWTConfig;
import it.rf.hotel.dto.FeedbackDto;
import it.rf.hotel.dto.PrenotazioneResponse;
import it.rf.hotel.dto.TaxiRequest;
import it.rf.hotel.exception.CodiceDuplicatoException;
import it.rf.hotel.exception.NotClienteFoundExpcetion;
import it.rf.hotel.model.Cliente;
import it.rf.hotel.service.ClienteService;
import it.rf.hotel.service.FeedbackService;
import it.rf.hotel.service.PrenotazioneService;
import it.rf.hotel.service.TaxiService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("hotel/homepage/cliente")
public class ClienteSideController {

    @Autowired 
    private JWTConfig jwtConfig;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private TaxiService taxiService;

    @GetMapping("/check")
    public ResponseEntity<?> check(@RequestHeader("Authorization") String authHeader) {
        
        if(authHeader != null && !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
        }
        else{
            String token = authHeader.substring(7);

            if (!jwtConfig.isValid(token)) {
                return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
            }
            else{
                Claims claims = jwtConfig.parseClaims(token);
                Long clienteId = claims.get("clienteId", Long.class);
                
                Cliente cliente = clienteService.trovaCliente(clienteId);

                return ResponseEntity.ok(cliente);
            }

        }
    }

    @PostMapping("/addPrenotazione")
    public ResponseEntity<String> addPrenotazione(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody PrenotazioneResponse dto) {
        try {

            if(authHeader != null && !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
            }
            else{
                String token = authHeader.substring(7);

                if (!jwtConfig.isValid(token)) {
                    return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
                }
                else{
                    Claims claims = jwtConfig.parseClaims(token);
                    String clienteCF = claims.get("clienteCF", String.class);
                    
                    dto.setCfCliente(clienteCF);

                    String esito = prenotazioneService.creaPrenotazione(dto);

                    return ResponseEntity.ok(esito);
                }

            }
        }
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE PRENOTAZIONE");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE INSERIMENTO PRENOTAZIONE");
        }
    }

    @PostMapping("/addFeedback")
    public ResponseEntity<String> addFeedback(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody FeedbackDto dto) {
        try {
            if(authHeader != null && !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
            }
            else{
                String token = authHeader.substring(7);

                if (!jwtConfig.isValid(token)) {
                    return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
                }
                else{
                    Claims claims = jwtConfig.parseClaims(token);
                    String clienteCF = claims.get("clienteCF", String.class);
                    
                    dto.setCfCliente(clienteCF);

                    String esito = feedbackService.creaFeedback(dto);

                    return ResponseEntity.ok(esito);
                }

            }

        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE INSERIMENTO FEEDBACK");
        }
    }

    @GetMapping("/readFeedbackByClienteCF")
    public ResponseEntity<?> readFeedback(@RequestHeader("Authorization") String authHeader) {
        if(authHeader != null && !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
        }
        else{
            String token = authHeader.substring(7);

            if (!jwtConfig.isValid(token)) {
                return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
            }
            else{
                Claims claims = jwtConfig.parseClaims(token);
                String clienteCF = claims.get("clienteCF", String.class);
                
                List<FeedbackDto> feedbacks = feedbackService.elencoFeedbackInBaseAlCliente(clienteCF);

                return ResponseEntity.ok(feedbacks);
            }

        }
    }

    /*
     * Il possessore non si accetta dal corpo della richiesta: viene sempre
     * riscritto con il cliente collegato, altrimenti chiunque potrebbe
     * addebitare una corsa sul soggiorno di un altro ospite.
     */
    @PostMapping("/addTaxi")
    public ResponseEntity<String> addTaxi(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody TaxiRequest dto) {
        try {
            if(authHeader != null && !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
            }
            else{
                String token = authHeader.substring(7);

                if (!jwtConfig.isValid(token)) {
                    return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
                }
                else{
                    Claims claims = jwtConfig.parseClaims(token);
                    Long clienteId = claims.get("clienteId", Long.class);

                    Cliente cliente = clienteService.trovaCliente(clienteId);

                    dto.setNomePossessore(cliente.getNome());
                    dto.setCognomePossessore(cliente.getCognome());

                    String esito = taxiService.creaTaxi(dto);

                    return ResponseEntity.ok(esito);
                }

            }

        }
        catch (NotClienteFoundExpcetion exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("NESSUNA PRENOTAZIONE TROVATA PER IL CLIENTE");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE INSERIMENTO TAXI");
        }
    }

    @GetMapping("/readTaxiByCliente")
    public ResponseEntity<?> readTaxi(@RequestHeader("Authorization") String authHeader) {
        if(authHeader != null && !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
        }
        else{
            String token = authHeader.substring(7);

            if (!jwtConfig.isValid(token)) {
                return ResponseEntity.status(HttpStatusCode.valueOf(403)).body("ACCESSO NEGATO");
            }
            else{
                Claims claims = jwtConfig.parseClaims(token);
                Long clienteId = claims.get("clienteId", Long.class);

                Cliente cliente = clienteService.trovaCliente(clienteId);

                List<TaxiRequest> corse = taxiService.elencoTaxiPrenotazione(cliente.getNome(), cliente.getCognome());

                return ResponseEntity.ok(corse);
            }

        }
    }

}
