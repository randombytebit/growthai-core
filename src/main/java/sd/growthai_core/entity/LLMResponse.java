package sd.growthai_core.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Entity
@Table(name = "llm_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LLMResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;

    @OneToOne
    @JoinColumn(name = "user_message_id", nullable = false)
    private UserMessage userMessage;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // LLM-specific metadata
    @Column(name = "model_used")
    private String modelUsed;

    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    @Column(name = "token_count_input")
    private Integer tokenCountInput;

    @Column(name = "token_count_output")
    private Integer tokenCountOutput;

    @Column(name = "cost_usd")
    private Double costUsd; // API cost tracking

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "max_tokens")
    private Integer maxTokens;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ResponseStatus status = ResponseStatus.GENERATED;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "character_count")
    private Integer characterCount;

    @Column(name = "word_count")
    private Integer wordCount;

    @Column(name = "finish_reason")
    private String finishReason; // "stop", "length", "content_filter", etc.

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

    public enum ResponseStatus {
        GENERATED, FAILED, FILTERED, TRUNCATED, CANCELLED
    }

    // Helper methods
    public Long getChatId() {
        return userMessage != null && userMessage.getChatSession() != null ?
                userMessage.getChatSession().getChatId() : null;
    }

    public Long getUserId() {
        return userMessage != null ? userMessage.getUserId() : null;
    }

    public String getSessionId() {
        return userMessage != null ? userMessage.getSessionId() : null;
    }
}
