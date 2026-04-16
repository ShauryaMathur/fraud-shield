package com.fraudplatform.user_account_service.controller;


import com.fraudplatform.user_account_service.dto.AuthResponse;
import com.fraudplatform.user_account_service.dto.LoginRequest;
import com.fraudplatform.user_account_service.dto.SignupRequest;
import com.fraudplatform.user_account_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse signup(@Valid @RequestBody SignupRequest signupRequest){
        try{
            return userService.signup(signupRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }
}
