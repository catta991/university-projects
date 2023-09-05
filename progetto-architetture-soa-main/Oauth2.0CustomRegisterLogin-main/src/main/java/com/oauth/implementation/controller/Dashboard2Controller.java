package com.oauth.implementation.controller;


import com.oauth.implementation.dto.TokenDTO;
import com.oauth.implementation.model.UserDb;
import com.oauth.implementation.repository.UserRepository;
import com.oauth.implementation.util.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/dashboardDue")
public class Dashboard2Controller {


    //@Autowired
    //UserRepository userRepo;

    @Autowired
    TokenDTO tokenDTO;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping
    public String dashboardDue(Model model) {


        try {

            // recupero le informazioni dell'utente loggato dallo spring security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // di queste informazioni prendo solo il nome
            String currentPrincipalName = authentication.getName();


            // inserisco il nome e i token nel html
            model.addAttribute("userDetails", currentPrincipalName);
            model.addAttribute("accessToken", tokenDTO.getAccessToken());
            model.addAttribute("refreshToken", tokenDTO.getRefreshToken());


            // creo gli header contenenti il token di accesso per la richiesta al custom resource server
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + tokenDTO.getAccessToken());

            HttpEntity<Void> httpHeaders = new HttpEntity<>(headers);

            // invio la richiesta al custom resource server e salvo la risposta
            HttpEntity<JsonParser> resp = restTemplate.exchange("http://localhost:8082/user/resources", HttpMethod.GET, httpHeaders, JsonParser.class);

            if (resp.getBody() != null) {
                model.addAttribute("message", resp.getBody().getMessage());
            } else
                model.addAttribute("message", "error in retrieving data");


            return "dashboardDue";

        } catch (Exception e) {

            model.addAttribute("message", "error in retrieving data");
            return "dashboardDue";

        }
    }

}

