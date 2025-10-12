package org.example.urlshortener;

import org.springframework.boot.SpringApplication;                   // Boots the app
import org.springframework.boot.autoconfigure.SpringBootApplication; // Enables auto-config + component scan

@SpringBootApplication
public class UrlShortenerApplication {

    public static void main(String[] args) {
        // Start Spring Boot (creates context, starts web server)
        SpringApplication.run(UrlShortenerApplication.class, args);
    }

}
