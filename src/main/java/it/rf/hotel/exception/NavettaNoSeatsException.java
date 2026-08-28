package it.rf.hotel.exception;

public class NavettaNoSeatsException extends Exception {

	
	private String codice;
	
	public NavettaNoSeatsException() {
		
	}
	
	
	public NavettaNoSeatsException(String codice) {
		System.out.println("Non ci sono posti disponibili per la navetta con codice " + codice);
		this.codice=codice;
	}


	public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
	
}
