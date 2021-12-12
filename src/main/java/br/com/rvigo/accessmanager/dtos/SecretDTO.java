package br.com.rvigo.accessmanager.dtos;

import br.com.rvigo.accessmanager.entities.Secret;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SecretDTO {
    @Schema(title = "Secret Id", description = "The Id of the secret (auto generated)")
    private UUID id;

    @JsonSerialize(using = UUIDSerializer.class)
    @JsonDeserialize(using = UUIDDeserializer.class)
    @JsonProperty("user_id")
    @Schema(title = "User Id", description = "The Id of the user", required = true)
    private UUID userId;
    @Schema(title = "Username", description = "The username for the given secret")
    private String username;
    @Schema(title = "Password", description = "The password for the given secret")
    private String password;
    @Schema(title = "Salt", description = "The salt value for the given secret (auto generated)")
    private String salt;
    @Schema(title = "URL", description = "The url of the given secret")
    private String url;
    @Schema(title = "Note", description = "Additional info about the secret")
    private String note;

    public SecretDTO(Secret secret) {
        this.id = secret.getId();
        this.userId = secret.getUserId();
        this.username = secret.getUsername();
        this.password = secret.getPassword();
        this.salt = secret.getSalt();
        this.url = secret.getUrl();
        this.note = secret.getNote();
    }
}
