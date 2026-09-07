package it.rf.hotel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BevandaResponse {

    @NotBlank(message = "Nome obbligatorio")
    private String nome;

    @NotNull(message = "Quantità base obbligatorio")
    private Integer quantitaBase;

    @NotNull(message = "Prezzo base obbligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "Prezzo base non valido")
    private BigDecimal prezzoBase;

    @NotNull(message = "Alcolico obbligatorio")
    private Boolean alcolico = false;

    public BevandaResponse() {
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getQuantitaBase() { return quantitaBase; }
    public void setQuantitaBase(Integer quantitaBase) { this.quantitaBase = quantitaBase; }

    public BigDecimal getPrezzoBase() { return prezzoBase; }
    public void setPrezzoBase(BigDecimal prezzoBase) { this.prezzoBase = prezzoBase; }

    public Boolean getAlcolico() { return alcolico; }
    public void setAlcolico(Boolean alcolico) { this.alcolico = alcolico; }
}
