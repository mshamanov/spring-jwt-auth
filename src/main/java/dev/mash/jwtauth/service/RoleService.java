package dev.mash.jwtauth.service;

import dev.mash.jwtauth.entity.Role;
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

    public List<Role> getAllRoles() {
        return this.roleRepository.findAll();
    }
}
