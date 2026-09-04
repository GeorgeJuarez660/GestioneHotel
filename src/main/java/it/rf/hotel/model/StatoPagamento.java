package it.rf.hotel.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stati_pagamento")
public class StatoPagamento {
    public StatoPagamento() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stato_pagamento_id")
    private Long stato_pagamento_id;

    @Column(nullable = false, unique = true, length = 40)
    private String stato;

    @OneToMany(mappedBy = "stato")
    private List<Pagamento> pagamenti = new ArrayList<>();

    public Long getId() { return stato_pagamento_id; }
    public String getStato() { return stato; } public void setStato(String stato) { this.stato = stato; }
}
