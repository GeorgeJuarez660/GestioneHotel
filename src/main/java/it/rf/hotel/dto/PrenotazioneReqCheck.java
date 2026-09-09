package it.rf.hotel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PrenotazioneReqCheck {

    @NotBlank(message = "Codice obbligatorio")
    @Size(max = 50, message = "Codice troppo lungo")
    private String codicePrenotazione;

    @NotNull(message = "Prezzo totale obbligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "Prezzo totale non valido")
    private BigDecimal prezzoTotale;

    /* Il prezzo effettivo lo ricalcola il service man mano che si addebitano
       consumazioni e taxi: puo' arrivare vuoto, quindi niente @NotNull. */
    @DecimalMin(value = "0.0", inclusive = true, message = "Prezzo effettivo non valido")
    private BigDecimal prezzoEffettivo;

    private LocalDate dataCheckIn;

    private LocalDate dataCheckOut;

    private Integer numPersone;

    @Size(max = 1000, message = "Note troppo lunghe")
    private String note;

    @NotNull(message = "Cliente obbligatorio")
    @Size(max = 16, message = "Codice fiscale cliente non valido")
    private String cfCliente;

    @NotNull(message = "Stato prenotazione obbligatorio")
    @Size(max = 16, message = "Codice stato prenotazione non valido")
    private String statoPrenotazione;

    @Size(max = 16, message = "Codice navetta non valido")
    private String codiceNavetta;
    
    @Size(max = 16, message = "Codice guida non valido")
    private String codiceGuida;

    @Size(max = 16, message = "Codice piscina non valido")
    private String codicePiscina;

    @Size(max = 16, message = "Codice stato pagamento non valido")
    private String statoPagamento;

    private List<ConsumaRequest> consumazioni;

    public PrenotazioneReqCheck() {
    }

    public String getCodicePrenotazione() { return codicePrenotazione; }
    public void setCodicePrenotazione(String codicePrenotazione) { this.codicePrenotazione = codicePrenotazione; }

    public LocalDate getDataCheckIn() { return dataCheckIn; }
    public void setDataCheckIn(LocalDate dataCheckIn) { this.dataCheckIn = dataCheckIn; }

    public LocalDate getDataCheckOut() { return dataCheckOut; }
    public void setDataCheckOut(LocalDate dataCheckOut) { this.dataCheckOut = dataCheckOut; }

    public BigDecimal getPrezzoTotale() { return prezzoTotale; }
    public void setPrezzoTotale(BigDecimal prezzoTotale) { this.prezzoTotale = prezzoTotale; }

    public BigDecimal getPrezzoEffettivo() { return prezzoEffettivo; }
    public void setPrezzoEffettivo(BigDecimal prezzoEffettivo) { this.prezzoEffettivo = prezzoEffettivo; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getCfCliente() { return cfCliente; }
    public void setCfCliente(String cfCliente) { this.cfCliente = cfCliente; }

    public String getCodiceNavetta() { return codiceNavetta; }
    public void setCodiceNavetta(String codiceNavetta) { this.codiceNavetta = codiceNavetta; }

    public String getCodiceGuida() { return codiceGuida; }
    public void setCodiceGuida(String codiceGuida) { this.codiceGuida = codiceGuida; }

    public String getCodicePiscina() { return codicePiscina; }
    public void setCodicePiscina(String codicePiscina) { this.codicePiscina = codicePiscina; }

    public String getStatoPrenotazione() { return statoPrenotazione; }
    public void setStatoPrenotazione(String statoPrenotazione) { this.statoPrenotazione = statoPrenotazione; }

    public String getStatoPagamento() { return statoPagamento; }
    public void setStatoPagamento(String statoPagamento) { this.statoPagamento = statoPagamento; }

    public Integer getNumPersone() { return numPersone; }
    public void setNumPersone(Integer numPersone) { this.numPersone = numPersone; }

    public List<ConsumaRequest> getConsumazioni() { return consumazioni; }
    public void setConsumazioni(List<ConsumaRequest> consumazioni) { this.consumazioni = consumazioni; }

}
