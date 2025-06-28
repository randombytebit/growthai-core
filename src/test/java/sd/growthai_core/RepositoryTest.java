package sd.growthai_core;

import sd.growthai_core.entity.User;
import sd.growthai_core.entity.ChatSession;
import sd.growthai_core.entity.UserMessage;
import sd.growthai_core.repository.UserRepository;
import sd.growthai_core.repository.ChatSessionRepository;
import sd.growthai_core.repository.UserMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional  // Rollback after each test
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Test
    public void testCreateSampleData() {
        System.out.println("🧪 Testing repository operations...");

        // Create user
        User user = User.builder()
                .username("testuser123")
                .email("test123@example.com")
                .displayName("Test User 123")
                .build();
        user = userRepository.save(user);

        assertNotNull(user.getUserId());
        System.out.println("✅ User created with ID: " + user.getUserId());

        // Create chat session
        ChatSession chat = ChatSession.builder()
                .user(user)
                .sessionId(UUID.randomUUID().toString())
                .chatTitle("Test Chat Session")
                .build();
        chat = chatSessionRepository.save(chat);

        assertNotNull(chat.getChatId());
        System.out.println("✅ Chat created with ID: " + chat.getChatId());

        // Create message
        UserMessage message = UserMessage.builder()
                .chatSession(chat)
                .content("Hello, this is a test message!")
                .messageOrder(1)
                .build();
        message = userMessageRepository.save(message);

        assertNotNull(message.getMessageId());
        System.out.println("✅ Message created with ID: " + message.getMessageId());

        // Verify counts
        long userCount = userRepository.count();
        long chatCount = chatSessionRepository.count();
        long messageCount = userMessageRepository.count();

        System.out.println(String.format(
                "📊 Final counts - Users: %d, Chats: %d, Messages: %d",
                userCount, chatCount, messageCount
        ));

        assertTrue(userCount >= 1);
        assertTrue(chatCount >= 1);
        assertTrue(messageCount >= 1);

        System.out.println("🎉 All tests passed!");
    }

    @Test
    public void testRepositoryMethods() {
        System.out.println("🧪 Testing repository query methods...");

        // Create test data
        User user = User.builder()
                .username("querytest")
                .email("query@test.com")
                .displayName("Query Test User")
                .build();
        user = userRepository.save(user);

        ChatSession chat = ChatSession.builder()
                .user(user)
                .sessionId("test-session-123")
                .chatTitle("Query Test Chat")
                .build();
        chat = chatSessionRepository.save(chat);

        // Test repository methods
        assertTrue(userRepository.existsByUsername("querytest"));
        assertTrue(chatSessionRepository.findBySessionId("test-session-123").isPresent());
        assertEquals(1, chatSessionRepository.findActiveChatsForUser(user.getUserId()).size());

        System.out.println("✅ Repository query methods working!");
    }
}