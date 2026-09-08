package it.rf.hotel.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "operatori_esterni")
public class OperatoreEsterno {
    public OperatoreEsterno() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long operatore_esterno_id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false, unique = true, length = 16)
    private String codiceFiscale;

    @Column(nullable = false)
    private LocalDate dataNascita;

    @Column(nullable = false)
    private String lingua;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false) 
    private String password;

    @Column(nullable = false) 
    private String codDipendenteEsterno;

    public Long getId() { return operatore_esterno_id; }
    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; } public void setCognome(String cognome) { this.cognome = cognome; }
    public String getCodiceFiscale() { return codiceFiscale; } public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }
    public LocalDate getDataNascita() { return dataNascita; } public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }
    public String getLingua() { return lingua; } public void setLingua(String lingua) { this.lingua = lingua; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; } public void setPassword(String password) { this.password = password; }
    public String getCodDipendenteEsterno() { return codDipendenteEsterno; }
    public void setCodDipendenteEsterno(String codDipendenteEsterno) { this.codDipendenteEsterno = codDipendenteEsterno; }
}
