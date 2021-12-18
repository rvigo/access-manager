package br.com.rvigo.accessmanager.dtos;

import br.com.rvigo.accessmanager.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    @JsonProperty
    private UUID id;
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }
}
