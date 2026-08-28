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

@Component
public class JWTConfig {
    
    private final Key key = Keys.hmacShaKeyFor(
        "una-chiave-segreta-molto-lunga-almeno-32-caratteri".getBytes()
    );

    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public String generateTokenFromCliente(Cliente cliente) {
        return Jwts.builder()
                .subject(cliente.getUsername())
                .claim("clienteId", cliente.getId())
                .claim("clienteCF", cliente.getCodiceFiscale())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

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

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }


    public Claims parseClaims(String token) {
        return Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token).getPayload();
    }


    public void invalidateToken(String token) {
        blacklist.add(token);
    }

}
