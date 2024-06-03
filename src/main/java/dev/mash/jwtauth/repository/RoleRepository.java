package dev.mash.jwtauth.repository;

import dev.mash.jwtauth.entity.Role;
import dev.mash.jwtauth.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Repository to manage {@link Role} entities.
 *
 * @author Mikhail Shamanov
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findRolesByTypeIn(Collection<RoleType> roleTypes);
}
