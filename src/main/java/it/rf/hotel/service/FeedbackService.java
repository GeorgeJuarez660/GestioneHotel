package it.rf.hotel.service;

import it.rf.hotel.dto.FeedbackDto;
import it.rf.hotel.model.Cliente;
import it.rf.hotel.model.Feedback;
import it.rf.hotel.repository.ClienteRepository;
import it.rf.hotel.repository.FeedbackRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    public String creaFeedback(FeedbackDto dto) {

        Feedback feedback = new Feedback();
        feedback.setNote(dto.getNote());

        Cliente cliente = clienteRepository.findByCodiceFiscale(dto.getCfCliente()).orElse(null);
        feedback.setCliente(cliente);

        feedbackRepository.save(feedback);

        return "FEEDBACK AGGIUNTO CON SUCCESSO";
    }

    public List<FeedbackDto> elencoFeedback() {

        List<Feedback> feedbacks = feedbackRepository.findAll();
        List<FeedbackDto> elenco = new ArrayList<>();

        for (Feedback feedback : feedbacks) {
            FeedbackDto dto = new FeedbackDto();
            dto.setNote(feedback.getNote());

            if (feedback.getCliente() != null) {
                dto.setCfCliente(feedback.getCliente().getCodiceFiscale());
                dto.setNomeCliente(feedback.getCliente().getNome());
                dto.setCognomeCliente(feedback.getCliente().getCognome());
            }

            elenco.add(dto);
        }

        return elenco;
    }

    public List<FeedbackDto> elencoFeedbackInBaseAlCliente(String codiceFiscale) {

        List<Feedback> feedbacks = feedbackRepository.findByClienteCodiceFiscale(codiceFiscale);
        List<FeedbackDto> elenco = new ArrayList<>();

        for (Feedback feedback : feedbacks) {
            FeedbackDto dto = new FeedbackDto();
            dto.setNote(feedback.getNote());

            if (feedback.getCliente() != null) {
                dto.setCfCliente(feedback.getCliente().getCodiceFiscale());
                dto.setNomeCliente(feedback.getCliente().getNome());
                dto.setCognomeCliente(feedback.getCliente().getCognome());
            }

            elenco.add(dto);
        }

        return elenco;
    }

    public Feedback trovaFeedback(Long id) {

        Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
        Feedback feedback;

        if (feedbackOpt.isPresent()) {
            feedback = feedbackOpt.get();
        }
        else {
            feedback = null;
        }

        return feedback;
    }

    public Feedback aggiornaFeedback(Long id, Feedback feedback) {
        if (!feedbackRepository.existsById(id)) throw new IllegalArgumentException("Feedback non trovato");
        return feedbackRepository.save(feedback);
    }

    public String eliminaFeedback(Long id) {

        feedbackRepository.deleteById(id);

        return "FEEDBACK RIMOSSO CON SUCCESSO";
    }
}
