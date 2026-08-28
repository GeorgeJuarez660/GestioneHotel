package it.rf.hotel.exception;

public class PiscinaReferencedException extends Exception {

	
	private String codice;
	
	public PiscinaReferencedException() {
		
	}
	
	
	public PiscinaReferencedException(String codice) {
		System.out.println("La piscina con codice " + codice + " risulta in servizio ad una o più prenotazioni!");
		this.codice=codice;
	}


	public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
	
}
