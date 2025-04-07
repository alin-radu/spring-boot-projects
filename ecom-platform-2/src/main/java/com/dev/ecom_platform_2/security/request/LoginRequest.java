package com.dev.ecom_platform_2.security.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {
    private String username;
    private String password;
}
