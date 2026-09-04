package it.rf.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteReqRes {

    @NotBlank(message = "Username obbligatorio")
    @Size(max = 50, message = "Username troppo lungo")
    private String username;

    @NotBlank(message = "Password obbligatoria")
    @Size(min = 8, max = 100, message = "La password deve contenere da 8 a 100 caratteri")
    private String password;

    @NotBlank(message = "Codice fiscale obbligatorio")
    @Pattern(regexp = "[A-Za-z0-9]{16}", message = "Codice fiscale non valido")
    private String codiceFiscale;


    public ClienteReqRes() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodiceFiscale() { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }


}
