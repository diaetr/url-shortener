package org.example.urlshortener.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.urlshortener.service.UrlService;
import org.example.urlshortener.model.ShortenRequest;
import org.example.urlshortener.model.ShortenResponse;

import java.net.URI;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService urlService;

    // Constructor injection for the service layer
    public UrlController(UrlService urlService){
        this.urlService = urlService;

    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shorten(@RequestBody ShortenRequest req) {
        // Basic input check
        if (req == null || req.getUrl() == null || req.getUrl().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "URL must not be blank"));
        }

        try {
            // Ask the service to create an ID for the URL
            String id = urlService.shorten(req.getUrl());

            // Build full short URL based on current host/port
            String shortUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/")
                    .path(id)
                    .toUriString();

            // response body includes both id and shortUrl
            ShortenResponse body = new ShortenResponse(id, shortUrl);

            // 201 Created + Location header pointing to "/{id}"
            return ResponseEntity.created(URI.create("/" + id)).body(body);

        } catch (IllegalArgumentException ex) {
            // Invalid URL → 400 with a small error message
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable String id) {
        // Look up the original URL by id
        String url = urlService.resolve(id);
        if (url == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // 302 redirect with Location header set to the original URL
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }
}
