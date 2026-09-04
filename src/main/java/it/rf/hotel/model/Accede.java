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
@Table(name = "accede")
public class Accede {

    public Accede() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long accede_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prenotazione_id")
    private Prenotazione prenotazione;

    @ManyToOne(optional = false)
    @JoinColumn(name = "piscina_id")
    private Piscina piscina;

    @Column(length = 1000)
    private String note;

    @Column(length = 1000)
    private Integer numPersone;

    public Long getId() {
        return accede_id;
    }

    public Piscina getPiscina() {
        return piscina;
    }

    public void setPiscina(Piscina piscina) {
        this.piscina = piscina;
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
