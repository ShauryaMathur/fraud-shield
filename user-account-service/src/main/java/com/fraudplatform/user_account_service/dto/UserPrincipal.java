package com.fraudplatform.user_account_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserPrincipal {
    private String userId;
    private String role;
    private List<String> permissions;
}
