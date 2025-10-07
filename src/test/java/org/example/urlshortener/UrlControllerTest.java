package org.example.urlshortener;

import org.example.urlshortener.dto.ShortenRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UrlControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;

    @Test
    void shorten_returnsId_forValidUrl() throws Exception {
        var req = new ShortenRequest("https://example.com/page");
        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString());
    }

    @Test
    void redirect_returns302_forExistingId() throws Exception {
        var create = mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ShortenRequest("https://example.org"))))
                .andExpect(status().isOk())
                .andReturn();

        String id = mapper.readTree(create.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(get("/" + id))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.org"));
    }
}
