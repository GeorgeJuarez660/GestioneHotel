package it.rf.hotel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class PiscinaDto {

    @NotBlank(message = "Codice obbligatorio")
    @Size(max = 255, message = "Codice troppo lungo")
    private String codice;

    @NotNull(message = "Larghezza obbligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "Larghezza non valida")
    private BigDecimal larghezza;

    @NotNull(message = "Lunghezza obbligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "Lunghezza non valida")
    private BigDecimal lunghezza;

    @Size(max = 16, message = "Codice fiscale operatore interno non valido")
    private String cfOperatoreInterno;

    private String nomeOperatoreInterno;
    private String cognomeOperatoreInterno;

    public PiscinaDto() {
    }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }

    public BigDecimal getLarghezza() { return larghezza; }
    public void setLarghezza(BigDecimal larghezza) { this.larghezza = larghezza; }

    public BigDecimal getLunghezza() { return lunghezza; }
    public void setLunghezza(BigDecimal lunghezza) { this.lunghezza = lunghezza; }

    public String getCfOperatoreInterno() { return cfOperatoreInterno; }
    public void setCfOperatoreInterno(String cfOperatoreInterno) { this.cfOperatoreInterno = cfOperatoreInterno; }

    public String getNomeOperatoreInterno() { return nomeOperatoreInterno; }
    public String getCognomeOperatoreInterno() { return cognomeOperatoreInterno; }
    public void setNomeOperatoreInterno(String nomeOperatoreInterno) { this.nomeOperatoreInterno = nomeOperatoreInterno; }
    public void setCognomeOperatoreInterno(String cognomeOperatoreInterno) { this.cognomeOperatoreInterno = cognomeOperatoreInterno; }
}
