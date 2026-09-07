package it.rf.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.jsonwebtoken.Claims;
import it.rf.hotel.config.JWTConfig;
import it.rf.hotel.dto.BevandaResponse;
import it.rf.hotel.dto.FeedbackDto;
import it.rf.hotel.dto.GuidaDto;
import it.rf.hotel.dto.NavettaDto;
import it.rf.hotel.dto.PacchettoDto;
import it.rf.hotel.dto.PiscinaDto; 
import it.rf.hotel.dto.PrenotazioneReqCheck;
import it.rf.hotel.dto.PrenotazioneResponse;
import it.rf.hotel.dto.StanzaDto;
import it.rf.hotel.dto.TaxiRequest;
import it.rf.hotel.exception.BevandaReferencedException;
import it.rf.hotel.exception.CodiceDuplicatoException;
import it.rf.hotel.exception.GuidaReferencedException;
import it.rf.hotel.exception.NavettaReferencedException;
import it.rf.hotel.exception.NotClienteFoundExpcetion;
import it.rf.hotel.exception.PacchettoReferencedException;
import it.rf.hotel.exception.PiscinaReferencedException;
import it.rf.hotel.exception.StanzaReferencedException;
import it.rf.hotel.model.Dipendente;
import it.rf.hotel.service.BevandaService;
import it.rf.hotel.service.DipendenteService;
import it.rf.hotel.service.FeedbackService;
import it.rf.hotel.service.NavettaService;
import it.rf.hotel.service.GuidaService;
import it.rf.hotel.service.PiscinaService;
import it.rf.hotel.service.PacchettoService;
import it.rf.hotel.service.PrenotazioneService;
import it.rf.hotel.service.StanzaService;
import it.rf.hotel.service.TaxiService;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("hotel/homepage/dipendente")
public class DipendenteSideController {

    @Autowired 
    private JWTConfig jwtConfig;

    @Autowired 
    private DipendenteService dipendenteService;

    @Autowired
    private StanzaService stanzaService;

    @Autowired
    private NavettaService navettaService;

    @Autowired
    private GuidaService guidaService;

    @Autowired
    private PiscinaService piscinaService;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private PacchettoService pacchettoService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private BevandaService bevandaService;

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
                Long dipendenteId = claims.get("dipendenteId", Long.class);
                
                Dipendente dipendente = dipendenteService.trovaDipendente(dipendenteId);

