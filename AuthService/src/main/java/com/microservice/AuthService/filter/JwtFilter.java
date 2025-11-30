package com.microservice.AuthService.filter;

import com.microservice.AuthService.service.CustomUserDetailsService;
import com.microservice.AuthService.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, java.io.IOException {

        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);
            String userName = jwtUtil.extractUsername(token);

            if(userName !=null && SecurityContextHolder.getContext().getAuthentication()==null){
//            need to check userName is same in UserDetails
                UserDetails userDetails = customUserDetailService.loadUserByUsername(userName);
                if(jwtUtil.validateToken(token)){
                    UsernamePasswordAuthenticationToken principle = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    //not important but adding request details for future purpose like logging or auditing
                    principle.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(principle);
                }
            }
        }

        chain.doFilter(req, res);
    }
}

