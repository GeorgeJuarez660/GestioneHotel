package it.rf.hotel.exception;

public class PacchettoReferencedException extends Exception {

	
	private String tipoPensione;
	
	public PacchettoReferencedException() {
		
	}
	
	
	public PacchettoReferencedException(String tipoPensione) {
		System.out.println("Il pacchetto " + tipoPensione + " risulta in uso ad una o più prenotazioni!");
		this.tipoPensione=tipoPensione;
	}


	public String getTipoPensione() {
        return tipoPensione;
    }

    public void setTipoPensione(String tipoPensione) {
        this.tipoPensione = tipoPensione;
    }
	
}
