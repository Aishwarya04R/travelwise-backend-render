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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dream")
public class DreamController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/generate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> generateDreamTrip(@RequestBody Map<String, String> payload) {
        String userPrompt = payload.get("prompt");
        String imageBase64 = payload.get("image");

        String prompt = String.format(
            "Analyze the user's text description and the provided image to suggest a dream vacation. " +
            "User's description: '%s'. " +
            "Return the response as a single, valid JSON object. Do not include any text outside of the JSON object. " +
            "The JSON object must have the following keys: " +
            "'suggestedLocation' (a specific city or region), " +
            "'vibe' (3-4 keywords describing the feeling of the trip), " +
            "'suggestedActivities' (an array of 3-4 specific activity suggestions), and " +
            "'whyItMatches' (a paragraph explaining why this suggestion matches the user's dream).",
            userPrompt
        );

        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> imagePart = Map.of("inlineData", Map.of(
            "mimeType", "image/jpeg",
            "data", imageBase64
        ));
        Map<String, Object> requestBodyMap = Map.of(
            "contents", new Object[]{ Map.of("parts", List.of(textPart, imagePart)) }
        );

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBodyMap, headers);
            ResponseEntity<String> geminiResponse = restTemplate.postForEntity(apiUrl, request, String.class);

            // This is the robust parsing logic
            JsonNode root = objectMapper.readTree(geminiResponse.getBody());
            String botReply = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            botReply = botReply.replace("```json", "").replace("```", "").trim();

            return ResponseEntity.ok(botReply);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("{\"error\": \"Failed to generate dream trip.\"}");
        }
    }
}

