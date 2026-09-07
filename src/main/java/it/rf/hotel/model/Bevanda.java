package it.rf.hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

@Entity
@Table(name = "bevande")
public class Bevanda {

    public Bevanda() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long bevanda_id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer quantitaBase;

    @Column(nullable = false, precision = 12, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true, message = "Prezzo base non valido")
    private BigDecimal prezzoBase;

    @Column(nullable = false)
    private Boolean alcolico = false;

    public Long getId() {
        return bevanda_id;
    }

    public void setId(Long bevanda_id) {
        this.bevanda_id = bevanda_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantitaBase() {
        return quantitaBase;
    }

    public void setQuantitaBase(Integer quantitaBase) {
        this.quantitaBase = quantitaBase;
    }

    public BigDecimal getPrezzoBase() {
        return prezzoBase;
    }

    public void setPrezzoBase(BigDecimal prezzoBase) {
        this.prezzoBase = prezzoBase;
    }

    public Boolean getAlcolico() {
        return alcolico;
    }

    public void setAlcolico(Boolean alcolico) {
        this.alcolico = alcolico;
    }
}
