package sd.growthai_core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sd.growthai_core.entity.UserMessage;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long>  {
    // Get messages for a chat session (most important)
    // Method 1: Using Spring Data JPA naming convention (no @Query needed)
    List<UserMessage> findByChatSessionSessionIdOrderByMessageOrderAsc(String sessionId);

    // Method 2: Using @Query annotation for custom queries
    @Query("SELECT um FROM UserMessage um WHERE um.chatSession.sessionId = :sessionId ORDER BY um.messageOrder ASC")
    List<UserMessage> findBySessionIdOrderByMessageOrderAsc(@Param("sessionId") String sessionId);

    // Method 3: Simple query methods
    List<UserMessage> findByStatus(UserMessage.MessageStatus status);

    // Method 4: Complex query with @Query
    @Query("SELECT COUNT(um) > 0 FROM UserMessage um WHERE um.chatSession.sessionId = :sessionId AND um.status = 'PROCESSING'")
    boolean hasProcessingMessages(@Param("sessionId") String sessionId);

    // Method 5: Find pending messages
    @Query("SELECT um FROM UserMessage um WHERE um.status = 'PENDING' ORDER BY um.createdAt ASC")
    List<UserMessage> findPendingMessages();

    // Method 6: Find by client message ID for deduplication
    Optional<UserMessage> findByClientMessageId(String clientMessageId);
}
