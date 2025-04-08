package com.dev.ecom_platform_2.security.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserInfoResponse {
    private UUID id;
    private String username;
    private List<String> roles;

    private String jwtToken;
}



