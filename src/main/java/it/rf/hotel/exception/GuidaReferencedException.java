package it.rf.hotel.exception;

public class GuidaReferencedException extends Exception {

	
	private String codice;
	
	public GuidaReferencedException() {
		
	}
	
	
	public GuidaReferencedException(String codice) {
		System.out.println("La guida con codice " + codice + " risulta in servizio ad una o più prenotazioni!");
		this.codice=codice;
	}


	public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
	
}
