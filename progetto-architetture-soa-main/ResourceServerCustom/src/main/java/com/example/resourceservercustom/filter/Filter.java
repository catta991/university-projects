package com.example.resourceservercustom.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
// a ogni richiesta viene eseguito questo filtro (grazie a OncePerRequestFilter)
public class Filter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // ottieni l'header chiamato Authorization
        String authorizationHeader = request.getHeader("Authorization");

        // se l'header esiste e il suo valore inizia con "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                // ottengo il token senza la scritta Bearer
                String token = authorizationHeader.substring("Bearer ".length());

                // inizializzo l'algoritmo e il segreto usato per generare il token
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();

                // verifico se il token è valido
                DecodedJWT decodedJWT = verifier.verify(token);

                // estraggo le informazioni dal token
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                // ottengo i ruoli e li trasformo in degli SimpleGrantedAuthority
                // in modo tale che siano comprensibili da spring
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                stream(roles).forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });


                // creao l'oggetto che mi server per autenticare l'utente (la password non serve perchè l'utente è già autenticato presso l'app monolitica)
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                // setta l'utente loggato nel security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                // prosegui nell'esecuzione della catena dei filtri
                filterChain.doFilter(request, response);
            } catch (Exception exception) {

                // se il token non è valido restituisci un errore
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }
}


