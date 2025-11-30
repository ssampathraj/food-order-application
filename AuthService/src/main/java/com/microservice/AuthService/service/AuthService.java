package com.microservice.AuthService.service;

import com.microservice.AuthService.entity.User;
import com.microservice.AuthService.repository.UserRepository;
import com.microservice.AuthService.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;// this bean we already created in customSecurityConfig class

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null) user.setRoles("USER");
        repo.save(user);
        return "User Registered Successfully";
    }

    public boolean isValid(String token){
       return jwtUtil.isTokenExpired(token);
    }
    public String getUserName(String token){
        return jwtUtil.extractUsername(token);
    }

    public String login(String username, String password) {

        try {
            // Authenticate using Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // If authentication succeeds, get the user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return jwtUtil.generateToken(userDetails.getUsername());

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        }

    }
}

