package dev.mash.jwtauth.service;

import dev.mash.jwtauth.entity.User;
import dev.mash.jwtauth.rest.model.AccessTokenResponse;
import dev.mash.jwtauth.rest.model.SignInRequest;
import dev.mash.jwtauth.rest.model.SignUpRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Spy
    JwtService jwtService = new JwtService(Duration.ofDays(1));

    @Spy
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    UserService userService;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    void signUp_shouldSuccessfullySignUpUserAndReturnToken() {
        SignUpRequest request = new SignUpRequest("user", "user@example.com", "password");
        doNothing().when(this.userService).createUser(any(User.class));

        AccessTokenResponse tokenResponse = this.authenticationService.signUp(request);

        assertNotNull(tokenResponse);
        String token = tokenResponse.getToken();
        Assertions.assertEquals(request.getUsername(), this.jwtService.extractUserName(token));
        Assertions.assertEquals(request.getEmail(), this.jwtService.extractClaim(token, c -> c.get("email")));
    }

    @Test
    void signIn_shouldSuccessfullySignInUserAndReturnToken() {
        SignInRequest request = new SignInRequest("user", "password");
        Authentication authentication = new TestingAuthenticationToken(request.getUsername(), request.getPassword());
        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        User user = UserServiceTest.getUserList().getFirst();
        when(this.userService.getByUsername(authentication.getName())).thenReturn(user);

        AccessTokenResponse tokenResponse = this.authenticationService.signIn(request);

        assertNotNull(tokenResponse);
        String token = tokenResponse.getToken();
        Assertions.assertEquals(user.getUsername(), this.jwtService.extractUserName(token));
        Assertions.assertEquals(user.getEmail(), this.jwtService.extractClaim(token, c -> c.get("email")));
    }
}