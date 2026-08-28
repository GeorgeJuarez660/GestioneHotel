package it.rf.hotel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagamenti")
public class Pagamento {
    public Pagamento() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long pagamento_id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importo;

    @Column(nullable = false)
    private LocalDate dataPagamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prenotazione_id")
    private Prenotazione prenotazione;

    @ManyToOne(optional = false)
    @JoinColumn(name = "metodo_pagamento_id")
    private MetodoPagamento metodo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stato_pagamento_id")
    private StatoPagamento stato;

    public Long getId() { return pagamento_id; }
    public BigDecimal getImporto() { return importo; } public void setImporto(BigDecimal importo) { this.importo = importo; }
    public LocalDate getDataPagamento() { return dataPagamento; } public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public Prenotazione getPrenotazione() { return prenotazione; } public void setPrenotazione(Prenotazione prenotazione) { this.prenotazione = prenotazione; }
    public MetodoPagamento getMetodo() { return metodo; } public void setMetodo(MetodoPagamento metodo) { this.metodo = metodo; }
    public StatoPagamento getStato() { return stato; } public void setStato(StatoPagamento stato) { this.stato = stato; }
}
