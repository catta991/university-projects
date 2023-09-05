package com.oauth.implementation.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.oauth.implementation.dto.TokenDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.oauth.implementation.repository.UserRepository;
import com.oauth.implementation.service.DefaultUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {


    @Autowired
    UserRepository userRepo;

    @Autowired
    DefaultUserService userService;
    @Autowired
    TokenDTO tokenDTO;

    /*
     * Se l'autenticazione va a buon fine vengono recuperate le informazioni dell'utente
     * dallo spring security context e restituiti i token JWT
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;
        ArrayList<String> tokens = new ArrayList<>();


        // recupero le informazioni dell'utente loggato dallo spring security context per generare i jwt token

        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
            tokens = tokenDTO.generateJWTtokenOauth(userDetails, request.getRequestURL().toString());

        } else {

            User user = (User) authentication.getPrincipal();
            tokens = tokenDTO.generateJWTtoken(user, request.getRequestURL().toString());
        }

        tokenDTO.setAccessToken(tokens.get(0));
        tokenDTO.setRefreshToken(tokens.get(1));
        redirectUrl = "/dashboard";


        // fa il redirect alla pagina /dashboard
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }


}
