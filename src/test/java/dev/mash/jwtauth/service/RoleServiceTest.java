package dev.mash.jwtauth.service;

import dev.mash.jwtauth.entity.Role;
import dev.mash.jwtauth.entity.RoleType;
import dev.mash.jwtauth.entity.User;
import dev.mash.jwtauth.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleService roleService;

    @Test
    void getUserRoles_shouldReturnUserRoles() {
        User user = UserServiceTest.getUserList().getFirst();
        when(this.roleRepository.findRolesByTypeIn(user.getRoles()
                .stream()
                .map(Role::getType)
                .toList())).thenReturn(user.getRoles());

        List<Role> roles = this.roleService.getUserRoles(user);

        verify(this.roleRepository).findRolesByTypeIn(anyList());
        assertNotNull(roles);
        assertEquals(roles.size(), user.getRoles().size());
    }

    @Test
    void getAllRoles_shouldReturnAllRoles() {
        List<Role> roleList = RoleServiceTest.getRoleList();
        when(this.roleRepository.findAll()).thenReturn(roleList);

        List<Role> roles = this.roleService.getAllRoles();

        verify(this.roleRepository).findAll();
        assertEquals(roleList, roles);
    }

    public static List<Role> getRoleList() {
        return List.of(new Role(1L, RoleType.ROLE_USER), new Role(2L, RoleType.ROLE_ADMIN));
    }
}