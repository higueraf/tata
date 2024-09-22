package com.tata.devop;

import com.tata.devop.util.JwtUtil;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    @Mock
    private Dotenv dotenv;

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String secretKey = "6d4800f37065e1a566799575e77b6fa90b3af42c9bb14c6d0edacbdb9a8c157562da01794f2860cb180a426c891914abfe8bb0a84b266e6ab79b4691655ccdd9";
    private final String validToken;
    private final String username = "testuser";

    public JwtUtilTest() {
        validToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10 horas
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(dotenv.get("JWT_SECRET_KEY")).thenReturn(secretKey);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(username);
        assertNotNull(token);
        assertTrue(token.startsWith("eyJhbGciOiJIUzI1NiJ9"));  // Check for JWT structure
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = validToken + "invalidPart";
        boolean isValid = jwtUtil.validateToken(invalidToken);
        assertFalse(isValid);
    }


}
