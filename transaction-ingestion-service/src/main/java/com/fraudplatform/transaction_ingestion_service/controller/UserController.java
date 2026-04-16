package com.fraudplatform.transaction_ingestion_service.controller;

import com.fraudplatform.transaction_ingestion_service.dto.AuthResponse;
import com.fraudplatform.transaction_ingestion_service.dto.LoginRequest;
import com.fraudplatform.transaction_ingestion_service.dto.SignupRequest;
import com.fraudplatform.transaction_ingestion_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse signup(@Valid @RequestBody SignupRequest signupRequest){
        return userService.signup(signupRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }
}
