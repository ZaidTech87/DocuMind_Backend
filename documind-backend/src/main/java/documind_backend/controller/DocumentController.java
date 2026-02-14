package documind_backend.controller;

import documind_backend.Model.Document;
import documind_backend.Model.User;
import documind_backend.repository.DocumentRepository;
import documind_backend.repository.UserRepository;
import documind_backend.service.DocumentService;
import documind_backend.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:5173")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Upload a new document, extract text, and save to DB.
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            // Get current authenticated user
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Use DocumentService to handle storage and text extraction (Apache Tika)
            Document savedDoc = documentService.saveDocument(file, user);

            return ResponseEntity.ok(savedDoc);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Could not upload file: " + e.getMessage()));
        }
    }

    /**
     * Get all documents belonging to the authenticated user.
     */
    @GetMapping("/list")
    public ResponseEntity<List<Document>> getUserDocuments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        List<Document> docs = documentRepository.findAllByUser(user);
        return ResponseEntity.ok(docs);
    }

    /**
     * Get details of a specific document.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Long id) {
        return documentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a document from DB and local storage.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        try {
            Document doc = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document not found"));

            // Remove physical file
            fileStorageService.delete(doc.getFilePath().replace("uploads/", ""));

            // Remove DB record
            documentRepository.delete(doc);

            return ResponseEntity.ok(Map.of("message", "Document deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Deletion failed: " + e.getMessage()));
        }
    }
}