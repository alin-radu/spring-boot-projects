package com.dev.ecom_platform_2.security.jwt;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {
    private String username;
    private String password;
}
