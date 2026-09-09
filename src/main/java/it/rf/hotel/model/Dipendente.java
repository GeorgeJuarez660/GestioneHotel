package it.rf.hotel.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dipendenti")
public class Dipendente {
    public Dipendente() {
    }

    @Id
    @Column 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dipendente_id;

    @Column(nullable = false) 
    private String nome;

    @Column(nullable = false) 
    private String cognome;

    @Column(nullable = false, unique = true, length = 16) 
    private String codiceFiscale;

    @Column(nullable = false)
    private LocalDate dataNascita;

    @Column(nullable = false, unique = true)
     private String username;

    @Column(nullable = false) 
    private String password;

    @Column(nullable = false) 
    private String lingua;

    @Column(nullable = false, unique = true)
    private String codDipendente;

    @ManyToOne(optional = false) 
    @JoinColumn(name = "cat_id")
    private CategoriaDipendente categoria;


    public Long getId() { return dipendente_id; }
    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; } public void setCognome(String cognome) { this.cognome = cognome; }
    public String getCodiceFiscale() { return codiceFiscale; } public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }
    public LocalDate getDataNascita() { return dataNascita; } public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; } public void setPassword(String password) { this.password = password; }
    public String getLingua() { return lingua; } public void setLingua(String lingua) { this.lingua = lingua; }
    public String getCodDipendente() { return codDipendente; } public void setCodDipendente(String codDipendente) { this.codDipendente = codDipendente; }
    public CategoriaDipendente getCategoria() { return categoria; }
    public void setCategoria(CategoriaDipendente categoria) { this.categoria = categoria; }
}
