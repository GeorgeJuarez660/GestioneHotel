package it.rf.hotel.dto;

public class PacchettoDto {

    private String tipoPensione;
    private boolean navetta;
    private boolean guida;
    private boolean colazione;
    private boolean piscina;
    private boolean parcheggio;
    private String descrizione;
    private Integer percentuale;

    public PacchettoDto() {
    }

    public String getTipoPensione() { return tipoPensione; }
    public void setTipoPensione(String tipoPensione) { this.tipoPensione = tipoPensione; }

    public boolean isNavetta() { return navetta; }
    public void setNavetta(boolean navetta) { this.navetta = navetta; }

    public boolean isGuida() { return guida; }
    public void setGuida(boolean guida) { this.guida = guida; }

    public boolean isColazione() { return colazione; }
    public void setColazione(boolean colazione) { this.colazione = colazione; }

    public boolean isPiscina() { return piscina; }
    public void setPiscina(boolean piscina) { this.piscina = piscina; }

    public boolean isParcheggio() { return parcheggio; }
    public void setParcheggio(boolean parcheggio) { this.parcheggio = parcheggio; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public Integer getPercentuale() { return percentuale; }
    public void setPercentuale(Integer percentuale){ this.percentuale = percentuale; }

}
