package sd.growthai_core.repository;

import sd.growthai_core.entity.LLMResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LLMResponseRepository extends JpaRepository<LLMResponse, Long> {

    // Method 1: Find response by user message ID (to get AI response for a specific message)
    Optional<LLMResponse> findByUserMessageMessageId(Long messageId);

    // Method 2: Find all responses for a chat session (for complete conversation history)
    @Query("SELECT lr FROM LLMResponse lr WHERE lr.userMessage.chatSession.sessionId = :sessionId ORDER BY lr.createdAt ASC")
    List<LLMResponse> findBySessionIdOrderByCreatedAtAsc(@Param("sessionId") String sessionId);

    // Method 3: Find responses for multiple message IDs (for batch loading to avoid N+1 problem)
    @Query("SELECT lr FROM LLMResponse lr WHERE lr.userMessage.messageId IN :messageIds")
    List<LLMResponse> findByUserMessageMessageIdIn(@Param("messageIds") List<Long> messageIds);
}