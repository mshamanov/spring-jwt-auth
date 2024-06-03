package dev.mash.jwtauth.repository;

import dev.mash.jwtauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Repository to manage {@link User} entities.
 *
 * @author Mikhail Shamanov
 */
@Service
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String userName);

    boolean existsByUsername(String userName);

    boolean existsByEmail(String email);
}
