package documind_backend.service;

import documind_backend.Model.Document;
import documind_backend.Model.User;
import documind_backend.repository.DocumentRepository;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class DocumentService {
    @Autowired private DocumentRepository documentRepository;
    @Autowired private FileStorageService fileStorageService;

    public Document saveDocument(MultipartFile file, User user) throws Exception {
        // 1. Store file physically
        String fileName = fileStorageService.store(file);

        // 2. Extract text content using Tika
        Tika tika = new Tika();
        String extractedText = "";
        try {
            extractedText = tika.parseToString(file.getInputStream());
        } catch (IOException e) {
            extractedText = "Could not extract text from this file type.";
        }

        // 3. Save metadata to DB
        Document doc = Document.builder()
                .fileName(file.getOriginalFilename())
                .filePath("uploads/" + fileName)
                .fileType(file.getContentType())
                .content(extractedText)
                .user(user)
                .build();

        return documentRepository.save(doc);
    }
}