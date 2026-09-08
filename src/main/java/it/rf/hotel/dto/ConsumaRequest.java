package it.rf.hotel.dto;

public class ConsumaRequest {

    private String nomeBevanda;

    private Integer quantitaOrdinata;

    public ConsumaRequest() {
    }

    public String getNomeBevanda() { return nomeBevanda; }
    public void setNomeBevanda(String nomeBevanda) { this.nomeBevanda = nomeBevanda; }

    public Integer getQuantitaOrdinata() { return quantitaOrdinata; }
    public void setQuantitaOrdinata(Integer quantitaOrdinata) { this.quantitaOrdinata = quantitaOrdinata; }
}
