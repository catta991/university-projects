package com.oauth.implementation.dto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TokenDTO {

    private String accessToken;
    private String refreshToken;


    // metodo di supporto usato per generare i token JWT in caso di login natio dell'applicazione
    public ArrayList<String> generateJWTtoken(User user, String issuer) {
        ArrayList<String> tokens = new ArrayList<>();

        // indica il tipo di algoritmo utilizzato e il segreto usato per firmare il token
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());


        String access_token = JWT.create()
                // sto indicando un campo che identifica in modo univoco il token
                .withSubject(user.getUsername())
                // indico la validità del token
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60))
                .withIssuer(issuer)
                // aggiunto dei dettagli al token in questo caso alla voce roles avremmo
                // associati i ruoli
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);


        // creo il refresh token
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 200 * 60))
                .withIssuer(issuer)
                .sign(algorithm);

        tokens.add(access_token);
        tokens.add(refresh_token);

        return tokens;
    }

    // metodo di supporto usato per generare i token JWT in caso di login mediante OAuth2.0
    public ArrayList<String> generateJWTtokenOauth(DefaultOAuth2User user, String issuer) {
        ArrayList<String> tokens = new ArrayList<>();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());


        String access_token = JWT.create()
                // sto indicando un campo che identifica in modo univoco il token
                .withSubject(user.getName())
                // indico la validità del token
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60))
                .withIssuer(issuer)
                // aggiunto dei dettagli al token in questo caso alla voce roles avremmo
                // associati i ruoli
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);


        String refresh_token = JWT.create()
                .withSubject(user.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + 200 * 60))
                .withIssuer(issuer)
                .sign(algorithm);

        tokens.add(access_token);
        tokens.add(refresh_token);

        return tokens;
    }


    public ArrayList<String> generateJWTtokenKeycloack(String username, String issuer, Collection<GrantedAuthority> grantedAuthority) {

        ArrayList<String> tokens = new ArrayList<>();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());


        String access_token = JWT.create()
                // sto indicando un campo che identifica in modo univoco il token
                .withSubject(username)
                // indico la validità del token
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60))
                .withIssuer(issuer)
                // aggiunto dei dettagli al token in questo caso alla voce roles avremmo
                // associati i ruoli
                .withClaim("roles", grantedAuthority.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);


        String refresh_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 200 * 60))
                .withIssuer(issuer)
                .sign(algorithm);

        tokens.add(access_token);
        tokens.add(refresh_token);

        return tokens;
    }

}
