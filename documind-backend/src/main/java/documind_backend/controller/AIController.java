package documind_backend.controller;

import documind_backend.dto.ChatRequest;
import documind_backend.Model.Document;
import documind_backend.repository.DocumentRepository;
import documind_backend.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {
    @Autowired private AIService aiService;
    @Autowired private DocumentRepository documentRepository;

    @PostMapping("/ask/{docId}")
    public ResponseEntity<?> askQuestion(@PathVariable Long docId, @RequestBody ChatRequest request) {
        Document doc = documentRepository.findById(docId).orElseThrow();
        String answer = aiService.getAIResponse(doc.getContent(), request.getQuestion());
        return ResponseEntity.ok(Map.of("answer", answer));
    }
}