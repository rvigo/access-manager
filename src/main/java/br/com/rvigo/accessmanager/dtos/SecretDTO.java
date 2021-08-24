package br.com.rvigo.accessmanager.dtos;

import br.com.rvigo.accessmanager.entities.Secret;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SecretDTO  {
    private UUID id;

    @JsonSerialize(using= UUIDSerializer.class)
    @JsonDeserialize(using= UUIDDeserializer.class)
    @JsonProperty("user_id")
    private UUID userId;
    private String username;
    private String password;
    private String salt;
    private String url;
    private String note;

    public SecretDTO(Secret secret){
        this.id = secret.getId();
        this.userId = secret.getUserId();
        this.username = secret.getUsername();
        this.password = secret.getPassword();
        this.salt = secret.getSalt();
        this.url = secret.getUrl();
        this.note = secret.getNote();
    }
}
