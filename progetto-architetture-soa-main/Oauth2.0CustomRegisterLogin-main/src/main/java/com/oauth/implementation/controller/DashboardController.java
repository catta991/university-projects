package com.oauth.implementation.controller;

import com.oauth.implementation.dto.TokenDTO;
import com.oauth.implementation.model.UserDb;
import com.oauth.implementation.util.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oauth.implementation.repository.UserRepository;
import org.springframework.web.client.RestTemplate;

/*
 * controller per renderizzare la pagina controller.html
 */

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    TokenDTO tokenDTO;

    @Autowired
    RestTemplate restTemplate;


    @GetMapping
    public String displayDashboard(Model model) {


        try {

            SecurityContext securityContext = SecurityContextHolder.getContext();
            if (securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
                DefaultOAuth2User user = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
                model.addAttribute("userDetails", user.getAttribute("name") != null ? user.getAttribute("name") : user.getAttribute("login"));
            } else {

                User user = (User) securityContext.getAuthentication().getPrincipal();
                UserDb users = userRepo.findByEmail(user.getUsername());
                model.addAttribute("userDetails", users.getName());
            }

            model.addAttribute("accessToken", tokenDTO.getAccessToken());
            model.addAttribute("refreshToken", tokenDTO.getRefreshToken());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + tokenDTO.getAccessToken());

            HttpEntity<Void> httHeaders = new HttpEntity<>(headers);
            HttpEntity<JsonParser> resp = restTemplate.exchange("http://localhost:8082/user/resources", HttpMethod.GET, httHeaders, JsonParser.class);

            if (resp.getBody() != null) {

                model.addAttribute("message", resp.getBody().getMessage());
            } else
                model.addAttribute("message", "error in retrieving data");


            return "dashboard";

        } catch (Exception e) {

            model.addAttribute("message", "error in retrieving data");
            return "dashboard";

        }
    }

}
