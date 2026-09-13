package it.rf.hotel.exception;

public class BevandaReferencedException extends Exception {

	
	private String nome;
	
	public BevandaReferencedException() {
		
	}
	
	
	public BevandaReferencedException(String nome) {
		System.out.println("La bevanda con nome " + nome + " risulta in consumazione ad una o più prenotazioni!");
		this.nome=nome;
	}


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
