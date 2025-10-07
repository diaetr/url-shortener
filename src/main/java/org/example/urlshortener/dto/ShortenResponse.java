package org.example.urlshortener.dto;

public class ShortenResponse {
    private String id;

    public ShortenResponse() {}
    public ShortenResponse(String id) { this.id = id; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
