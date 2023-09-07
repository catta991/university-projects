package com.monitoringsystemconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class MonitoringSystemConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringSystemConfigServerApplication.class, args);
    }

}
