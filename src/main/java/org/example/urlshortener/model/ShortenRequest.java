package org.example.urlshortener.model;     // package to keep related model classes together

/**
 * ShortenRequest
 * --------------
 * Represents the JSON body the client sends to POST /shorten.
 * Expected JSON shape:
 *   { "url": "https://example.com/long/path" }
 * This class is intentionally minimal:
 * - one field: url
 * - default constructor (needed by Jackson for JSON → object)
 * - all-args constructor (useful for tests)
 * - getters/setters so Spring/Jackson can read/write the field
 * Validation (non-empty, http/https, etc.) will be handled in the service layer,
 * not here, to keep this DTO simple.
 */

public class ShortenRequest {

    // The long URL that the client wants to shorten.
    private String url;

    // Default no-args constructor: required for JSON deserialization.
    // Jackson creates an empty object first, then sets fields via setters
    public ShortenRequest() {}

    // Convenience constructor: handy in unit tests or when constructing by hand.
    public ShortenRequest(String url) { this.url = url; }

    // Getter so frameworks can read the URL value.
    public String getUrl() { return url; }

    // Setter so JSON deserialization can populate the url field.
    public void setUrl(String url) { this.url = url; }
}
