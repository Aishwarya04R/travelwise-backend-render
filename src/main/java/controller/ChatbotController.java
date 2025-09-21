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
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/chat")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        // CORRECTED: Use the latest, correct Gemini model name
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBodyMap = Map.of(
            "contents", new Object[] {
                Map.of("parts", new Object[] {
                    Map.of("text", userMessage)
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
            return ResponseEntity.internalServerError().body("Sorry, I'm having trouble connecting to the AI service.");
        }
    }
}

