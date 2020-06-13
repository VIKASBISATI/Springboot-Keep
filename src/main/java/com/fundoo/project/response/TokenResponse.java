package com.fundoo.project.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String token;
}
