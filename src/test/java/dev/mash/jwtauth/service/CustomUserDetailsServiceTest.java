package dev.mash.jwtauth.service;

import dev.mash.jwtauth.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    UserService userService;

    @InjectMocks
    CustomUserDetailsService detailsService;

    @Test
    void loadUserByUsername_shouldReturnUser() {
        User user = UserServiceTest.getUserList().getFirst();
        when(this.userService.getByUsername(user.getUsername())).thenReturn(user);

        UserDetails userDetails = this.detailsService.loadUserByUsername(user.getUsername());

        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(user.getAuthorities(), userDetails.getAuthorities());
    }
}