package it.rf.hotel.exception;

public class StanzaReferencedException extends Exception {

	
	private String codice;
	
	public StanzaReferencedException() {
		
	}
	
	
	public StanzaReferencedException(String codice) {
		System.out.println("La stanza con codice " + codice + " risulta prenotata!");
		this.codice=codice;
	}


	public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
	
}
