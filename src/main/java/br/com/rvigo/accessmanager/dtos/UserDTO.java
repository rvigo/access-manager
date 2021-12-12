package br.com.rvigo.accessmanager.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    @Schema(hidden = true)
    @JsonProperty
    private UUID id;
    @Schema(title = "Username", description = "The username for the given user")
    @JsonProperty
    private String username;
    @Schema(title = "Password", description = "The encrypted password for the given user")
    @JsonProperty
    private String password;
}
