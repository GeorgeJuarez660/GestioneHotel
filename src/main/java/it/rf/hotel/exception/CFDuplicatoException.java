package it.rf.hotel.exception;

public class CFDuplicatoException extends Exception {

	
	private String cf;
	
	public CFDuplicatoException() {
		
	}
	
	
	public CFDuplicatoException(String cf) {
		System.out.println("Il cf " + cf + " inserito risulta duplicato");
		this.cf=cf;
	}


	public String getCf() {
		return cf;
	}


	public void setCf(String cf) {
		this.cf = cf;
	}
	
	
	
}
