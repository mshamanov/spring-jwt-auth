package dev.mash.jwtauth.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mash.jwtauth.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response to represent a {@link Role} to a client.
 *
 * @author Mikhail Shamanov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User role response")
public class RoleResponse {
    @Schema(description = "Role name", example = "ROLE_USER")
    @JsonProperty("role")
    private String role;
}
