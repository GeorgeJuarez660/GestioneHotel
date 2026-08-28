package it.rf.hotel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    public Feedback() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long feedback_id;

    @Column(nullable = false, length = 2000)
    private String note;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Long getId() { return feedback_id; }
    public String getNote() { return note; } public void setNote(String note) { this.note = note; }
    public Cliente getCliente() { return cliente; } public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
