package it.rf.hotel.config;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import it.rf.hotel.model.Cliente;
import it.rf.hotel.model.Dipendente;
import it.rf.hotel.model.OperatoreEsterno;

@Component // Rende questa classe un Bean gestito da Spring, iniettabile altrove
public class JWTConfig {
    
    // Chiave segreta usata per firmare e verificare i token JWT (deve essere lunga almeno 32 caratteri per l'algoritmo HMAC-SHA)
    private final Key key = Keys.hmacShaKeyFor(
        "una-chiave-segreta-molto-lunga-almeno-32-caratteri".getBytes()
    );

    // Insieme (thread-safe) dei token invalidati manualmente, ad esempio dopo un logout
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    // Genera un token JWT per un Cliente
    public String generateTokenFromCliente(Cliente cliente) {
        return Jwts.builder()
                .subject(cliente.getUsername()) // "soggetto" del token: identifica l'utente
                .claim("clienteId", cliente.getId()) // dato extra inserito nel token: id del cliente
                .claim("clienteCF", cliente.getCodiceFiscale()) // dato extra: codice fiscale
                .issuedAt(new Date()) // data/ora di creazione del token
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // scadenza: 1 ora da adesso (in millisecondi)
                .signWith(key) // firma il token con la chiave segreta (garantisce autenticità e integrità)
                .compact(); // genera la stringa finale del token JWT
    }

    // Genera un token JWT per un Dipendente (stessa logica del metodo sopra, ma per un altro tipo di utente)
    public String generateTokenFromDipendente(Dipendente dipendente) {
        return Jwts.builder()
                .subject(dipendente.getUsername())
                .claim("dipendenteId", dipendente.getId())
                .claim("dipendenteCF", dipendente.getCodiceFiscale())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    public String generateTokenFromDipendente(OperatoreEsterno operatoreEsterno) {
        return Jwts.builder()
                .subject(operatoreEsterno.getUsername())
                .claim("dipendenteId", operatoreEsterno.getId())
                .claim("dipendenteCF", operatoreEsterno.getCodiceFiscale())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    // Controlla se un token è valido (firma corretta, non scaduto, non corrotto)
    public boolean isValid(String token) {
        try {
            parseClaims(token); // prova a leggere/validare il contenuto del token
            return true; // se non lancia eccezioni, il token è valido
        } catch (JwtException e) {
            return false; // se qualcosa va storto (firma errata, scaduto, ecc.), il token non è valido
        }
    }

    // Estrae e restituisce i "claims" (i dati/payload) contenuti nel token, verificandone la firma
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key) // usa la chiave per verificare che il token non sia stato manomesso
                .build()
                .parseSignedClaims(token) // effettua il parsing del token firmato
                .getPayload(); // restituisce il contenuto (i claims) del token
    }

    // Aggiunge un token alla blacklist, rendendolo di fatto "invalidato" (es. dopo il logout)
    public void invalidateToken(String token) {
        blacklist.add(token);
    }

}