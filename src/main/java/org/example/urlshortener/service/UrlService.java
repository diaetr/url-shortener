package org.example.urlshortener.service;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlService {
    // Thread-safe in-memory store: id -> original URL
    private final Map<String, String> store = new ConcurrentHashMap<>();
    // For generating random IDs
    private final SecureRandom random = new SecureRandom();

    //Create a short id for a given long URL.
    public String shorten(String longUrl) {

        // Trim input and validate basic URL rules
        String trimmed = (longUrl == null) ? "" : longUrl.trim();
        validateUrl(trimmed);

        // Keep generating until we insert a unique id
        String id;
        do {
            byte[] bytes = new byte[6];    // 48 bits entropy
            random.nextBytes(bytes);

            // URL-safe Base64 -> [A-Za-z0-9-_]; no padding
            // For 6 bytes you get 8 chars; take first 7 for a compact ID
            id = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(bytes)
                    .substring(0, 7);
        } while (store.putIfAbsent(id, trimmed) != null);  // retry on rare collision

        return id;
    }

    /**
     * Resolve an id to its original URL.
     * Returns null if unknown (controller turns that into 404).
     */

    public String resolve(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return store.get(id);
    }
    /**
     * Minimal validation:
     * - not blank
     * - scheme must be http or https
     * - host must exist
     */

    private void validateUrl(String s) {
        // Quick guard: avoid null/empty inputs
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException("Invalid URL");
        }
        try {
            // Use Java's URL parser to check basic structure
            URL u = new URL(s);
            // Allow only web-safe schemes
            String protocol = u.getProtocol();
            if (!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
                throw new IllegalArgumentException("URL must start with http:// or https://");
            }

            // Require a hostname (e.g., example.com). Rejects cases like "http:///path"
            if (u.getHost() == null || u.getHost().isBlank()) {
                throw new IllegalArgumentException("Invalid URL");
            }
        } catch (MalformedURLException e) {
            // Parsing failed -> not a valid URL format
            throw new IllegalArgumentException("Invalid URL");
        }
    }
}
