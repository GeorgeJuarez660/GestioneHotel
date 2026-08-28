package it.rf.hotel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PrenotazioneResponse {

    @NotBlank(message = "Codice obbligatorio")
    @Size(max = 50, message = "Codice troppo lungo")
    private String codice;

    @NotNull(message = "Data prenotazione obbligatoria")
    private LocalDate dataPrenotazione;

    @NotNull(message = "Prezzo totale obbligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "Prezzo totale non valido")
    private BigDecimal prezzoTotale;

    @NotNull(message = "Prezzo effettivo obbligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "Prezzo effettivo non valido")
    private BigDecimal prezzoEffettivo;

    @NotNull(message = "Data inizio obbligatoria")
    private LocalDate dataInizio;

    @NotNull(message = "Data fine obbligatoria")
    private LocalDate dataFine;

    @Size(max = 1000, message = "Note troppo lunghe")
    private String note;

    @Size(max = 16, message = "Codice fiscale cliente non valido")
    private String cfCliente;

    private String nomeCliente;
    private String cognomeCliente;

    @Size(max = 16, message = "Codice fiscale receptionist non valido")
    private String cfReceptionist;

    private String nomeReceptionist;
    private String cognomeReceptionist;

    @NotNull(message = "Stanza obbligatoria")
    @Size(max = 16, message = "Codice stanza non valido")
    private String codiceStanza;

    @Size(max = 16, message = "Codice stato prenotazione non valido")
    private String statoPrenotazione;

    @Size(max = 16, message = "Codice navetta non valido")
    private String codiceNavetta;

    @Size(max = 16, message = "Codice guida non valido")
    private String codiceGuida;

    @Size(max = 16, message = "Codice piscina non valido")
    private String codicePiscina;

    private String tipoPacchetto;

    @NotNull(message = "Numero persone obbligatorio")
    private Integer numPersone;

    public PrenotazioneResponse() {
    }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }

    public LocalDate getDataPrenotazione() { return dataPrenotazione; }
    public void setDataPrenotazione(LocalDate dataPrenotazione) { this.dataPrenotazione = dataPrenotazione; }

    public BigDecimal getPrezzoTotale() { return prezzoTotale; }
    public void setPrezzoTotale(BigDecimal prezzoTotale) { this.prezzoTotale = prezzoTotale; }

    public BigDecimal getPrezzoEffettivo() { return prezzoEffettivo; }
    public void setPrezzoEffettivo(BigDecimal prezzoEffettivo) { this.prezzoEffettivo = prezzoEffettivo; }

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getCfCliente() { return cfCliente; }
    public void setCfCliente(String cfCliente) { this.cfCliente = cfCliente; }

    public String getCfReceptionist() { return cfReceptionist; }
    public void setCfReceptionist(String cfReceptionist) { this.cfReceptionist = cfReceptionist; }

    public String getCodiceStanza() { return codiceStanza; }
    public void setCodiceStanza(String codiceStanza) { this.codiceStanza = codiceStanza; }

    public String getCodiceNavetta() { return codiceNavetta; }
    public void setCodiceNavetta(String codiceNavetta) { this.codiceNavetta = codiceNavetta; }

    public String getCodiceGuida() { return codiceGuida; }
    public void setCodiceGuida(String codiceGuida) { this.codiceGuida = codiceGuida; }

    public String getCodicePiscina() { return codicePiscina; }
    public void setCodicePiscina(String codicePiscina) { this.codicePiscina = codicePiscina; }

    public String getTipoPacchetto() { return tipoPacchetto; }
    public void setTipoPacchetto(String tipoPacchetto) { this.tipoPacchetto = tipoPacchetto; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getCognomeCliente() { return cognomeCliente; }
    public void setCognomeCliente(String cognomeCliente) { this.cognomeCliente = cognomeCliente; }

    public String getNomeReceptionist() { return nomeReceptionist; }
    public void setNomeReceptionist(String nomeReceptionist) { this.nomeReceptionist = nomeReceptionist; }

    public String getCognomeReceptionist() { return cognomeReceptionist; }
    public void setCognomeReceptionist(String cognomeReceptionist) { this.cognomeReceptionist = cognomeReceptionist; }

    public String getStatoPrenotazione() { return statoPrenotazione; }
    public void setStatoPrenotazione(String statoPrenotazione) { this.statoPrenotazione = statoPrenotazione; }

    public Integer getNumPersone() { return numPersone; }
    public void setNumPersone(Integer numPersone) { this.numPersone = numPersone; }

}
