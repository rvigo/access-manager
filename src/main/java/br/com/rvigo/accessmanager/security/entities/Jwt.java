package br.com.rvigo.accessmanager.security.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Jwt {
    @JsonProperty("token")
    private String jwtToken;
    @JsonProperty("token_type")
    private String tokenType = "Bearer";
    private Long duration;

    public Jwt(String jwtToken, Long duration) {
        this.jwtToken = jwtToken;
        this.duration = duration;
    }
}