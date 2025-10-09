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

@SpringBootTest
@AutoConfigureMockMvc
class UrlControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shorten_validUrl_thenRedirectWorks() throws Exception {
        // 1) Create a short link
        String longUrl = "https://example.com/path";
        MvcResult create = mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"" + longUrl + "\"}"))
                // Accept either 200 OK or 201 Created (depends on your controller)
                .andExpect(status().is2xxSuccessful())
                // Body must contain an "id" field
                .andExpect(jsonPath("$.id").isString())
                .andReturn();

        // 2) Extract the id from the JSON response
        String json = create.getResponse().getContentAsString();
        String id = new ObjectMapper().readTree(json).get("id").asText();

        // 3) Follow the short link -> expect 302 redirect to the original URL
        mockMvc.perform(get("/" + id))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", longUrl));
    }
    @Test
    void shorten_withInvalidUrl_returnsBadRequest() throws Exception {
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
        mockMvc.perform(get("/doesNotExist123"))
                .andExpect(status().isNotFound());
    }
}
