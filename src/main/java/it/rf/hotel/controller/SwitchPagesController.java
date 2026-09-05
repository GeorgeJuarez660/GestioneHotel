package it.rf.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("hotel")
public class SwitchPagesController {

    @GetMapping("/")
    public String start() {
        return "dashboard";
    }

    @GetMapping("/choose-register-login")
    public String choose() {
        return "chooseRegisterLogin";
    }

    @GetMapping("/register-cliente")
    public String registerCliente() {
        return "register-cliente";
    }

    @GetMapping("/register-dipendente")
    public String registerDipendente() {
        return "register-dipendente";
    }

    @GetMapping("/login-cliente")
    public String loginCliente() {
        return "login-cliente";
    }

    @GetMapping("/login-dipendente")
    public String loginDipendente() {
        return "login-dipendente";
    }

    @GetMapping("/homepage/cliente/")
    public String homepageCliente() {
        return "homepageCliente";
    }

    @GetMapping("/homepage/dipendente/")
    public String homepageDipendente() {
        return "homepageDipendente";
    }

    @GetMapping("/logout/")
    public String logout() {
        return "chooseRegisterLogin";
    }

    //------PRENOTAZIONE, STANZE, FEEDBACK E SERVIZIO NAVETTA, GUIDA E PISCINA

    @GetMapping("/cliente/prenotazione")
    public String prenotazione() {
        return "bookStanzeAndNavetta";
    }

    @GetMapping("/cliente/feedback")
    public String feedback() {
        return "insertFeedback";
    }

    @GetMapping("/dipendente/prenotazioni")
    public String prenotazioniDipendente() {
        return "prenotazione";
    }

    @GetMapping("/dipendente/stanze")
    public String stanzeDipendente() {
        return "setStanze";
    }

    @GetMapping("/dipendente/navetta")
    public String navettaDipendente() {
        return "setServizioNavetta";
    }

    @GetMapping("/dipendente/pacchetti")
    public String pacchettiDipendente() {
        return "insertPacchetti";
    }

    @GetMapping("/cliente/miei-feedback")
    public String feedbackCliente() {
        return "showFeedbackCliente";
    }

    @GetMapping("/dipendente/feedback")
    public String feedbackDipendente() {
        return "showFeedbackDipendente";
    }

    @GetMapping("/dipendente/modifica-stanza")
    public String modificaStanzaDipendente() {
        return "updateStanza";
    }

    @GetMapping("/dipendente/modifica-navetta")
    public String modificaNavettaDipendente() {
        return "updateNavetta";
    }

    @GetMapping("/dipendente/modifica-pacchetto")
    public String modificaPacchettoDipendente() {
        return "updatePacchetto";
    }

    @GetMapping("/dipendente/modifica-prenotazione")
    public String modificaPrenotazioneDipendente() {
        return "updatePrenotazione";
    }

    @GetMapping("/dipendente/guida")
    public String guidaDipendente() {
        return "setServizioGuida";
    }

    @GetMapping("/dipendente/piscina")
    public String piscinaDipendente() {
        return "setServizioPiscina";
    }

    @GetMapping("/dipendente/modifica-guida")
    public String modificaGuidaDipendente() {
        return "updateGuida";
    }

    @GetMapping("/dipendente/modifica-piscina")
    public String modificaPiscinaDipendente() {
        return "updatePiscina";
    }
}
