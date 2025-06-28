package sd.growthai_core.repository;

import sd.growthai_core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Method 1: Find user by username (for login/authentication)
    Optional<User> findByUsername(String username);

    // Method 2: Find user by email (for registration/password reset)
    Optional<User> findByEmail(String email);

    // Method 3: Check if username already exists (for registration validation)
    boolean existsByUsername(String username);
}