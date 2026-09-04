package it.rf.hotel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "stanze")
public class Stanza {
    public Stanza() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long stanza_id;

    @Column(nullable = false, unique = true)
    private String codice;

    @Column(nullable = false)
    private Integer capienza;

    @Column(nullable = false)
    private Integer piano;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prezzoBase;

    @Column(nullable = false)
    private Boolean termoregolabile = false;

    @Column(length = 1000)
    private String note;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_stanza_id")
    private TipoStanza tipo;

    public Long getId() {
        return stanza_id;
    }

    public void setId(Long stanza_id) {
        this.stanza_id = stanza_id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Integer getCapienza() {
        return capienza;
    }

    public void setCapienza(Integer capienza) {
        this.capienza = capienza;
    }

    public Integer getPiano() {
        return piano;
    }

    public void setPiano(Integer piano) {
        this.piano = piano;
    }

    public BigDecimal getPrezzoBase() {
        return prezzoBase;
    }

    public void setPrezzoBase(BigDecimal prezzoBase) {
        this.prezzoBase = prezzoBase;
    }

    public Boolean getTermoregolabile() {
        return termoregolabile;
    }

    public void setTermoregolabile(Boolean termoregolabile) {
        this.termoregolabile = termoregolabile;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public TipoStanza getTipo() {
        return tipo;
    }

    public void setTipo(TipoStanza tipo) {
        this.tipo = tipo;
    }
}

