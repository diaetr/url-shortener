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
    private final Map<String, String> store = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String shorten(String longUrl) {
        validateUrl(longUrl);
        String id;
        do {
            byte[] bytes = new byte[6];
            random.nextBytes(bytes);
            id = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(bytes)
                    .substring(0, 7);
        } while (store.containsKey(id));
        store.put(id, longUrl);
        return id;
    }

    public String resolve(String id) {
        return store.get(id);
    }

    private void validateUrl(String s) {
        try {
            URL u = new URL(s);
            if (u.getProtocol() == null || u.getHost() == null) {
                throw new IllegalArgumentException("Invalid URL");
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }
}
