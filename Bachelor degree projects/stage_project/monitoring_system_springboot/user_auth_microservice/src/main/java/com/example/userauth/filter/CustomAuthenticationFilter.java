package com.example.userauth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.userauth.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    // chiama il metodo loadByUserName e crea un oggetto User di spring in base ai parametri passati
    // e chiama il metodo authenticate che confronta i vari campi e se tutto va a buon fine
    // chiama  il metodo sotto
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("in attemptAuthentication");


        try {
            UserDto user = new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
            System.out.println(user);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            System.out.println("in exception");
            System.out.println(e.getMessage());
            throw new AuthenticationServiceException(e.getMessage(), e);
        }


    }


    // qui creo i token che verranno restituiti allo user
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        System.out.println("in successfulAuthentication");


        User user = (User) authentication.getPrincipal();


        System.out.println(user);

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        String access_token = JWT.create()
                // sto indicando un campo che identifica in modo univoco il token
                .withSubject(user.getUsername())
                // indico la validità del token
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                // aggiunto dei dettagli al token in questo caso alla voce roles avremmo
                // associati i ruoli
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        System.out.println("scadenza nuovo token "+new Date(System.currentTimeMillis() + 1000 * 60 * 1000));


        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 200 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");


        // inserisco i token nel body della response
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        //response.setStatus(OK.value());
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
