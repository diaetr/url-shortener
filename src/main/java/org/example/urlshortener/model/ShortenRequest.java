package org.example.urlshortener.model;

public class ShortenRequest {

    // The original long URL that the client wants to shorten.
    private String url;

    // Empty constructor needed for JSON deserialization
    public ShortenRequest() {}

    // Constructor to quickly create a request with a given URL
    public ShortenRequest(String url) {
        this.url = url; 
    }

    // Getter for the URL
    public String getUrl() {
        return url; 
    }

    // Setter for the URL
    public void setUrl(String url) {
        this.url = url; 
    }
}
