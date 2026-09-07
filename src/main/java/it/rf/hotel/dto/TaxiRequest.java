package it.rf.hotel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class TaxiRequest {

    /* Il taxi non ha un codice come navette e stanze: l'unico identificativo
       e' l'id, quindi viaggia nella dto. */
    private Long id;

    @NotNull(message = "Data obbligatoria")
    private LocalDate data;

    @NotNull(message = "Ora obbligatoria")
    private LocalTime ora;

    @NotBlank(message = "Luogo di partenza obbligatorio")
    @Size(max = 255, message = "Luogo di partenza troppo lungo")
    private String luogoPartenza;

    @NotBlank(message = "Luogo di destinazione obbligatorio")
    @Size(max = 255, message = "Luogo di destinazione troppo lungo")
    private String luogoDestinazione;

    @NotNull(message = "Prezzo obbligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "Prezzo non valido")
    private BigDecimal prezzo;

    @NotNull(message = "Numero persone obbligatorio")
    @Positive(message = "Numero persone non valido")
    private Integer numPersone;

    @NotBlank(message = "Nome possessore obbligatorio")
    @Size(max = 100, message = "Nome possessore troppo lungo")
    private String nomePossessore;

    @NotBlank(message = "Cognome possessore obbligatorio")
    @Size(max = 100, message = "Cognome possessore troppo lungo")
    private String cognomePossessore;

    public TaxiRequest() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getOra() { return ora; }
    public void setOra(LocalTime ora) { this.ora = ora; }

    public String getLuogoPartenza() { return luogoPartenza; }
    public void setLuogoPartenza(String luogoPartenza) { this.luogoPartenza = luogoPartenza; }

    public String getLuogoDestinazione() { return luogoDestinazione; }
    public void setLuogoDestinazione(String luogoDestinazione) { this.luogoDestinazione = luogoDestinazione; }

    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }

    public Integer getNumPersone() { return numPersone; }
    public void setNumPersone(Integer numPersone) { this.numPersone = numPersone; }

    public String getNomePossessore() { return nomePossessore; }
    public void setNomePossessore(String nomePossessore) { this.nomePossessore = nomePossessore; }

    public String getCognomePossessore() { return cognomePossessore; }
    public void setCognomePossessore(String cognomePossessore) { this.cognomePossessore = cognomePossessore; }
}
