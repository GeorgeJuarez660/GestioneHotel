package it.rf.hotel.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tipi_stanza")
public class TipoStanza {
    public TipoStanza() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long tipo_stanza_id;

    @Column(nullable = false, unique = true, length = 50)
    private String tipo;

    @Column(nullable = false)
    private String descrizione;

    @OneToMany(mappedBy = "tipo")
    private List<Stanza> stanze = new ArrayList<>();

    public Long getId() { return tipo_stanza_id; }
    public String getTipo() { return tipo; } public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescrizione() { return descrizione; } public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
}
