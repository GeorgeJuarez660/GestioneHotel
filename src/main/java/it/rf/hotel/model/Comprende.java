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
@Table(name = "comprende")
public class Comprende {

    public Comprende() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long comprende_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prenotazione_id")
    private Prenotazione prenotazione;

    @ManyToOne(optional = false)
    @JoinColumn(name = "navetta_id")
    private Navetta navetta;

    @Column(length = 1000)
    private String note;

    @Column(length = 1000)
    private Integer numPasseggeri;

    public Long getId() {
        return comprende_id;
    }

    public Navetta getNavetta() {
        return navetta;
    }

    public void setNavetta(Navetta navetta) {
        this.navetta = navetta;
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

    public Integer getNumPasseggeri() {
        return numPasseggeri;
    }

    public void setNumPasseggeri(Integer numPasseggeri) {
        this.numPasseggeri = numPasseggeri;
    }
}
