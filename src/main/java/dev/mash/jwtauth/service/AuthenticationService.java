package dev.mash.jwtauth.service;

import dev.mash.jwtauth.entity.Role;
import dev.mash.jwtauth.entity.RoleType;
import dev.mash.jwtauth.entity.User;
import dev.mash.jwtauth.rest.model.AccessTokenResponse;
import dev.mash.jwtauth.rest.model.SignInRequest;
import dev.mash.jwtauth.rest.model.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to manage authentication processes, including registration and sign-in.
 *
 * @author Mikhail Shamanov
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new {@link User}
     *
     * @param request user request data to sign up
     * @return JWT token
     */
    @Transactional
    public AccessTokenResponse signUp(SignUpRequest request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(this.passwordEncoder.encode(request.getPassword()));
        newUser.addRole(new Role(RoleType.ROLE_USER));

        this.userService.createUser(newUser);
        String jwt = this.jwtService.generateToken(newUser);

        return new AccessTokenResponse(jwt);
    }

    /**
     * Authenticates a {@link User}
     *
     * @param request user request data to sign in
     * @return JWT token
     * @throws AuthenticationException if sign in fails
     */
    public AccessTokenResponse signIn(SignInRequest request) throws AuthenticationException {
        Authentication authenticate = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        User user = this.userService.getByUsername(authenticate.getName());
        String jwt = this.jwtService.generateToken(user);

        return new AccessTokenResponse(jwt);
    }
}