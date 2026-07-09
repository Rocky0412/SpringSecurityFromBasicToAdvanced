package org.example.springsecurity.Services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.websocket.OnClose;
import org.apache.el.parser.Token;
import org.example.springsecurity.Repositories.UserRepository;
import org.example.springsecurity.models.Users;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

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
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(
                JWT_SECRET.getBytes(StandardCharsets.UTF_8)
        );

    }
    private <T> T getClaimFromToken(
            String token,
            Function<Claims, T> claimsResolver
    ){
        Claims claims = parseToken(token);

        return claimsResolver.apply(claims);
    }


    private boolean isTokeExpired(String token) {
        /*
        * Check the Token is Expired or Not
        * Return True if expired or False
        * */
        Claims payload= parseToken(token);
        Date expiry = payload.getExpiration();
        //System.out.println("isTokeExpired: " + expiry + " and current time is "  + new Date());
        return expiry.before(new Date());
    }
    private Claims parseToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        System.out.println("Payload: " + claims);
        return claims;
    }

    private boolean isValidToken(String token) {

        return false;
    }
    @Override
    public void run(String @NonNull ... args) throws Exception {
        Map<String,Object> payload = new HashMap<>();
        payload.put("username","Rocky");
        payload.put("Email","rocky@gmail.com");
        payload.put("Role","USER");

        String token = generateToken(payload);
        System.out.println("Token: "+token);
        Claims payload1=parseToken(token);

//        Object username = payload1.get("username");
//        Object email = payload1.get("Email");
//        Object role = payload1.get("Role");
//        System.out.println("Username: "+username +" Email: "+email+" Role: "+role);




    }
}
