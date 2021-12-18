package br.com.rvigo.accessmanager.entities;

import br.com.rvigo.accessmanager.dtos.UserDTO;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@Document
public class User implements UserDetails {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    @EqualsAndHashCode.Include
    private String username;
    @EqualsAndHashCode.Include
    @ToString.Exclude
    @Field("master_password")
    private String password;
    @ToString.Exclude
    @BsonIgnore
    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

    public User(UserDTO dto) {
        this.id = dto.getId();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
