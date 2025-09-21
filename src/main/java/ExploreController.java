package com.mycompany.travelmanagementsystem.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/explore")
public class ExploreController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/local-tips")
    @PreAuthorize("isAuthenticated()") // Secure this endpoint
    public ResponseEntity<String> getLocalTips(@RequestBody Map<String, String> payload) {
        String locationName = payload.get("location");
        
        // This is a carefully crafted prompt for the AI
        String prompt = String.format(
            "Act as a local travel expert for %s. " +
            "Provide a brief, engaging guide for a tourist. " +
            "The guide must include the following three sections with clear headings in bold: " +
            "1. **Must-Try Cuisine**: List 3-4 specific local dishes with a brief, enticing description for each. " +
            "2. **What to Wear**: Suggest practical and culturally appropriate clothing for the current season. " +
            "3. **Festivals & Events**: Mention one or two famous local festivals or events, even if they are not happening right now, and briefly explain their significance. " +
            "Format the entire response using simple markdown.",
            locationName
        );

        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBodyMap = Map.of(
            "contents", new Object[] {
                Map.of("parts", new Object[] {
                    Map.of("text", prompt)
                })
            }
        );

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBodyMap, headers);
            ResponseEntity<String> geminiResponse = restTemplate.postForEntity(apiUrl, request, String.class);
            
            JsonNode root = objectMapper.readTree(geminiResponse.getBody());
            String botReply = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

            return ResponseEntity.ok(botReply);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Sorry, I'm having trouble generating local tips right now.");
        }
    }
}
