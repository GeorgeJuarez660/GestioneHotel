package it.rf.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginClienteRequest {

    @NotBlank(message = "Username obbligatorio")
    @Size(max = 50, message = "Username troppo lungo")
    private String username;

    @NotBlank(message = "Password obbligatoria")
    @Size(min = 8, max = 100, message = "La password deve contenere da 8 a 100 caratteri")
    private String password;

    public LoginClienteRequest() {
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
}
