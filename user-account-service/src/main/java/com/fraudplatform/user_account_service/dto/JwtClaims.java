package com.fraudplatform.user_account_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JwtClaims {
    private String userId;
    private String role;
    private List<String> permissions;
}
