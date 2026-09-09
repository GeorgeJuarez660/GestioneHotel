package it.rf.hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Indica che questa classe contiene configurazioni Spring (definisce dei Bean)
@EnableWebSecurity // Attiva Spring Security per l'applicazione
public class SecurityConfig {

    @Bean // Questo metodo produce un Bean gestito da Spring
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // Disabilita la protezione CSRF (utile per API stateless)
            
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Nessuna sessione salvata sul server: ogni richiesta è indipendente
            
            .authorizeHttpRequests(auth -> auth
                // Percorsi pubblici: accessibili senza autenticazione
                .requestMatchers("/hotel/**", "/css/**", "/js/**", "/img/**").permitAll()
                
                // Tutte le altre richieste richiedono l'autenticazione
                .anyRequest().authenticated()
            )
            
            .formLogin(Customizer.withDefaults()); // Abilita il login tramite form standard di Spring Security

        return http.build(); // Costruisce e restituisce la catena di filtri di sicurezza
    }
}
