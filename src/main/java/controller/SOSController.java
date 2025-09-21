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
@RequestMapping("/api/sos")
public class SOSController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/find-help")
    @PreAuthorize("isAuthenticated()") // Only logged-in users can use this
    public ResponseEntity<String> findHelp(@RequestBody Map<String, Object> payload) {
        Double latitude = (Double) payload.get("latitude");
        Double longitude = (Double) payload.get("longitude");
        String helpType = (String) payload.get("helpType"); // e.g., "Hospital", "Police", "Embassy"

        String prompt = String.format(
            "Act as an emergency assistance operator. A traveler at latitude %f and longitude %f needs help. " +
            "Find the closest %s to their location. " +
            "Return the response as a single, valid JSON object. Do not include any text outside of the JSON object. " +
            "The JSON object must have three keys: 'name' (the name of the place), 'address' (the full address), and 'phone' (the primary contact number). " +
            "If you cannot find a specific result, provide the general emergency number for that country.",
            latitude, longitude, helpType
        );

        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBodyMap = Map.of(
            "contents", new Object[]{ Map.of("parts", new Object[]{ Map.of("text", prompt) }) }
        );

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBodyMap, headers);
            ResponseEntity<String> geminiResponse = restTemplate.postForEntity(apiUrl, request, String.class);

            JsonNode root = objectMapper.readTree(geminiResponse.getBody());
            String botReply = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            botReply = botReply.replace("```json", "").replace("```", "").trim();

            return ResponseEntity.ok(botReply);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("{\"error\": \"Could not retrieve emergency information.\"}");
        }
    }
}
