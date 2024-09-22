package com.tata.devop.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final Dotenv dotenv;

    public AuthService(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    public String authenticate(String username, String password) {
        if ("testuser".equals(username) && "testpassword".equals(password)) {
            return generateToken(username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    // Genera el token JWT
    public String generateToken(String username) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", "ROLE_USER");  // Aquí puedes añadir más claims si es necesario.
            return createToken(claims, username);
        } catch (Exception e) {
            System.err.println("Error generating token: " + e.getMessage());
            throw new RuntimeException("Failed to generate token", e);
        }
    }
    private String getSecretKey() {
        return dotenv.get("JWT_SECRET_KEY", "defaultSecretKey");
    }
    // Crea el token JWT usando la clave secreta y expiración
    private String createToken(Map<String, Object> claims, String subject) {
        long expirationTime = 1000 * 60 * 60 * 10L;  // 10 horas por defecto

        try {
            String expirationString = dotenv.get("JWT_EXPIRATION");
            if (expirationString != null) {
                expirationTime = Long.parseLong(expirationString);
            }
        } catch (NumberFormatException e) {
            System.err.println("JWT_EXPIRATION no es un número válido. Se usará el valor por defecto.");
        }

        String secretKey = dotenv.get("JWT_SECRET_KEY", "defaultSecretKey");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(new Date().getTime() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, getSecretKey())
                .compact();
    }

    // Valida el token JWT
    public Boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET_KEY", "defaultSecretKey").getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            System.err.println("Error validating token: " + e.getMessage());
            return false;
        }
    }
}
