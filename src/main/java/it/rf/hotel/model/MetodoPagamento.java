package it.rf.hotel.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "metodi_pagamento")
public class MetodoPagamento {
    public MetodoPagamento() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodo_pagamento_id")
    private Long metodo_pagamento_id;

    @Column(nullable = false, unique = true, length = 30)
    private String tipo;

    @OneToMany(mappedBy = "metodo")
    private List<Pagamento> pagamenti = new ArrayList<>();

    public Long getId() { return metodo_pagamento_id; }
    public String getTipo() { return tipo; } public void setTipo(String tipo) { this.tipo = tipo; }
}
