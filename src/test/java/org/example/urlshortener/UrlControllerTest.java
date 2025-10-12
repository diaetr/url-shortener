package org.example.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Loads the Spring context and wires a MockMvc for HTTP-style tests
@SpringBootTest
@AutoConfigureMockMvc
class UrlControllerTest {

    @Autowired
    MockMvc mockMvc; // lets us call endpoints without a real server

    @Test
    void shorten_validUrl_thenRedirectWorks() throws Exception {
        // 1) Call POST /shorten with a valid URL
        String longUrl = "https://example.com/path";
        MvcResult create = mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"" + longUrl + "\"}"))
                // Accept 201 
                .andExpect(status().is2xxSuccessful())
                // Response JSON should contain an "id" string
                .andExpect(jsonPath("$.id").isString())
                .andReturn();

        // 2) Extract the id from the JSON response
        String json = create.getResponse().getContentAsString();
        String id = new ObjectMapper().readTree(json).get("id").asText();

        // 3) GET /{id} should issue a redirect to the original URL
        mockMvc.perform(get("/" + id))
                .andExpect(status().isFound())             // 302
                .andExpect(header().string("Location", longUrl)); // target matches what we sent
    }
    @Test
    void shorten_withInvalidUrl_returnsBadRequest() throws Exception {
        // Bad URL should be rejected
        String body = """
            { "url": "not-a-url" }
        """;

        mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void redirect_withUnknownId_returnsNotFound() throws Exception {
        // Unknown short id -> 404
        mockMvc.perform(get("/doesNotExist123"))
                .andExpect(status().isNotFound());
    }
}
