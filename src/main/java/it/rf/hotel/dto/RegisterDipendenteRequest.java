package it.rf.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class RegisterDipendenteRequest {

    @NotBlank(message = "Nome obbligatorio") @Size(max = 100)
    private String nome;
    @NotBlank(message = "Cognome obbligatorio") @Size(max = 100)
    private String cognome;
    @NotBlank(message = "Codice fiscale obbligatorio")
    @Pattern(regexp = "[A-Za-z0-9]{16}", message = "Codice fiscale non valido")
    private String codiceFiscale;
    @NotNull(message = "Data di nascita obbligatoria") @Past(message = "Data di nascita non valida")
    private LocalDate dataNascita;
    @NotBlank(message = "Username obbligatorio") @Size(min = 3, max = 50)
    private String username;
    @NotBlank(message = "Password obbligatoria") @Size(min = 8, max = 100)
    private String password;
    @NotBlank(message = "Lingua obbligatoria") @Size(max = 50)
    private String lingua;
    @NotBlank(message = "Categoria obbligatoria") @Size(max = 100)
    private String categoria;

    public RegisterDipendenteRequest() {
    }

    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; } public void setCognome(String cognome) { this.cognome = cognome; }
    public String getCodiceFiscale() { return codiceFiscale; } public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }
    public LocalDate getDataNascita() { return dataNascita; } public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; } public void setPassword(String password) { this.password = password; }
    public String getLingua() { return lingua; } public void setLingua(String lingua) { this.lingua = lingua; }
    public String getCategoria() { return categoria; } public void setCategoria(String categoria) { this.categoria = categoria; }
}
