package org.example.springsecurity.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableWebSecurity
public class GreetingController {

    @GetMapping("/hello")
    public ResponseEntity<String> greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return ResponseEntity.ok("Hello " + name);
    }
    @GetMapping("/")
    public ResponseEntity<String> SayHello(@RequestParam(value="name", defaultValue="World") String name) {
        return ResponseEntity.ok("Hello " + name);
    }
}
