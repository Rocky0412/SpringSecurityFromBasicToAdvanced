package org.example.springsecurity.Services;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService implements CommandLineRunner {

    @Value("${jwt.secret}")
    public String JWT_SECRET ;
    @Value("${jwt.expiry}")
    public int JWT_EXPIRY ;

    public String generateToken(Map<String, Object> payload) {
        /*
          This method will create brand-new token based on payload

         String token = Jwts.builder()
        .subject("rocky") ->name
        .claim("role", "ADMIN") ->payload
        .issuedAt(new Date()) -> issue time
        .expiration(new Date(System.currentTimeMillis() + 60000)) ->expiry
        .signWith(key) ->key with secrete
        .compact();

         */
        SecretKey key = Keys.hmacShaKeyFor(
                JWT_SECRET.getBytes(StandardCharsets.UTF_8)
        );
        return Jwts.builder()
                .subject("Rocky")
                .claims(payload)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+JWT_EXPIRY))
                .signWith(key)
                .compact();
    }
    @Override
    public void run(String... args) throws Exception {
        Map<String,Object> payload = new HashMap<>();
        payload.put("username","Rocky");
        payload.put("Email","rocky@gmail.com");

        String token = generateToken(payload);
        System.out.println("Token: "+token);
    }
}
