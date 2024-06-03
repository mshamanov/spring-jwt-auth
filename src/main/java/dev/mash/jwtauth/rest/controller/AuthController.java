package dev.mash.jwtauth.rest.controller;

import dev.mash.jwtauth.entity.User;
import dev.mash.jwtauth.rest.model.AccessTokenResponse;
import dev.mash.jwtauth.rest.model.SignInRequest;
import dev.mash.jwtauth.rest.model.SignUpRequest;
import dev.mash.jwtauth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to authenticate and register users {@link User}.
 *
 * @author Mikhail Shamanov
 */
@Tag(name = "Authentication")
@ApiResponse(
        responseCode = "200",
        content = @Content(
                schema = @Schema(
                        implementation = AccessTokenResponse.class
                )
        )
)
@RestController
@RequestMapping(path = "/api/v1/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Registers a new user")
    @PostMapping("/sign-up")
    public AccessTokenResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return this.authenticationService.signUp(request);
    }

    @Operation(summary = "Authenticates a user")
    @PostMapping("/sign-in")
    public AccessTokenResponse signIn(@RequestBody @Valid SignInRequest request) {
        return this.authenticationService.signIn(request);
    }
}
