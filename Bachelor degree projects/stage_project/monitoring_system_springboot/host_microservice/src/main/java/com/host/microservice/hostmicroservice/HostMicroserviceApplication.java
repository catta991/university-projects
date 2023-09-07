package com.host.microservice.hostmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HostMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HostMicroserviceApplication.class, args);
    }



    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
