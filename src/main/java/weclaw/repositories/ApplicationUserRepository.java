package weclaw.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import weclaw.domain.ApplicationUser;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsername(String username);
}