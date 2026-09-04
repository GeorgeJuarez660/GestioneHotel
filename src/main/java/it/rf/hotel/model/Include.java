package it.rf.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "include")
public class Include {

    public Include() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long include_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prenotazione_id")
    private Prenotazione prenotazione;

    @ManyToOne(optional = false)
    @JoinColumn(name = "guida_id")
    private Guida guida;

    @Column(length = 1000)
    private String note;

    @Column(length = 1000)
    private Integer numPersone;

    public Long getId() {
        return include_id;
    }

    public Guida getGuida() {
        return guida;
    }

    public void setGuida(Guida guida) {
        this.guida = guida;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getNumPersone() {
        return numPersone;
    }

    public void setNumPersone(Integer numPersone) {
        this.numPersone = numPersone;
    }
}
