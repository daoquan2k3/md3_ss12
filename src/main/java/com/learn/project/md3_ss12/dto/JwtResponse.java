package com.learn.project.md3_ss12.dto;

import com.learn.project.md3_ss12.entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class JwtResponse {
    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private Date expired;
    //    @JsonIgnoreProperties({})
    private Users user;
}
