package com.fraudplatform.transaction_ingestion_service.controller;

import com.fraudplatform.transaction_ingestion_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/token")
    public Map<String, String> getToken(@RequestParam String userId) {
        return Map.of("token", jwtUtil.generateToken(userId));
    }
}

