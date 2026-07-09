package org.example.springsecurity.Controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.example.springsecurity.DTOS.UserDTO;
import org.example.springsecurity.Services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@EnableWebSecurity
public class GreetingController {

    private final AuthenticationConfiguration configuration;
    private final JWTService jwtService;
    public GreetingController(AuthenticationConfiguration configuration, JWTService jwtService) {
        this.configuration = configuration;
        this.jwtService = jwtService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return ResponseEntity.ok("Hello " + name);
    }
    @GetMapping("/")
    public ResponseEntity<String> SayHello(@RequestParam(value="name", defaultValue="World") String name) {
        return ResponseEntity.ok("Hello " + name);
    }
    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody UserDTO userDTO, HttpServletResponse
                                         response) {

        /*
         * Check authentication
         */

        AuthenticationManager authenticationManager =
                configuration.getAuthenticationManager();
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", userDTO.getUsername());
            String token= jwtService.generateToken(claims);
            ResponseCookie cookies= ResponseCookie.from("JWT_TOKEN",token)
                    .httpOnly(true)
                    .secure(false)
                    .maxAge(3600)
                    .build();
            response.setHeader(HttpHeaders.SET_COOKIE, cookies.toString());

            return ResponseEntity.ok("Auth Successful " + token );
        }

        return new ResponseEntity<>("Wrong Credentials", HttpStatus.UNAUTHORIZED);
    }

}
