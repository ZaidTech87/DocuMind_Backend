package documind_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private String summary;
    private LocalDateTime uploadDate;

    // We send a snippet of the content rather than the full LONGTEXT
    // to save bandwidth on the dashboard list view.
    private String contentSnippet;

    public static DocumentDTO fromEntity(documind_backend.Model.Document doc) {
        return DocumentDTO.builder()
                .id(doc.getId())
                .fileName(doc.getFileName())
                .fileType(doc.getFileType())
                .summary(doc.getSummary())
                .uploadDate(doc.getUploadDate())
                .contentSnippet(doc.getContent() != null && doc.getContent().length() > 200
                        ? doc.getContent().substring(0, 200) + "..."
                        : doc.getContent())
                .build();
    }
}