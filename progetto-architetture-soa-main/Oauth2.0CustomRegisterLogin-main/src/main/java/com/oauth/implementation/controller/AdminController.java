package com.oauth.implementation.controller;

import com.oauth.implementation.dto.TokenDTO;
import com.oauth.implementation.util.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/*
 * controller per renderizzare la pagina admin.html
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    TokenDTO tokenDTO;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping
    public String admin(Model model) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenDTO.getAccessToken());

        HttpEntity<Void> httHeaders = new HttpEntity<>(headers);

        try {
            HttpEntity<JsonParser> resp = restTemplate.exchange("http://localhost:8082/admin/resources", HttpMethod.GET, httHeaders, JsonParser.class);

            if (resp.getBody() != null) {
                model.addAttribute("message", resp.getBody().getMessage());
            } else
                model.addAttribute("message", resp.getBody().getMessage());
            return "admin";
        } catch (Exception e) {
            model.addAttribute("message", "error in retrieving data");
            return "admin";
        }
    }
}
