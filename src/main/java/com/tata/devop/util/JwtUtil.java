package com.tata.devop.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Autowired
    private Dotenv dotenv;

    public String getSecretKey() {
        String secretKey = dotenv.get("JWT_SECRET_KEY");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("JWT_SECRET cannot be null or empty");
        }
        return secretKey;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT Token: " + e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        String secret = this.getSecretKey();
        System.out.println(secret);
        Claims claims = Jwts.parser()
                .setSigningKey(this.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
