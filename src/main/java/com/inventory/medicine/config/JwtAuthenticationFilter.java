package com.inventory.medicine.config;

import com.fasterxml.jackson.databind.ObjectMapper;  // Make sure to import ObjectMapper
import com.inventory.medicine.dto.error.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper; // Add ObjectMapper for serialization

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("request : "+request);

        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header "+authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Authorization Header came to if JWTAuthenticationFilter");
            filterChain.doFilter(request, response);
            System.out.println("Authorization Header passed filterChange doFilter method");

            return;
        }

        final String jwt = authorizationHeader.substring(7);
        System.out.println("jwt token substringed" + jwt);
        String userEmail = null;
        System.out.println("userEmail is :"+userEmail);

        try {
            System.out.println("email extraction started from jwt");

            userEmail = jwtService.extractEmail(jwt);
            System.out.println("email extracted from jwt");
        } catch (RuntimeException e) {
            System.out.println("can not extract email");
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error("Unauthorized")
                    .message("Invalid JWT token JWTAuthenticationFilter1")
                    .build();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            } catch (RuntimeException e) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .error("Unauthorized")
                        .message("User not found")
                        .build();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .error("Unauthorized")
                        .message("Invalid JWT token JWTAuthFillter2")
                        .build();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

