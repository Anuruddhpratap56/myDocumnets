package com.mydocs.mydocs.springSecurity.authDto;

import lombok.Data;

@Data
public class AuthDto {
    private String userName;
    private String refreshToken;
    private String password;
}
