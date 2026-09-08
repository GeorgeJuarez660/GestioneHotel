package it.rf.hotel.exception;

public class NotDipendenteFoundException extends Exception {

	
	private String username;
	private String password;
    private String codDipendente;
	
	public NotDipendenteFoundException() {
		
	}
	
	
	public NotDipendenteFoundException(String username, String password, String codDipendente) {
		System.out.println("Il dipendente con " + username + ", " + password + " e " + codDipendente + " non è stato trovato!");
		this.username=username;
		this.password=password;
		this.codDipendente=codDipendente;
	}


	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodDipendente() {
        return codDipendente;
    }

    public void setCodDipendente(String codDipendente) {
        this.codDipendente = codDipendente;
    }
	
}
