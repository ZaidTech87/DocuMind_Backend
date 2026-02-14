package documind_backend.repository;

import documind_backend.Model.Document;
import documind_backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByUser(User user);
}