package br.com.rvigo.accessmanager.security.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenDTO {
    @JsonProperty("token")
    private String jwtToken;
    @JsonProperty("token_type")
    private String tokenType = "Bearer";
    @Schema(description = "The token duration (in ms)")
    private Long duration;

    public AccessTokenDTO(String jwtToken, Long duration) {
        this.jwtToken = jwtToken;
        this.duration = duration;
    }
}