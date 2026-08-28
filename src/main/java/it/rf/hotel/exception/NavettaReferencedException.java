package it.rf.hotel.exception;

public class NavettaReferencedException extends Exception {

	
	private String codice;
	
	public NavettaReferencedException() {
		
	}
	
	
	public NavettaReferencedException(String codice) {
		System.out.println("La navetta con codice " + codice + " risulta in servizio ad una o più prenotazioni!");
		this.codice=codice;
	}


	public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
	
}
