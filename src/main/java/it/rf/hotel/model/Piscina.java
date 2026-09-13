package it.rf.hotel.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "piscine")
public class Piscina {
    public Piscina() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long piscina_id;

    @Column(nullable = false)
    private String codice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal larghezza;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal lunghezza;

    @ManyToOne(optional = true)
    @JoinColumn(name = "dipendente_id", nullable = true)
    private Dipendente operatoreInterno;

    public Long getId() {
        return piscina_id;
    }

    public void setId(Long piscina_id) {
        this.piscina_id = piscina_id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public BigDecimal getLarghezza(){
        return larghezza;
    }

    public BigDecimal getLunghezza(){
        return lunghezza;
    }

    public void setLunghezza(BigDecimal lunghezza){
        this.lunghezza = lunghezza;
    }

    public void setLarghezza(BigDecimal larghezza){
        this.larghezza = larghezza;
    }

    public Dipendente getOperatoreInterno() {
        return operatoreInterno;
    }

    public void setOperatoreInterno(Dipendente operatoreInterno) {
        this.operatoreInterno = operatoreInterno;
    }

}
