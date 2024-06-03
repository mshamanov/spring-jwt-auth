package dev.mash.jwtauth.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mash.jwtauth.entity.RoleType;
import dev.mash.jwtauth.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumSet;
import java.util.List;

/**
 * Response to represent a {@link User} to a client.
 *
 * @author Mikhail Shamanov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User information response")
public class UserResponse {
    @Schema(description = "User name", example = "Mike")
    @JsonProperty("username")
    private String username;

    @Schema(description = "User email", example = "mike@gmail.com")
    @JsonProperty("email")
    private String email;

    @Schema(description = "User roles", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    @JsonProperty("roles")
    private List<RoleType> roles;
}
