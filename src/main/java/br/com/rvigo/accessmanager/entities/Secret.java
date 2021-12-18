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
@ToString
@NoArgsConstructor
@Document
public class Secret {
    @Id
    private UUID id = UUID.randomUUID();
    @Field("user_id")
    private UUID userId;
    @EqualsAndHashCode.Include
    private String username;
    @EqualsAndHashCode.Include
    private String password = "";
    private String salt;
    @EqualsAndHashCode.Include
    private String url = "";
    private String note = "";

    public Secret(SecretDTO dto) {
        this.id = dto.getId();
        this.userId = dto.getUserId();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.salt = dto.getSalt();
        this.url = dto.getUrl();
        this.note = dto.getNote();
    }
}
