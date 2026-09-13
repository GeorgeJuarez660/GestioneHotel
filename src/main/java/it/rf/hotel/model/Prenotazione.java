package it.rf.hotel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "prenotazioni")
public class Prenotazione {
    public Prenotazione() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long prenotazione_id;

    @Column(nullable = false, unique = true)
    private String codice;

    @Column(nullable = false)
    private LocalDate dataPrenotazione;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prezzoTotale;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prezzoEffettivo;

    @Column(nullable = false)
    private LocalDate dataInizio;

    @Column(nullable = false)
    private LocalDate dataFine;

    @Column(length = 1000)
    private String note;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stato_prenotazione_id")
    private StatoPrenotazione stato;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dipendente_id")
    private Dipendente receptionist;

    @ManyToOne(optional = true)
    @JoinColumn(name = "operatore_esterno_id", nullable = true)
    private OperatoreEsterno receptionistEsterno;

    public Long getId() {
        return prenotazione_id;
    }

    public void setId(Long prenotazione_id) {
        this.prenotazione_id = prenotazione_id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public LocalDate getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(LocalDate dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public BigDecimal getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(BigDecimal prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public BigDecimal getPrezzoEffettivo() {
        return prezzoEffettivo;
    }

    public void setPrezzoEffettivo(BigDecimal prezzoEffettivo) {
        this.prezzoEffettivo = prezzoEffettivo;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public StatoPrenotazione getStato() {
        return stato;
    }

    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    public Dipendente getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(Dipendente receptionist) {
        this.receptionist = receptionist;
    }

    public OperatoreEsterno getReceptionistEsterno() {
        return receptionistEsterno;
    }

    public void setReceptionistEsterno(OperatoreEsterno receptionistEsterno) {
        this.receptionistEsterno = receptionistEsterno;
    }
}

