package it.rf.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FeedbackDto {

    @NotBlank(message = "Note obbligatorie")
    @Size(max = 2000, message = "Note troppo lunghe")
    private String note;

    private String cfCliente;

    private String cognomeCliente;
    private String nomeCliente;

    public FeedbackDto() {
    }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getCfCliente() { return cfCliente; }
    public void setCfCliente(String cfCliente) { this.cfCliente = cfCliente; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getCognomeCliente() { return cognomeCliente; }
    public void setCognomeCliente(String cognomeCliente) { this.cognomeCliente = cognomeCliente; }
}
