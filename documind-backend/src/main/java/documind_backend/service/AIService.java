package documind_backend.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class AIService {
    @Value("${google.gemini.api.key}")
    private String apiKey;

    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=";

    public String getAIResponse(String context, String question) {
        RestTemplate restTemplate = new RestTemplate();
        String prompt = "Based on the following text: " + context + "\n\nQuestion: " + question;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );

        try {
            Map<String, Object> response = restTemplate.postForEntity(GEMINI_URL + apiKey, requestBody, Map.class).getBody();
            // Simplified parsing of Gemini JSON structure
            List candidates = (List) response.get("candidates");
            Map firstCandidate = (Map) candidates.get(0);
            Map content = (Map) firstCandidate.get("content");
            List parts = (List) content.get("parts");
            return (String) ((Map) parts.get(0)).get("text");
        } catch (Exception e) {
            return "AI Error: Unable to process request.";
        }
    }
}