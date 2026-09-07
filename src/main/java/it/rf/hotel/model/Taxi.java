package it.rf.hotel.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "taxi")
public class Taxi {

    public Taxi() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long taxi_id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime ora;

    @Column(nullable = false)
    private String luogoPartenza;

    @Column(nullable = false)
    private String luogoDestinazione;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prezzo;

    @Column(nullable = false)
    private Integer numPersone;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gestisce_id")
    private Gestisce gestisce;

    public Long getId() {
        return taxi_id;
    }

    public void setId(Long taxi_id) {
        this.taxi_id = taxi_id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public String getLuogoPartenza() {
        return luogoPartenza;
    }

    public void setLuogoPartenza(String luogoPartenza) {
        this.luogoPartenza = luogoPartenza;
    }

    public String getLuogoDestinazione() {
        return luogoDestinazione;
    }

    public void setLuogoDestinazione(String luogoDestinazione) {
        this.luogoDestinazione = luogoDestinazione;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getNumPersone() {
        return numPersone;
    }

    public void setNumPersone(Integer numPersone) {
        this.numPersone = numPersone;
    }

    public Gestisce getGestisce() {
        return gestisce;
    }

    public void setGestisce(Gestisce gestisce) {
        this.gestisce = gestisce;
    }
}
