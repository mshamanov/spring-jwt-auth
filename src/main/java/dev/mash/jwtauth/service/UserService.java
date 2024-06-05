package dev.mash.jwtauth.service;

import dev.mash.jwtauth.entity.Role;
import dev.mash.jwtauth.entity.User;
import dev.mash.jwtauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service to manage Users {@link User}.
 *
 * @author Mikhail Shamanov
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleService roleService;
    private final UserRepository userRepository;

    /**
     * Retrieves all the users
     *
     * @return the list of all users
     */
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    /**
     * Retrieves the user by the specified username
     *
     * @param username the name of the user to look for
     * @return the user with the specified username
     * @throws UsernameNotFoundException if the user not found
     */
    public User getByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }

    /**
     * Checks whether the user with the specified username exists
     *
     * @param username the name of the user to check against
     * @return true if the user exists, otherwise false
     */
    public boolean existsByUserName(String username) {
        return this.userRepository.existsByUsername(username);
    }

    /**
     * Checks whether the user with the specified email address exists
     *
     * @param email the email of the user to check against
     * @return true if the user exists, otherwise false
     */
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    /**
     * Creates a new user unless it already exists
     *
     * @param user the user to create
     * @throws IllegalStateException if the user already exists or the roles for the user are not assigned
     */
    @Transactional
    public void createUser(User user) throws IllegalStateException {
        if (this.existsByUserName(user.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }

        if (this.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        List<Role> dbRoles = this.roleService.getUserRoles(user);

        if (dbRoles.isEmpty()) {
            throw new IllegalStateException("Roles list empty");
        }

        user.setRoles(dbRoles);

        this.userRepository.save(user);
    }
}
