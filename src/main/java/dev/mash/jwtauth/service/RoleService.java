package dev.mash.jwtauth.service;

import dev.mash.jwtauth.entity.Role;
import dev.mash.jwtauth.entity.RoleType;
import dev.mash.jwtauth.entity.User;
import dev.mash.jwtauth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage Roles {@link Role}.
 *
 * @author Mikhail Shamanov
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * Retrieves all the roles
     *
     * @return the list of roles
     */
    public List<Role> getAllRoles() {
        return this.roleRepository.findAll();
    }

    /**
     * Retrieves the roles for the specified user
     *
     * @param user the user to get the roles for
     * @return the list of the roles
     */
    public List<Role> getUserRoles(User user) {
        List<RoleType> userRoleTypes = user.getRoles().stream().map(Role::getType).toList();
        return this.roleRepository.findRolesByTypeIn(userRoleTypes);
    }
}
