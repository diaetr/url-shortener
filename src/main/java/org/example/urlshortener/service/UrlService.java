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
    private final SecureRandom random = new SecureRandom();

    /**
     * Shorten the given long URL and return the generated id.
     * - trims the input
     * - validates scheme/host
     * - generates a URL-safe base64 id
     * - collision-safe via putIfAbsent loop
     */

    public String shorten(String longUrl) {
        // 1) sanitize
        String trimmed = (longUrl == null) ? "" : longUrl.trim();
        validateUrl(trimmed);

        // 2) generate id and insert atomically to avoid rare races
        String id;
        do {
            byte[] bytes = new byte[6]; // 48 bits entropy
            random.nextBytes(bytes);

            // URL-safe base64 uses [A-Za-z0-9-_]; always 8 chars for 6 bytes → take 7 for a compact id
            id = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(bytes)
                    .substring(0, 7);
        } while (store.putIfAbsent(id, trimmed) != null);

        return id;
    }

    /**
     * Resolve an id to its original URL, or return null if unknown.
     * Controller can translate null to HTTP 404.
     */

    public String resolve(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return store.get(id);
    }
    /**
     * Minimal validation:
     * - non-blank
     * - scheme must be http or https
     * - host must exist
     */

    private void validateUrl(String s) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException("Invalid URL");
        }
        try {
            URL u = new URL(s);
            String protocol = u.getProtocol();
            if (!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
                throw new IllegalArgumentException("URL must start with http:// or https://");
            }
            if (u.getHost() == null || u.getHost().isBlank()) {
                throw new IllegalArgumentException("Invalid URL");
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }
}
