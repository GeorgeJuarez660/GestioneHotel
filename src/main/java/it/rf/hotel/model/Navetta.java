package it.rf.hotel.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "navette")
public class Navetta {
    public Navetta() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long navetta_id;

    @Column(nullable = false)
    private String codice;

    @Column(nullable = false)
    private LocalDate dataPartenza;

    @Column(nullable = false)
    private LocalTime oraPartenza;

    @Column(nullable = false)
    private String luogoDestinazione;

    @Column(nullable = false)
    private String luogoPartenza;

    @Column(nullable = false)
    private Integer numPostiMax;

    @Column(nullable = false)
    private Integer numPostiDisp;

    @ManyToOne(optional = true)
    @JoinColumn(name = "dipendente_id", nullable = true)
    private Dipendente operatoreInterno;

    @ManyToOne(optional = true)
    @JoinColumn(name = "operatore_esterno_id", nullable = true)
    private OperatoreEsterno operatoreEsterno;

    public Long getId() {
        return navetta_id;
    }

    public void setId(Long navetta_id) {
        this.navetta_id = navetta_id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public LocalDate getDataPartenza() {
        return dataPartenza;
    }

    public void setDataPartenza(LocalDate dataPartenza) {
        this.dataPartenza = dataPartenza;
    }

    public LocalTime getOraPartenza() {
        return oraPartenza;
    }

    public void setOraPartenza(LocalTime oraPartenza) {
        this.oraPartenza = oraPartenza;
    }

    public String getLuogoDestinazione() {
        return luogoDestinazione;
    }

    public void setLuogoDestinazione(String luogoDestinazione) {
        this.luogoDestinazione = luogoDestinazione;
    }

    public Dipendente getOperatoreInterno() {
        return operatoreInterno;
    }

    public void setOperatoreInterno(Dipendente operatoreInterno) {
        this.operatoreInterno = operatoreInterno;
    }

    public OperatoreEsterno getOperatoreEsterno() {
        return operatoreEsterno;
    }

    public void setOperatoreEsterno(OperatoreEsterno operatoreEsterno) {
        this.operatoreEsterno = operatoreEsterno;
    }

    public String getLuogoPartenza() {
        return luogoPartenza;
    }

    public void setLuogoPartenza(String luogoPartenza) {
        this.luogoPartenza = luogoPartenza;
    }

    public Integer getNumPostiMax() {
        return numPostiMax;
    }

    public void setNumPostiMax(Integer numPostiMax) {
        this.numPostiMax = numPostiMax;
    }

    public Integer getNumPostiDisp() {
        return numPostiDisp;
    }

    public void setNumPostiDisp(Integer numPostiDisp) {
        this.numPostiDisp = numPostiDisp;
    }

}
