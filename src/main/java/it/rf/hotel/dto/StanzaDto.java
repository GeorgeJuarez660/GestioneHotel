package it.rf.hotel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class StanzaDto {

    @NotBlank(message = "Codice obbligatorio")
    @Size(max = 50, message = "Codice troppo lungo")
    private String codice;

    @NotNull(message = "Capienza obbligatoria")
    @Positive(message = "Capienza non valida")
    private Integer capienza;

    @NotNull(message = "Piano obbligatorio")
    private Integer piano;

    @NotNull(message = "Prezzo base obbligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "Prezzo base non valido")
    private BigDecimal prezzoBase;

    @NotNull(message = "Termoregolabile obbligatorio")
    private Boolean termoregolabile;

    @Size(max = 1000, message = "Note troppo lunghe")
    private String note;

    @NotBlank(message = "Tipo stanza obbligatorio")
    @Size(max = 50, message = "Tipo stanza troppo lungo")
    private String tipoStanza;

    public StanzaDto() {
    }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }

    public Integer getCapienza() { return capienza; }
    public void setCapienza(Integer capienza) { this.capienza = capienza; }

    public Integer getPiano() { return piano; }
    public void setPiano(Integer piano) { this.piano = piano; }

    public BigDecimal getPrezzoBase() { return prezzoBase; }
    public void setPrezzoBase(BigDecimal prezzoBase) { this.prezzoBase = prezzoBase; }

    public Boolean getTermoregolabile() { return termoregolabile; }
    public void setTermoregolabile(Boolean termoregolabile) { this.termoregolabile = termoregolabile; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getTipoStanza() { return tipoStanza; }
    public void setTipoStanza(String tipoStanza) { this.tipoStanza = tipoStanza; }
}
