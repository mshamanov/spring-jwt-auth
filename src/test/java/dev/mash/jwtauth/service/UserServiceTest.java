package dev.mash.jwtauth.service;

import dev.mash.jwtauth.entity.Role;
import dev.mash.jwtauth.entity.RoleType;
import dev.mash.jwtauth.entity.User;
import dev.mash.jwtauth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleService roleService;

    @InjectMocks
    UserService userService;

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        List<User> userList = UserServiceTest.getUserList();
        when(this.userRepository.findAll()).thenReturn(userList);

        List<User> allUsers = this.userService.getAllUsers();

        verify(this.userRepository).findAll();
        assertNotNull(allUsers);
        assertEquals(userList.size(), allUsers.size());
        assertTrue(allUsers.containsAll(userList));
    }

    @MethodSource("getUserList")
    @ParameterizedTest
    void getByUsername_shouldReturnUserByUsername(User user) {
        when(this.userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User retrievedUser = this.userService.getByUsername(user.getUsername());

        verify(this.userRepository).findByUsername(user.getUsername());
        assertNotNull(retrievedUser);
        assertEquals(user, retrievedUser);
    }

    @Test
    void getByUsername_shouldThrowExceptionIfUserNotExists() {
        when(this.userRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> this.userService.getByUsername("user"));
    }

    @Test
    void existsByUserName_shouldReturnTrueIfUserExists() {
        List<User> userList = UserServiceTest.getUserList();
        User user = userList.getFirst();
        when(this.userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        boolean userExists = this.userService.existsByUserName(user.getUsername());

        verify(this.userRepository).existsByUsername(user.getUsername());
        assertTrue(userExists);
    }

    @Test
    void existsByUserName_shouldReturnFalseIfUserNotExists() {
        List<User> userList = UserServiceTest.getUserList();
        User user = userList.getFirst();
        when(this.userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        boolean userExists = this.userService.existsByUserName(user.getUsername());

        verify(this.userRepository).existsByUsername(user.getUsername());
        assertFalse(userExists);
    }

    @Test
    void existsByEmail_shouldReturnTrueIfUserExists() {
        List<User> userList = UserServiceTest.getUserList();
        User user = userList.getFirst();
        when(this.userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        boolean userExists = this.userService.existsByEmail(user.getEmail());

        verify(this.userRepository).existsByEmail(user.getEmail());
        assertTrue(userExists);
    }

    @Test
    void existsByEmail_shouldReturnFalseIfUserNotExists() {
        List<User> userList = UserServiceTest.getUserList();
        User user = userList.getFirst();
        when(this.userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        boolean userExists = this.userService.existsByEmail(user.getEmail());

        verify(this.userRepository).existsByEmail(user.getEmail());
        assertFalse(userExists);
    }

    @Test
    void createUser_shouldCreateUserIfUserNotExists() {
        List<User> userList = UserServiceTest.getUserList();
        User user = userList.getFirst();
        when(this.userRepository.save(user)).thenReturn(user);
        when(this.roleService.getUserRoles(user)).thenReturn(user.getRoles());

        this.userService.createUser(user);

        verify(this.userRepository).save(user);
    }

    @Test
    void createUser_shouldThrowExceptionIfUserExistsByUsername() {
        List<User> userList = UserServiceTest.getUserList();
        User user = userList.getFirst();
        when(this.userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> this.userService.createUser(user));
    }

    @Test
    void createUser_shouldThrowExceptionIfUserExistsByEmail() {
        List<User> userList = UserServiceTest.getUserList();
        User user = userList.getFirst();
        when(this.userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> this.userService.createUser(user));
    }

    @Test
    void createUser_shouldThrowExceptionIfUserRolesNotAssigned() {
        List<User> userList = UserServiceTest.getUserList();
        User user = userList.getFirst();
        when(this.roleService.getUserRoles(user)).thenReturn(Collections.emptyList());

        assertThrows(IllegalStateException.class, () -> this.userService.createUser(user));
    }

    public static List<User> getUserList() {
        return List.of(
                new User(
                        1L,
                        "user",
                        "user@example.com",
                        "$2y$10$u7XrM8iGgdD3E4VtH.wikOQIhcFEsB0mCQpdfBNzLS./xOmXgdvU6",
                        List.of(new Role(1L, RoleType.ROLE_USER))
                ),
                new User(
                        2L,
                        "admin",
                        "admin@example.com",
                        "$2y$10$u7XrM8iGgdD3E4VtH.wikOQIhcFEsB0mCQpdfBNzLS./xOmXgdvU6",
                        List.of(new Role(1L, RoleType.ROLE_USER), new Role(2L, RoleType.ROLE_ADMIN))
                )
        );
    }
}