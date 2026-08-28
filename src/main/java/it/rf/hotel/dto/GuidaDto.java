package it.rf.hotel.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public class GuidaDto {

    @NotBlank(message = "Codice obbligatorio")
    @Size(max = 255, message = "Codice troppo lungo")
    private String codice;

    @NotNull(message = "Data di partenza obbligatoria")
    @FutureOrPresent(message = "Data di partenza non valida")
    private LocalDate data;

    @NotNull(message = "Ora di partenza obbligatoria")
    private LocalTime ora;

    @NotBlank(message = "Luogo di destinazione obbligatorio")
    @Size(max = 255, message = "Luogo di destinazione troppo lungo")
    private String luogo;

    @Size(max = 16, message = "Codice fiscale operatore interno non valido")
    private String cfOperatoreInterno;

    @Size(max = 16, message = "Codice fiscale operatore esterno non valido")
    private String cfOperatoreEsterno;

    private String nomeOperatoreInterno;
    private String cognomeOperatoreInterno;

    public GuidaDto() {
    }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getOra() { return ora; }
    public void setOra(LocalTime ora) { this.ora = ora; }

    public String getLuogo() { return luogo; }
    public void setLuogo(String luogo) { this.luogo = luogo; }

    public String getCfOperatoreInterno() { return cfOperatoreInterno; }
    public void setCfOperatoreInterno(String cfOperatoreInterno) { this.cfOperatoreInterno = cfOperatoreInterno; }

    public String getCfOperatoreEsterno() { return cfOperatoreEsterno; }
    public void setCfOperatoreEsterno(String cfOperatoreEsterno) { this.cfOperatoreEsterno = cfOperatoreEsterno; }

    public String getNomeOperatoreInterno() { return nomeOperatoreInterno; }
    public String getCognomeOperatoreInterno() { return cognomeOperatoreInterno; }
    public void setNomeOperatoreInterno(String nomeOperatoreInterno) { this.nomeOperatoreInterno = nomeOperatoreInterno; }
    public void setCognomeOperatoreInterno(String cognomeOperatoreInterno) { this.cognomeOperatoreInterno = cognomeOperatoreInterno; }
}
