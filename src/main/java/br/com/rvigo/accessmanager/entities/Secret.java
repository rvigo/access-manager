package br.com.rvigo.accessmanager.entities;

import br.com.rvigo.accessmanager.dtos.SecretDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Document
public class Secret {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    @Field("user_id")
    @EqualsAndHashCode.Include
    private UUID userId;
    @ToString.Include
    private String username;
    private String password = "";
    private String salt;
    @ToString.Include
    private String url = "";
    private String notes = "";

    public Secret(SecretDTO dto) {
        this.id = dto.getId();
        this.userId = dto.getUserId();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.salt = dto.getSalt();
        this.url = dto.getUrl();
        this.notes = dto.getNotes();
    }

    public void update(Secret secret){
        if (this.equals(secret)){
            this.username = secret.getUsername() != null && !secret.getUsername().isEmpty()? secret.getUsername() : this.username;
            this.password = secret.getPassword() != null && !secret.getPassword().isEmpty() ? secret.getPassword() : this.password;
            this.url = secret.getUrl() != null &&  !secret.getUrl().isEmpty() ? secret.getUrl() : this.url;
            this.notes = secret.getNotes() != null &&  !secret.getNotes().isEmpty() ? secret.getNotes() : this.notes;
        }
    }

    public Boolean canUpdate(Secret secret){
            return (this.equals(secret)
                    && (!this.username.equals(secret.getUsername())
                    || !this.url.equals(secret.getUrl())
                    || !this.password.equals(secret.getPassword())
                    || !this.notes.equals(secret.getNotes())));
    }
}
