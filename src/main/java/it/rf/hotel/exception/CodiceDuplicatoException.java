package it.rf.hotel.exception;

public class CodiceDuplicatoException extends Exception {

	
	private String codice;
	
	public CodiceDuplicatoException() {
		
	}
	
	
	public CodiceDuplicatoException(String codice) {
		System.out.println("Il codice " + codice + " inserito risulta duplicato");
		this.codice=codice;
	}


	public String getCodice() {
		return codice;
	}


	public void setCodice(String codice) {
		this.codice = codice;
	}
	
	
	
}
