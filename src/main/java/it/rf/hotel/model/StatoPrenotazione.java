package it.rf.hotel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stati_prenotazione")
public class StatoPrenotazione {
    public StatoPrenotazione() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stato_prenotazione_id")
    private Long stato_prenotazione_id;

    @Column(nullable = false, unique = true, length = 40)
    private String stato;

    public Long getId() { return stato_prenotazione_id; }
    public String getStato() { return stato; } public void setStato(String stato) { this.stato = stato; }
}
