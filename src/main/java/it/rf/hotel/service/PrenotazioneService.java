package it.rf.hotel.service;

import it.rf.hotel.dto.PrenotazioneReqCheck;
import it.rf.hotel.dto.PrenotazioneResponse;
import it.rf.hotel.exception.CodiceDuplicatoException;
import it.rf.hotel.exception.NavettaNoSeatsException;
import it.rf.hotel.exception.StanzaBookedException;
import it.rf.hotel.exception.StanzaTooPeopleException;
import it.rf.hotel.model.Accede;
import it.rf.hotel.model.Cliente;
import it.rf.hotel.model.Comprende;
import it.rf.hotel.model.Dipendente;
import it.rf.hotel.model.Gestisce;
import it.rf.hotel.model.Include;
import it.rf.hotel.model.MetodoPagamento;
import it.rf.hotel.model.Navetta;
import it.rf.hotel.model.Guida;
import it.rf.hotel.model.Piscina;
import it.rf.hotel.model.Pacchetto;
import it.rf.hotel.model.Pagamento;
import it.rf.hotel.model.Prenotazione;
import it.rf.hotel.model.Stanza;
import it.rf.hotel.model.StatoPagamento;
import it.rf.hotel.model.StatoPrenotazione;
import it.rf.hotel.repository.AccedeRepository;
import it.rf.hotel.repository.ClienteRepository;
import it.rf.hotel.repository.ComprendeRepository;
import it.rf.hotel.repository.DipendenteRepository;
import it.rf.hotel.repository.GestisceRepository;
import it.rf.hotel.repository.GuidaRepository;
import it.rf.hotel.repository.IncludeRepository;
import it.rf.hotel.repository.MetodoPagamentoRepository;
import it.rf.hotel.repository.NavettaRepository;
import it.rf.hotel.repository.PacchettoRepository;
import it.rf.hotel.repository.PagamentoRepository;
import it.rf.hotel.repository.PiscinaRepository;
import it.rf.hotel.repository.PrenotazioneRepository;
import it.rf.hotel.repository.StanzaRepository;
import it.rf.hotel.repository.StatoPagamentoRepository;
import it.rf.hotel.repository.StatoPrenotazioneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Arrays;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private StatoPrenotazioneRepository statoPrenotazioneRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private StanzaRepository stanzaRepository;

    @Autowired
    private PacchettoRepository pacchettoRepository;

    @Autowired
    private NavettaRepository navettaRepository;

    @Autowired
    private GuidaRepository guidaRepository;

    @Autowired
    private PiscinaRepository piscinaRepository;

    @Autowired
    private GestisceRepository gestisceRepository;

    @Autowired
    private ComprendeRepository comprendeRepository;

    @Autowired
    private IncludeRepository includeRepository;

    @Autowired
    private AccedeRepository accedeRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private MetodoPagamentoRepository metodoPagamentoRepository;

    @Autowired
    private StatoPagamentoRepository statoPagamentoRepository;


    public String creaPrenotazione(PrenotazioneResponse dto) throws CodiceDuplicatoException, StanzaBookedException, StanzaTooPeopleException, NavettaNoSeatsException {

        if (prenotazioneRepository.countByCodice(dto.getCodice()) > 0L) {
            throw new CodiceDuplicatoException(dto.getCodice());
        }
        else if(gestisceRepository.countByPrenotazioneDataInizioAndPrenotazioneDataFineAndStanzaCodice(dto.getDataInizio(), dto.getDataFine(), dto.getCodiceStanza()) > 0){
            throw new StanzaBookedException(dto.getCodiceStanza(), dto.getDataInizio(), dto.getDataFine());
        }
        else{
  
            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setCodice(dto.getCodice());
            prenotazione.setDataPrenotazione(dto.getDataPrenotazione());
            prenotazione.setPrezzoTotale(dto.getPrezzoTotale());
            prenotazione.setPrezzoEffettivo(dto.getPrezzoTotale());
            prenotazione.setDataInizio(dto.getDataInizio());
            prenotazione.setDataFine(dto.getDataFine());
            String note = "Stato pagamento: PAGATO | " + dto.getNote();
            prenotazione.setNote(note);

            Cliente cliente = clienteRepository.findByCodiceFiscale(dto.getCfCliente()).orElse(null);
            prenotazione.setCliente(cliente);

            StatoPrenotazione stato = statoPrenotazioneRepository.findByStato("IN_ATTESA").orElse(null);
            prenotazione.setStato(stato);

            Dipendente receptionist = dipendenteRepository.findByCodiceFiscale("VRDLGU80A01H501W").orElse(null);
            prenotazione.setReceptionist(receptionist);

            List<String> codStanze = Arrays.asList(dto.getCodiceStanza().trim().split(",")); //tolgo gli spazi e li spezzetto

            List<Stanza> stanze = stanzaRepository.findByCodiceIn(codStanze).orElse(null);

            int capienzaTot = 0;
            for(Stanza stanza : stanze){
                capienzaTot += stanza.getCapienza();
            }

            if(capienzaTot < dto.getNumPersone()){
                throw new StanzaTooPeopleException(codStanze);
            }

            Gestisce gestisce = null;
            List<Gestisce> gestisceList = new ArrayList<>();
            for(Stanza stanza : stanze){
                gestisce = new Gestisce();
                gestisce.setPrenotazione(prenotazione);
                gestisce.setStanza(stanza);
                gestisce.setNumeroPersone(dto.getNumPersone());

                if(dto.getTipoPacchetto() != null && !dto.getTipoPacchetto().isEmpty()){
                    Pacchetto pacchetto = pacchettoRepository.findByTipoPensione(dto.getTipoPacchetto()).orElse(null);
                    gestisce.setPacchetto(pacchetto);
                }

                gestisceList.add(gestisce);
            }
            

            Comprende comprende = null;
            List<Comprende> comprendeList = new ArrayList<>();
            if(dto.getCodiceNavetta() != null && !dto.getCodiceNavetta().isEmpty()){

                List<String> codNavette = Arrays.asList(dto.getCodiceNavetta().trim().split(",")); //tolgo gli spazi e li spezzetto

                List<Navetta> navette = navettaRepository.findByCodiceIn(codNavette).orElse(null);

                for(Navetta navetta : navette){

                    if(navetta.getNumPostiMax() < dto.getNumPersone() || navetta.getNumPostiDisp() < dto.getNumPersone()){
                        throw new NavettaNoSeatsException(navetta.getCodice());
                    }

                    comprende = new Comprende();
                    comprende.setPrenotazione(prenotazione);
                    comprende.setNavetta(navetta);
                    comprende.setNumPasseggeri(dto.getNumPersone());

                    comprendeList.add(comprende);
                }
                
            }

            Include include = null;
            List<Include> includeList = new ArrayList<>();
            if(dto.getCodiceGuida() != null && !dto.getCodiceGuida().isEmpty()){

                List<String> codGuide = Arrays.asList(dto.getCodiceGuida().trim().split(",")); //tolgo gli spazi e li spezzetto

                List<Guida> guide = guidaRepository.findByCodiceIn(codGuide).orElse(null);

                for(Guida guida : guide){
                    include = new Include();
                    include.setPrenotazione(prenotazione);
                    include.setGuida(guida);
                    include.setNumPersone(dto.getNumPersone());

                    includeList.add(include);
                }
                
            }

            Accede accede = null;
            List<Accede> accedeList = new ArrayList<>();
            if(dto.getCodicePiscina() != null && !dto.getCodicePiscina().isEmpty()){

                List<String> codPiscine = Arrays.asList(dto.getCodicePiscina().trim().split(",")); //tolgo gli spazi e li spezzetto

                List<Piscina> piscine = piscinaRepository.findByCodiceIn(codPiscine).orElse(null);

                for(Piscina piscina : piscine){
                    accede = new Accede();
                    accede.setPrenotazione(prenotazione);
                    accede.setPiscina(piscina);
                    accede.setNumPersone(dto.getNumPersone());
                }

                accedeList.add(accede);
            }

            prenotazioneRepository.save(prenotazione);
            gestisceRepository.saveAll(gestisceList);

            if(dto.getCodiceNavetta() != null && !dto.getCodiceNavetta().isEmpty()){
                comprendeRepository.saveAll(comprendeList);

                for(Comprende compr : comprendeList){

                    Navetta navetta = compr.getNavetta();
                    navetta.setNumPostiDisp(navetta.getNumPostiDisp()-dto.getNumPersone());
                    navettaRepository.save(navetta);
                }
                
            }

            if(dto.getCodiceGuida() != null && !dto.getCodiceGuida().isEmpty()){
                includeRepository.saveAll(includeList);
            }

            if(dto.getCodicePiscina() != null && !dto.getCodicePiscina().isEmpty()){
                accedeRepository.saveAll(accedeList);
            }

            Pagamento pagamento = new Pagamento();
            pagamento.setPrenotazione(prenotazione);
            pagamento.setDataPagamento(LocalDate.now());
            pagamento.setImporto(dto.getPrezzoTotale());

            MetodoPagamento metodo = metodoPagamentoRepository.findByTipo("CARTA").orElse(null);
            pagamento.setMetodo(metodo);

            StatoPagamento statoPag = statoPagamentoRepository.findByStato("PAGATO").orElse(null);
            pagamento.setStato(statoPag);
            
            pagamentoRepository.save(pagamento);
            
        }

        return "PRENOTAZIONE AGGIUNTA CON SUCCESSO";
    }

    public List<PrenotazioneResponse> elencoPrenotazioni() {

        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        List<PrenotazioneResponse> elenco = new ArrayList<>();

        for(Prenotazione prenotazione : prenotazioni){
            PrenotazioneResponse dto = new PrenotazioneResponse();
            dto.setCodice(prenotazione.getCodice());
            dto.setDataPrenotazione(prenotazione.getDataPrenotazione());
            dto.setPrezzoTotale(prenotazione.getPrezzoTotale());
            dto.setPrezzoEffettivo(prenotazione.getPrezzoTotale());
            dto.setDataInizio(prenotazione.getDataInizio());
            dto.setDataFine(prenotazione.getDataFine());
            dto.setStatoPrenotazione(prenotazione.getStato().getStato());
            dto.setNote(prenotazione.getNote());

            if (prenotazione.getCliente() != null) {
                dto.setCfCliente(prenotazione.getCliente().getCodiceFiscale());
                dto.setNomeCliente(prenotazione.getCliente().getNome());
                dto.setCognomeCliente(prenotazione.getCliente().getCognome());
            }

            if (prenotazione.getReceptionist() != null) {
                dto.setCfReceptionist(prenotazione.getReceptionist().getCodiceFiscale());
                dto.setNomeReceptionist(prenotazione.getReceptionist().getNome());
                dto.setCognomeReceptionist(prenotazione.getReceptionist().getCognome());
            }

            elenco.add(dto);
        }

        return elenco;
    }

    public PrenotazioneReqCheck trovaPrenotazionePerConfermare(String codicePrenotazione) {

        Optional<Prenotazione> prenotazioneOpt = prenotazioneRepository.findByCodice(codicePrenotazione);
        Prenotazione prenotazione;
        PrenotazioneReqCheck dtoReq = null;

        if (prenotazioneOpt.isPresent()) {
            prenotazione = prenotazioneOpt.get();

            Optional<Gestisce> gestisceOpt = gestisceRepository.findByPrenotazioneCodice(codicePrenotazione);

            Optional<Comprende> comprendeOpt = comprendeRepository.findByPrenotazioneCodice(codicePrenotazione);

            Optional<Include> includeOpt = includeRepository.findByPrenotazioneCodice(codicePrenotazione);

            Optional<Accede> accedeOpt = accedeRepository.findByPrenotazioneCodice(codicePrenotazione);

            if (gestisceOpt.isPresent()) {
                Gestisce gestisce = gestisceOpt.get();
                Comprende comprende = null;
                if(comprendeOpt.isPresent()){
                    comprende = comprendeOpt.get();
                }

                Include include = null;
                if(includeOpt.isPresent()){
                    include = includeOpt.get();
                }

                Accede accede = null;
                if(accedeOpt.isPresent()){
                    accede = accedeOpt.get();
                }

                dtoReq = new PrenotazioneReqCheck();
                dtoReq.setNote(prenotazione.getNote());
                dtoReq.setPrezzoTotale(prenotazione.getPrezzoEffettivo());
                dtoReq.setNumPersone(gestisce.getNumeroPersone());
                dtoReq.setCodicePrenotazione(gestisce.getPrenotazione().getCodice());
                dtoReq.setDataCheckIn(gestisce.getDataCheckIn());
                dtoReq.setDataCheckOut(gestisce.getDataCheckOut());
                dtoReq.setCfCliente(gestisce.getPrenotazione().getCliente().getCodiceFiscale());
                dtoReq.setStatoPrenotazione(gestisce.getPrenotazione().getStato().getStato());
                dtoReq.setCodiceNavetta(comprende != null && comprende.getNavetta() != null ? comprende.getNavetta().getCodice() : null);
                dtoReq.setCodiceGuida(include != null && include.getGuida() != null ? include.getGuida().getCodice() : null);
                dtoReq.setCodicePiscina(accede != null && accede.getPiscina() != null ? accede.getPiscina().getCodice() : null);

                Optional<Pagamento> pagamentoOpt = pagamentoRepository.findByPrenotazioneCodice(codicePrenotazione);

                Pagamento pagamento = null;
                if(pagamentoOpt.isPresent()){
                    pagamento = pagamentoOpt.get();
                }
                dtoReq.setStatoPagamento(pagamento != null ? pagamento.getStato().getStato() : null);
            }
        }

        return dtoReq;
    }

    public String aggiornaPrenotazione(String codice, PrenotazioneReqCheck dto) throws CodiceDuplicatoException {
        if(codice.equals(dto.getCodicePrenotazione())){
            StatoPrenotazione statoPrenotazione = statoPrenotazioneRepository.findByStato(dto.getStatoPrenotazione()).orElse(null);

            Prenotazione prenotazione = prenotazioneRepository.findByCodice(codice).orElse(null);
            prenotazione.setPrezzoTotale(dto.getPrezzoTotale());
            prenotazione.setNote(dto.getNote());
            prenotazione.setStato(statoPrenotazione);

            prenotazioneRepository.save(prenotazione);
            
            Navetta navetta = navettaRepository.findByCodice(dto.getCodiceNavetta()).orElse(null);
            navetta.setCodice(dto.getCodiceNavetta());

            Gestisce gestisce = gestisceRepository.findByPrenotazioneCodice(codice).orElse(null);
            gestisce.setDataCheckIn(dto.getDataCheckIn());
            gestisce.setDataCheckOut(dto.getDataCheckOut());
            gestisce.setNumeroPersone(dto.getNumPersone());
            
            gestisceRepository.save(gestisce);
            
            Comprende comprende = comprendeRepository.findByPrenotazioneCodice(codice).orElse(null);
            comprende.setPrenotazione(prenotazione);
            comprende.setNavetta(navetta);

            comprendeRepository.save(comprende);

            StatoPagamento statoPagamento = statoPagamentoRepository.findByStato(dto.getStatoPagamento()).orElse(null);

            Pagamento pagamento = pagamentoRepository.findByPrenotazioneCodice(codice).orElse(null);
            pagamento.setStato(statoPagamento);

            pagamentoRepository.save(pagamento);

        }

        return "PRENOTAZIONE MODIFICATA CON SUCCESSO";
    }

    public String eliminaPrenotazione(String codice) { 
        Long countCompr = comprendeRepository.countByPrenotazioneCodice(codice);
        if (countCompr > 0) {
            comprendeRepository.deleteByPrenotazioneCodice(codice);
        }

        Long countGest = gestisceRepository.countByPrenotazioneCodice(codice);
        if (countGest > 0) {
            gestisceRepository.deleteByPrenotazioneCodice(codice);
        }

        Long countIncl = includeRepository.countByPrenotazioneCodice(codice);
        if (countIncl > 0) {
            includeRepository.deleteByPrenotazioneCodice(codice);
        }

        Long countAcc = accedeRepository.countByPrenotazioneCodice(codice);
        if (countAcc > 0) {
            accedeRepository.deleteByPrenotazioneCodice(codice);
        }

        Long countPag = pagamentoRepository.countByPrenotazioneCodice(codice);
        if (countPag > 0) {
            pagamentoRepository.deleteByPrenotazioneCodice(codice);
        }
        prenotazioneRepository.deleteByCodice(codice); 

        return "PRENOTAZIONE RIMOSSA CON SUCCESSO";
    }
}
