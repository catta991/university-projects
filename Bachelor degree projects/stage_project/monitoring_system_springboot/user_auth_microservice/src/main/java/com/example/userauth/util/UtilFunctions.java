package com.example.userauth.util;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Component
public class UtilFunctions {

    public HttpHeaders getCheckMkAuthHeader(String auth) {

        // da cambiare
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", auth);

        return headers;

    }

    public HttpHeaders getCheckMkAuthHeaderNewPassword(String auth, String newPassword){

        String headersPart[] = auth.split(" ");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer "+headersPart[1]+" "+ newPassword);

        return headers;
    }

    public String getFullName(String name, String surname) {
        return name + " " + surname;
    }


}