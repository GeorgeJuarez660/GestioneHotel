package it.rf.hotel.exception;

import java.time.LocalDate;

public class StanzaBookedException extends Exception {

	
	private String codice;
	private LocalDate dataInizio;
	private LocalDate dataFine;
	
	public StanzaBookedException() {
		
	}
	
	
	public StanzaBookedException(String codice, LocalDate dataInizio, LocalDate dataFine) {
		System.out.println("La stanza con codice " + codice + " da " + dataInizio.toString() + " a " + dataFine.toString() + " risulta occupata!");
		this.codice=codice;
		this.dataInizio=dataInizio;
		this.dataFine=dataFine;
	}


	public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
	
	public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }
}
