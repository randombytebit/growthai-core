package sd.growthai_core.repository;

import sd.growthai_core.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    // Method 1: Find chat session by session ID (most important for API calls)
    Optional<ChatSession> findBySessionId(String sessionId);

    // Method 2: Find all chats for a user (for chat history)
    @Query("SELECT cs FROM ChatSession cs WHERE cs.user.userId = :userId ORDER BY cs.lastMessageAt DESC NULLS LAST")
    List<ChatSession> findByUserIdOrderByLastMessageAtDesc(@Param("userId") Long userId);

    // Method 3: Find active chats for a user (for frontend chat list)
    @Query("SELECT cs FROM ChatSession cs WHERE cs.user.userId = :userId AND cs.isActive = true ORDER BY cs.lastMessageAt DESC NULLS LAST")
    List<ChatSession> findActiveChatsForUser(@Param("userId") Long userId);
}