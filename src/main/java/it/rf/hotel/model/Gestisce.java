package it.rf.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "gestisce")
public class Gestisce {

    public Gestisce() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long gestisce_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prenotazione_id")
    private Prenotazione prenotazione;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stanza_id")
    private Stanza stanza;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pacchetto_id")
    private Pacchetto pacchetto;

    @Column(nullable = true)
    private LocalDate dataCheckIn;

    @Column(nullable = true)
    private LocalDate dataCheckOut;

    @Column(length = 1000)
    private Integer numeroPersone;

    public Long getId() {
        return gestisce_id;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public Stanza getStanza() {
        return stanza;
    }

    public void setStanza(Stanza stanza) {
        this.stanza = stanza;
    }

    public LocalDate getDataCheckIn() {
        return dataCheckIn;
    }

    public void setDataCheckIn(LocalDate dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public LocalDate getDataCheckOut() {
        return dataCheckOut;
    }

    public void setDataCheckOut(LocalDate dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    public Pacchetto getPacchetto() {
        return pacchetto;
    }

    public void setPacchetto(Pacchetto pacchetto) {
        this.pacchetto = pacchetto;
    }

    public Integer getNumeroPersone() {
        return numeroPersone;
    }

    public void setNumeroPersone(Integer numeroPersone) {
        this.numeroPersone = numeroPersone;
    }
}
