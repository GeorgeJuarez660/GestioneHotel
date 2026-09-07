package it.rf.hotel.exception;

public class BevandaUnavailableException extends Exception {

	
	private String nome;
	
	public BevandaUnavailableException() {
		
	}
	
	
	public BevandaUnavailableException(String nome) {
		System.out.println("La bevanda con nome " + nome + " non è disponibile");
		this.nome=nome;
	}


	public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
	
}
