package sd.growthai_core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    // Foreign key to ChatSession
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatSession chatSession;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "message_order") // Order within the chat session
    private Integer messageOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageStatus status = MessageStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageType messageType = MessageType.TEXT;

    // Metadata
    @Column(name = "client_message_id") // For client-side deduplication
    private String clientMessageId;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "character_count")
    private Integer characterCount;

    @Column(name = "word_count")
    private Integer wordCount;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (content != null) {
            characterCount = content.length();
            wordCount = content.trim().split("\\s+").length;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum MessageStatus {
        PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
    }

    public enum MessageType {
        TEXT, IMAGE, FILE, AUDIO, VIDEO, SYSTEM
    }

    // Helper methods
    public Long getUserId() {
        return chatSession != null && chatSession.getUser() != null ?
                chatSession.getUser().getUserId() : null;
    }

    public String getSessionId() {
        return chatSession != null ? chatSession.getSessionId() : null;
    }
}