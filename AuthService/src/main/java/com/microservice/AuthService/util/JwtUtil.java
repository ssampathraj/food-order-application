package com.microservice.AuthService.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // A securely generated secret key; load from an external source in production
    private  final String SECRET = "your_strong_secret_key_that_is_long_and_random";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String userName){

        Date currentDate = new Date();
        // Expiration time, e.g., 1 minute from now
        long expiration_Time = 1000 * 60 * 60;
        Date exp = new Date(System.currentTimeMillis() + expiration_Time);

        return Jwts.builder()
                .subject(userName)  // The user identity
                .issuedAt(currentDate)   // The time token was issued
                .signWith(key, Jwts.SIG.HS256)   // The token expiration time
                .expiration(exp) //
                .compact();   // Build the token string
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT expired");
        } catch (SecurityException e) {
            System.out.println("Invalid signature");
        } catch (Exception e) {
            System.out.println("Invalid Token");
        }
        return false;
    }
}
