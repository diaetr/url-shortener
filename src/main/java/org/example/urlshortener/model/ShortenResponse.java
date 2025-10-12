package org.example.urlshortener.model; 

public class ShortenResponse {

    // The generated short ID for the long URL
    private String id;

    // The complete short URL (e.g., http://localhost:8080/def321).
    private String shortUrl;

    // Empty constructor for JSON deserialization
    public ShortenResponse() {}

    // Convenience constructor to set both fields at once.
    public ShortenResponse(String id, String shortUrl) {
        this.id = id;
        this.shortUrl = shortUrl;

    }

    // Getter for the short identifier.
    public String getId() { return id; }

    // Setter for the short identifier.
    public void setId(String id) { this.id = id; }

    // Getter for the full short URL.
    public String getShortUrl() { return shortUrl; }

    // Setter for the full short URL.
    public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }
}
