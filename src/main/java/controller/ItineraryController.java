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
@RequestMapping("/api/itinerary")
public class ItineraryController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/generate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> generateItinerary(@RequestBody Map<String, String> payload) {
        String destination = payload.get("destination");
        String duration = payload.get("duration");

        String prompt = String.format(
            "Create a detailed travel itinerary for a %s-day trip to %s. " +
            "Return the response as a single, valid JSON object. Do not include any text outside of the JSON object. " +
            "The JSON object must have a key named 'itinerary' which is an array of objects. " +
            "Each object in the array represents a day and must have the following keys: 'day' (e.g., 'Day 1'), 'title' (a short, catchy title for the day's plan), 'description' (a detailed paragraph of activities), and 'imageQuery' (a simple, effective Google search query to find a representative image for that day's main activity, e.g., 'Eiffel Tower at night').",
            duration, destination
        );

        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBodyMap = Map.of(
            "contents", new Object[]{
                Map.of("parts", new Object[]{
                    Map.of("text", prompt)
                })
            }
        );

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBodyMap, headers);
            ResponseEntity<String> geminiResponse = restTemplate.postForEntity(apiUrl, request, String.class);

            JsonNode root = objectMapper.readTree(geminiResponse.getBody());
            String botReply = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

            // The AI sometimes wraps the JSON in markdown, so we clean it
            botReply = botReply.replace("```json", "").replace("```", "").trim();

            return ResponseEntity.ok(botReply);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("{\"error\": \"Failed to generate itinerary.\"}");
        }
    }
}
