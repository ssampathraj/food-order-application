package com.microservice.AuthService.controller;
import com.microservice.AuthService.entity.User;
import com.microservice.AuthService.service.AuthService;
import com.microservice.AuthService.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        return authService.login(username, password);
    }
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestParam String token) {
        try {
            boolean ok = !authService.isValid(token);
            String user = authService.getUserName(token);
            return ResponseEntity.ok().body(java.util.Map.of("valid", ok, "username", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("valid", false));
        }
    }

}