                return ResponseEntity.ok(dipendente);
            }

        }
    }

    @PostMapping("/addStanza")
    public ResponseEntity<String> addStanza(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody StanzaDto dto) {
        try {
            String esito = stanzaService.creaStanza(dto);

            return ResponseEntity.ok(esito);
        } 
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE STANZA");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE INSERIMENTO STANZA");
        }
    }

    @GetMapping("/goToUpdateStanza/{codiceStanza}")
    public ResponseEntity<StanzaDto> goToUpdateStanza(@PathVariable String codiceStanza) {
        StanzaDto stanza = stanzaService.trovaStanza(codiceStanza);

        return ResponseEntity.ok(stanza);
    }

    @PutMapping("/modifyStanza")
    public ResponseEntity<String> updateStanza(@Valid @RequestBody StanzaDto dto, @RequestParam String codiceStanza) {
        try {
            String esito = stanzaService.aggiornaStanza(codiceStanza, dto);

            return ResponseEntity.ok(esito);
        } 
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE STANZA");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE MODIFICA STANZA");
        }
    }

    @GetMapping("/removeStanza")
    public ResponseEntity<String> removeStanza(@RequestParam String codice) {
        try {
            String esito = stanzaService.eliminaStanza(codice);

            return ResponseEntity.ok(esito);
        } 
        catch (StanzaReferencedException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("STANZA PRENOTATA");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE RIMOZIONE STANZA");
        }
    }

    @PostMapping("/addNavetta")
    public ResponseEntity<String> addNavetta(@Valid @RequestBody NavettaDto dto) {
        try {
            String esito = navettaService.creaNavetta(dto);

            return ResponseEntity.ok(esito);
        } 
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE SERVIZIO NAVETTA");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE INSERIMENTO NAVETTA");
        }
    }

    @GetMapping("/goToUpdateNavetta/{codiceNavetta}")
    public ResponseEntity<NavettaDto> goToUpdateNavetta(@PathVariable String codiceNavetta) {
        NavettaDto navetta = navettaService.trovaNavetta(codiceNavetta);

        return ResponseEntity.ok(navetta);
    }

    @PutMapping("/modifyNavetta")
    public ResponseEntity<String> updateNavetta(@Valid @RequestBody NavettaDto dto, @RequestParam String codiceNavetta) {
        try {
            String esito = navettaService.aggiornaNavetta(codiceNavetta, dto);

            return ResponseEntity.ok(esito);
        } 
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE SERVIZIO NAVETTA");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE MODIFICA NAVETTA");
        }
    }

    @GetMapping("/removeNavetta")
    public ResponseEntity<String> removeNavetta(@RequestParam String codice) {
        try {
            String esito = navettaService.eliminaNavetta(codice);

            return ResponseEntity.ok(esito);
        } 
        catch (NavettaReferencedException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("NAVETTA IN SERVIZIO AD UNA O PIU' PRENOTAZIONI");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE RIMOZIONE NAVETTA");
        }
    }

    @GetMapping("/readStanze")
    public ResponseEntity<List<StanzaDto>> readStanze() {
        List<StanzaDto> stanze = stanzaService.elencoStanze();

        return ResponseEntity.ok(stanze);
    }

    @GetMapping("/readNavette")
    public ResponseEntity<List<NavettaDto>> readNavette() {
        List<NavettaDto> navette = navettaService.elencoNavette();

        return ResponseEntity.ok(navette);
    }

    @PostMapping("/addPacchetto")
    public ResponseEntity<String> addPacchetto(@Valid @RequestBody PacchettoDto dto) {
        try {
            String esito = pacchettoService.creaPacchetto(dto);

            return ResponseEntity.ok(esito);
        }
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO TIPO PENSIONE PACCHETTO");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE INSERIMENTO PACCHETTO");
        }
    }

    @GetMapping("/goToUpdatePacchetto/{tipoPacchetto}")
    public ResponseEntity<PacchettoDto> goToUpdatePacchetto(@PathVariable String tipoPacchetto) {
        PacchettoDto pacchetto = pacchettoService.trovaPacchetto(tipoPacchetto);

        return ResponseEntity.ok(pacchetto);
    }

    @PutMapping("/modifyPacchetto")
    public ResponseEntity<String> updatePacchetto(@Valid @RequestBody PacchettoDto dto, @RequestParam String tipoPacchetto) {
        try {
            String esito = pacchettoService.aggiornaPacchetto(tipoPacchetto, dto);

            return ResponseEntity.ok(esito);
        } 
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE SERVIZIO PACCHETTO");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE MODIFICA PACCHETTO");
        }
    }

    @GetMapping("/removePacchetto")
    public ResponseEntity<String> removePacchetto(@RequestParam String tipoPensione) {
        try {
            String esito = pacchettoService.eliminaPacchetto(tipoPensione);

            return ResponseEntity.ok(esito);
        } 
        catch (PacchettoReferencedException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("PACCHETTO IN USO AD UNA O PIU' PRENOTAZIONI");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE RIMOZIONE PACCHETTO");
        }
    }

    @GetMapping("/readPacchetti")
    public ResponseEntity<List<PacchettoDto>> readPacchetti() {
        List<PacchettoDto> pacchetti = pacchettoService.elencoPacchetti();

        return ResponseEntity.ok(pacchetti);
    }

    @GetMapping("/readPrenotazioni")
    public ResponseEntity<List<PrenotazioneResponse>> readPrenotazioni() {
        List<PrenotazioneResponse> prenotazioni = prenotazioneService.elencoPrenotazioni();

        return ResponseEntity.ok(prenotazioni);
    }

    @GetMapping("/goToUpdatePrenotazione/{codicePrenotazione}")
    public ResponseEntity<PrenotazioneReqCheck> goToUpdatePrenotazione(@PathVariable String codicePrenotazione) {
        PrenotazioneReqCheck prenotazioneDaConf = prenotazioneService.trovaPrenotazionePerConfermare(codicePrenotazione);

        return ResponseEntity.ok(prenotazioneDaConf);
    }

    @PutMapping("/modifyPrenotazione")
    public ResponseEntity<String> updatePrenotazione(@Valid @RequestBody PrenotazioneReqCheck dto, @RequestParam String codicePrenotazione) {
        try {
            String esito = prenotazioneService.aggiornaPrenotazione(codicePrenotazione, dto);

            return ResponseEntity.ok(esito);
        } 
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE PRENOTAZIONE");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE MODIFICA PRENOTAZIONE");
        }
    }

    @GetMapping("/removePrenotazione")
    public ResponseEntity<String> removePrenotazione(@RequestParam String codice) {
        try {
            String esito = prenotazioneService.eliminaPrenotazione(codice);

            return ResponseEntity.ok(esito);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE RIMOZIONE PRENOTAZIONE");
        }
    }


    @GetMapping("/readFeedback")
    public ResponseEntity<List<FeedbackDto>> readFeedback() {
        List<FeedbackDto> feedbacks = feedbackService.elencoFeedback();

        return ResponseEntity.ok(feedbacks);
    }


    @PostMapping("/addGuida")
    public ResponseEntity<String> addGuida(@Valid @RequestBody GuidaDto dto) {
        try {
            String esito = guidaService.creaGuida(dto);

            return ResponseEntity.ok(esito);
        } 
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE SERVIZIO GUIDA");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE INSERIMENTO GUIDA");
        }
    }

    @GetMapping("/goToUpdateGuida/{codiceGuida}")
    public ResponseEntity<GuidaDto> goToUpdateGuida(@PathVariable String codiceGuida) {
        GuidaDto guida = guidaService.trovaGuida(codiceGuida);

        return ResponseEntity.ok(guida);
    }

    @PutMapping("/modifyGuida")
    public ResponseEntity<String> updateGuida(@Valid @RequestBody GuidaDto dto, @RequestParam String codiceGuida) {
        try {
            String esito = guidaService.aggiornaGuida(codiceGuida, dto);

            return ResponseEntity.ok(esito);
        } 
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE SERVIZIO GUIDA");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE MODIFICA GUIDA");
        }
    }

    @GetMapping("/removeGuida")
    public ResponseEntity<String> removeGuida(@RequestParam String codice) {
        try {
            String esito = guidaService.eliminaGuida(codice);

            return ResponseEntity.ok(esito);
        } 
        catch (GuidaReferencedException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("GUIDA IN SERVIZIO AD UNA O PIU' PRENOTAZIONI");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE RIMOZIONE GUIDA");
        }
    }

    @GetMapping("/readGuide")
    public ResponseEntity<List<GuidaDto>> readGuide() {
        List<GuidaDto> guide = guidaService.elencoGuide();

        return ResponseEntity.ok(guide);
    }

    @PostMapping("/addPiscina")
    public ResponseEntity<String> addPiscina(@Valid @RequestBody PiscinaDto dto) {
        try {
            String esito = piscinaService.creaPiscina(dto);

            return ResponseEntity.ok(esito);
        } 
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE ACCESSO PISCINA");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE INSERIMENTO PISCINA");
        }
    }

    @GetMapping("/goToUpdatePiscina/{codicePiscina}")
    public ResponseEntity<PiscinaDto> goToUpdatePiscina(@PathVariable String codicePiscina) {
        PiscinaDto piscina = piscinaService.trovaPiscina(codicePiscina);

        return ResponseEntity.ok(piscina);
    }

    @PutMapping("/modifyPiscina")
    public ResponseEntity<String> updatePiscina(@Valid @RequestBody PiscinaDto dto, @RequestParam String codicePiscina) {
        try {
            String esito = piscinaService.aggiornaPiscina(codicePiscina, dto);

            return ResponseEntity.ok(esito);
        } 
        catch (CodiceDuplicatoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE DUPLICATO CODICE ACCESSO PISCINA");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE MODIFICA PISCINA");
        }
    }

    @GetMapping("/removePiscina")
    public ResponseEntity<String> removePiscina(@RequestParam String codice) {
        try {
            String esito = piscinaService.eliminaPiscina(codice);

            return ResponseEntity.ok(esito);
        } 
        catch (PiscinaReferencedException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("PISCINA IN SERVIZIO AD UNA O PIU' PRENOTAZIONI");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE RIMOZIONE PISCINA");
        }
    }

    @GetMapping("/readPiscine")
    public ResponseEntity<List<PiscinaDto>> readPiscine() {
        List<PiscinaDto> piscine = piscinaService.elencoPiscine();

        return ResponseEntity.ok(piscine);
    }

    @PostMapping("/addBevanda")
    public ResponseEntity<String> addBevanda(@Valid @RequestBody BevandaResponse dto) {
        try {
            String esito = bevandaService.creaBevanda(dto);

            return ResponseEntity.ok(esito);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE INSERIMENTO GUIDA");
        }
    }

    @GetMapping("/goToUpdateBevanda/{codiceBevanda}")
    public ResponseEntity<BevandaResponse> goToUpdateBevanda(@PathVariable String codiceBevanda) {
        BevandaResponse bevanda = bevandaService.trovaBevanda(codiceBevanda);

        return ResponseEntity.ok(bevanda);
    }

    @PutMapping("/modifyBevanda")
    public ResponseEntity<String> updateBevanda(@Valid @RequestBody BevandaResponse dto, @RequestParam String codiceBevanda) {
        try {
            String esito = bevandaService.aggiornaBevanda(codiceBevanda, dto);

            return ResponseEntity.ok(esito);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE MODIFICA BEVANDA");
        }
    }

    @GetMapping("/removeBevanda")
    public ResponseEntity<String> removeBevanda(@RequestParam String codice) {
        try {
            String esito = bevandaService.eliminaBevanda(codice);

            return ResponseEntity.ok(esito);
        } 
        catch (BevandaReferencedException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("BEVANDA IN CONSUMAZIONE AD UNA O PIU' PRENOTAZIONI");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE RIMOZIONE BEVANDA");
        }
    }

    @GetMapping("/readBevande")
    public ResponseEntity<List<BevandaResponse>> readBevande() {
        List<BevandaResponse> bevande = bevandaService.elencoBevande();

        return ResponseEntity.ok(bevande);
    }

    @PostMapping("/addTaxi")
    public ResponseEntity<String> addTaxi(@Valid @RequestBody TaxiRequest dto) {
        try {
            String esito = taxiService.creaTaxi(dto);

            return ResponseEntity.ok(esito);
        }
        catch (NotClienteFoundExpcetion exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CLIENTE NON TROVATO");
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE INSERIMENTO TAXI");
        }
    }

    @GetMapping("/goToUpdateTaxi/{idTaxi}")
    public ResponseEntity<TaxiRequest> goToUpdateTaxi(@PathVariable Long idTaxi) {
        TaxiRequest taxi = taxiService.trovaTaxi(idTaxi);

        return ResponseEntity.ok(taxi);
    }

    @PutMapping("/modifyTaxi")
    public ResponseEntity<String> updateTaxi(@Valid @RequestBody TaxiRequest dto, @RequestParam Long idTaxi) {
        try {
            String esito = taxiService.aggiornaTaxi(idTaxi, dto);

            return ResponseEntity.ok(esito);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE MODIFICA TAXI");
        }
    }

    @GetMapping("/removeTaxi")
    public ResponseEntity<String> removeTaxi(@RequestParam Long id) {
        try {
            String esito = taxiService.eliminaTaxi(id);

            return ResponseEntity.ok(esito);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERRORE RIMOZIONE TAXI");
        }
    }

    @GetMapping("/readTaxi")
    public ResponseEntity<List<TaxiRequest>> readTaxi() {
        List<TaxiRequest> corse = taxiService.elencoTaxi();

        return ResponseEntity.ok(corse);
    }

}
