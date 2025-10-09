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

    public UrlController(UrlService urlService){
        this.urlService = urlService;

    }
    /**
     * POST /shorten
     * - Valid: 201 Created, Location: /{id}, body: { id, shortUrl }
     * - Invalid: 400 Bad Request, body: { error: "..." }
     */

    @PostMapping("/shorten")
    public ResponseEntity<?> shorten(@RequestBody ShortenRequest req) {
        // basic null/blank guard (trim to avoid whitespace-only)
        if (req == null || req.getUrl() == null || req.getUrl().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "URL must not be blank"));
        }

        try {
            // delegate to service (may throw IllegalArgumentException)
            String id = urlService.shorten(req.getUrl());

            // build full short URL from current request context (no hardcoded host)
            String shortUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/")
                    .path(id)
                    .toUriString();

            // response body includes both id and shortUrl
            ShortenResponse body = new ShortenResponse(id, shortUrl);

            // 201 Created + Location header pointing to the short resource
            return ResponseEntity.created(URI.create("/" + id)).body(body);

        } catch (IllegalArgumentException ex) {
            // translate service validation errors to 400 with a tiny JSON error
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * GET /{id}
     * - Found: 302 Found with Location header to original URL
     * - Not found: 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Void> redirect(@PathVariable String id) {
        String url = urlService.resolve(id);
        if (url == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // cleaner builder: sets the Location header for us
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }
}
