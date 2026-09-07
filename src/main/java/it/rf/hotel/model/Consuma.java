package it.rf.hotel.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "consuma")
public class Consuma {

    public Consuma() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long consuma_id;

    @Column(nullable = true)
    private LocalDate dataConsumata;

    @Column(nullable = true)
    private LocalTime oraConsumata;

    @Column(nullable = false)
    private Integer quantitaOrdinata;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prezzoEffettivo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gestisce_id")
    private Gestisce gestisce;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bevanda_id")
    private Bevanda bevanda;

    public Long getId() {
        return consuma_id;
    }

    public LocalDate getDataConsumata() {
        return dataConsumata;
    }

    public void setDataConsumata(LocalDate dataConsumata) {
        this.dataConsumata = dataConsumata;
    }

    public LocalTime getOraConsumata() {
        return oraConsumata;
    }

    public void setOraConsumata(LocalTime oraConsumata) {
        this.oraConsumata = oraConsumata;
    }

    public Integer getQuantitaOrdinata() {
        return quantitaOrdinata;
    }

    public void setQuantitaOrdinata(Integer quantitaOrdinata) {
        this.quantitaOrdinata = quantitaOrdinata;
    }

    public BigDecimal getPrezzoEffettivo() {
        return prezzoEffettivo;
    }

    public void setPrezzoEffettivo(BigDecimal prezzoEffettivo) {
        this.prezzoEffettivo = prezzoEffettivo;
    }

    public Gestisce getGestisce() {
        return gestisce;
    }

    public void setGestisce(Gestisce gestisce) {
        this.gestisce = gestisce;
    }

    public Bevanda getBevanda() {
        return bevanda;
    }

    public void setBevanda(Bevanda bevanda) {
        this.bevanda = bevanda;
    }
}
