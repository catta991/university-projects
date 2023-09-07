package com.example.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


// funziona una volta che ho generato un token con il servizio userAuth posso usare come
// base questo servizio per fare da validatore del token e poi se la chiamata è possibili
// reindirizzarla verso il micro servizio corretto
@SpringBootApplication
public class AuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }



    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}


