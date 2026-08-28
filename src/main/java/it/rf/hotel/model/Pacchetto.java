package it.rf.hotel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pacchetti")
public class Pacchetto {
    public Pacchetto() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long pacchetto_id;

    @Column(nullable = false, unique = true)
    private String tipoPensione;

    @Column(nullable = false)
    private Boolean colazione = false;

    @Column(nullable = false)
    private Boolean navetta = false;

    @Column(nullable = false)
    private Boolean guida = false;

    @Column(nullable = false)
    private Boolean piscina = false;

    @Column(nullable = false)
    private Boolean parcheggio = false;

    @Column(length = 1000)
    private String descrizione;

    @Column(length = 1000)
    private Integer percentuale;

    public Long getId() { return pacchetto_id; }
    public String getTipoPensione() { return tipoPensione; } public void setTipoPensione(String tipoPensione) { this.tipoPensione = tipoPensione; }
    public Boolean getColazione() { return colazione; } public void setColazione(Boolean colazione) { this.colazione = colazione; }
    public Boolean getGuida() { return guida; } public void setGuida(Boolean guida) { this.guida = guida; }
    public Boolean getNavetta() { return navetta; } public void setNavetta(Boolean navetta) { this.navetta = navetta; }
    public Boolean getPiscina() { return piscina; } public void setPiscina(Boolean piscina) { this.piscina = piscina; }
    public Boolean getParcheggio() { return parcheggio; } public void setParcheggio(Boolean parcheggio) { this.parcheggio = parcheggio; }
    public String getDescrizione() { return descrizione; } public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public Integer getPercentuale() { return percentuale; } public void setPercentuale(Integer percentuale) { this.percentuale = percentuale; }
}
