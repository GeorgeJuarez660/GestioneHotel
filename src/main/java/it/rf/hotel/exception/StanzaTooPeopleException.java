package it.rf.hotel.exception;

import java.util.List;

public class StanzaTooPeopleException extends Exception {

	
	private List<String> codice;
	
	public StanzaTooPeopleException() {
		
	}
	
	
	public StanzaTooPeopleException(List<String> codice) {
		System.out.println("Il numero di persone risulta insufficiente per le stanze con codice " + String.join(", ", codice).toString());
		this.codice=codice;
	}


	public List<String> getCodice() {
        return codice;
    }

    public void setCodice(List<String> codice) {
        this.codice = codice;
    }
}
