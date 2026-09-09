package it.rf.hotel.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "guide")
public class Guida {
    public Guida() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long guida_id;

    @Column(nullable = false)
    private String codice;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime ora;

    @Column(nullable = false)
    private String luogo;

    @ManyToOne(optional = true)
    @JoinColumn(name = "dipendente_id", nullable = true)
    private Dipendente operatoreInterno;

    @ManyToOne(optional = true)
    @JoinColumn(name = "operatore_esterno_id", nullable = true)
    private OperatoreEsterno operatoreEsterno;

    public Long getId() {
        return guida_id;
    }

    public void setId(Long guida_id) {
        this.guida_id = guida_id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setOra(LocalTime ora){
        this.ora = ora;
    }

    public LocalTime getOra(){
        return ora;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
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

}
