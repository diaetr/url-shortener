package org.example.urlshortener.model; // same package as ShortenRequest for consistency

/**
 * ShortenResponse
 * ---------------
 * Represents the JSON body we return on success from POST /shorten.
 * Example response JSON:
 *   {
 *     "id": "aB93xY",
 *     "shortUrl": "http://localhost:8080/aB93xY"
 *   }
 *
 * Fields:
 * - id:      the generated short identifier (opaque to clients)
 * - shortUrl: the fully qualified short link clients can click/open
 *
 * This class is also minimal: fields + constructors + getters/setters.
 * No logic here; the service will construct and return this DTO.
 */

public class ShortenResponse {

    // The short identifier your service minted for the long URL.
    private String id;

    // The full short URL (base + "/" + id) that's convenient for clients to use.
    private String shortUrl;

    // Default no-args constructor for JSON serialization.
    public ShortenResponse() {}

    // Convenience constructor to populate both fields at once.
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
