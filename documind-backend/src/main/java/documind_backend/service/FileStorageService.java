package documind_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    public String store(MultipartFile file) throws IOException {
        if (!Files.exists(root)) Files.createDirectories(root);

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), this.root.resolve(filename));
        return filename;
    }

    public void delete(String filename) throws IOException {
        Files.deleteIfExists(this.root.resolve(filename));
    }
}