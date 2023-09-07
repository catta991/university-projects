package com.example.authorization.utils;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UtilFunction {

    public String getSecurityContextUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public HttpHeaders getCheckMkAuthHeader(String password) {

        String username = getSecurityContextUsername();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + username + " " + password);



        return headers;

    }

    public HttpHeaders getAuthHeader(String refreshToken){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Auth", refreshToken);

        return  headers;
    }

    public HttpHeaders getCheckMkAuthHeaderWithRole(String password) {


        String username = getSecurityContextUsername();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + username + " " + password);

        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")))
            headers.add("isSuperadmin", "true");
        else
            headers.add("isSuperadmin", "false");


        return headers;

    }


}
