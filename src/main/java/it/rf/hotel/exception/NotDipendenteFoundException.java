package it.rf.hotel.exception;

public class NotDipendenteFoundException extends Exception {

	
	private String username;
	private String password;
	
	public NotDipendenteFoundException() {
		
	}
	
	
	public NotDipendenteFoundException(String username, String password) {
		System.out.println("Il dipendente con " + username + " e " + password + " non è stato trovato!");
		this.username=username;
		this.password=password;
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
	
}
