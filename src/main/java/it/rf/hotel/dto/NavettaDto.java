package it.rf.hotel.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public class NavettaDto {

    @NotBlank(message = "Codice obbligatorio")
    @Size(max = 255, message = "Codice troppo lungo")
    private String codice;

    @NotNull(message = "Data di partenza obbligatoria")
    @FutureOrPresent(message = "Data di partenza non valida")
    private LocalDate dataPartenza;

    @NotNull(message = "Ora di partenza obbligatoria")
    private LocalTime oraPartenza;

    @NotBlank(message = "Luogo di destinazione obbligatorio")
    @Size(max = 255, message = "Luogo di destinazione troppo lungo")
    private String luogoDestinazione;

    @NotBlank(message = "Luogo di partenza obbligatorio")
    @Size(max = 255, message = "Luogo di partenza troppo lungo")
    private String luogoPartenza;

    @Size(max = 16, message = "Codice fiscale operatore interno non valido")
    private String cfOperatoreInterno;

    @Size(max = 16, message = "Codice fiscale operatore esterno non valido")
    private String cfOperatoreEsterno;

    @NotNull(message = "Numero posti massimi obbligatorio")
    private Integer numPostiMax;

    @NotNull(message = "Numero posti disponibili obbligatorio")
    private Integer numPostiDisp;

    private String nomeOperatoreInterno;
    private String cognomeOperatoreInterno;

    public NavettaDto() {
    }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }

    public LocalDate getDataPartenza() { return dataPartenza; }
    public void setDataPartenza(LocalDate dataPartenza) { this.dataPartenza = dataPartenza; }

    public LocalTime getOraPartenza() { return oraPartenza; }
    public void setOraPartenza(LocalTime oraPartenza) { this.oraPartenza = oraPartenza; }

    public String getLuogoDestinazione() { return luogoDestinazione; }
    public void setLuogoDestinazione(String luogoDestinazione) { this.luogoDestinazione = luogoDestinazione; }

    public String getLuogoPartenza() { return luogoPartenza; }
    public void setLuogoPartenza(String luogoPartenza) { this.luogoPartenza = luogoPartenza; }

    public Integer getNumPostiMax() {
        return numPostiMax;
    }

    public void setNumPostiMax(Integer numPostiMax) {
        this.numPostiMax = numPostiMax;
    }

    public Integer getNumPostiDisp() {
        return numPostiDisp;
    }

    public void setNumPostiDisp(Integer numPostiDisp) {
        this.numPostiDisp = numPostiDisp;
    }
    
    public String getCfOperatoreInterno() { return cfOperatoreInterno; }
    public void setCfOperatoreInterno(String cfOperatoreInterno) { this.cfOperatoreInterno = cfOperatoreInterno; }

    public String getCfOperatoreEsterno() { return cfOperatoreEsterno; }
    public void setCfOperatoreEsterno(String cfOperatoreEsterno) { this.cfOperatoreEsterno = cfOperatoreEsterno; }

    public String getNomeOperatoreInterno() { return nomeOperatoreInterno; }
    public String getCognomeOperatoreInterno() { return cognomeOperatoreInterno; }
    public void setNomeOperatoreInterno(String nomeOperatoreInterno) { this.nomeOperatoreInterno = nomeOperatoreInterno; }
    public void setCognomeOperatoreInterno(String cognomeOperatoreInterno) { this.cognomeOperatoreInterno = cognomeOperatoreInterno; }
}
