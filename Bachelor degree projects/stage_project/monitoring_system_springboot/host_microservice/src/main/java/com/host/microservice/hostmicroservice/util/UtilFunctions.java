package com.host.microservice.hostmicroservice.util;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


@NoArgsConstructor
@Component
public class UtilFunctions {


    public HttpHeaders getHeaders(String auth){


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", auth);

        return  headers;

    }


    public HttpHeaders getHeadersWithETag(String auth, String etag){

        HttpHeaders headers = getHeaders(auth);
        headers.add("If-Match", etag);

        return headers;
    }
}
