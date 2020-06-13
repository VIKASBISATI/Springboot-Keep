package com.fundoo.project.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobileNumber;
    private String password;
}
