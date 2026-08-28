package it.rf.hotel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categorie_dipendente")
public class CategoriaDipendente {
    public CategoriaDipendente() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long cat_id;

    @Column(nullable = false, unique = true, length = 50)
    private String tipo;

    /*@OneToMany(mappedBy = "categoria")
    private List<Dipendente> dipendenti = new ArrayList<>();*/

    public Long getId() { return cat_id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    //public List<Dipendente> getDipendenti() { return dipendenti; }
}
