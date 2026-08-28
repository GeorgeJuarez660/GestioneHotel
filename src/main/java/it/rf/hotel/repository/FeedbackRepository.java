package it.rf.hotel.repository;

import it.rf.hotel.model.Feedback;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    public List<Feedback> findByClienteCodiceFiscale(String codiceFiscale);
}
