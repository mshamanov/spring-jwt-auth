package dev.mash.jwtauth.service;

import dev.mash.jwtauth.entity.Role;
import dev.mash.jwtauth.entity.RoleType;
import dev.mash.jwtauth.entity.User;
import dev.mash.jwtauth.repository.RoleRepository;
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

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User getByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }

    public boolean existsByUserName(String userName) {
        return this.userRepository.existsByUsername(userName);
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        if (this.existsByUserName(user.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }

        if (this.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        List<RoleType> userRoleTypes = user.getRoles().stream().map(Role::getType).toList();
        List<Role> dbRoles = this.roleRepository.findRolesByTypeIn(userRoleTypes);

        if (dbRoles.isEmpty()) {
            throw new IllegalStateException("Roles list empty");
        }

        user.setRoles(dbRoles);

        return this.userRepository.save(user);
    }
}
