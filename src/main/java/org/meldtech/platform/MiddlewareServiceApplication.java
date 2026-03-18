package org.meldtech.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class MiddlewareServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiddlewareServiceApplication.class, args);
    }

}
