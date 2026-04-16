package com.fraudplatform.api_gateway.dto;

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
