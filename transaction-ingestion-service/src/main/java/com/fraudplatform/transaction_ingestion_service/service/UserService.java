package com.fraudplatform.transaction_ingestion_service.service;

import com.fraudplatform.transaction_ingestion_service.dto.AuthResponse;
import com.fraudplatform.transaction_ingestion_service.dto.LoginRequest;
import com.fraudplatform.transaction_ingestion_service.dto.SignupRequest;
import com.fraudplatform.transaction_ingestion_service.model.User;
import com.fraudplatform.transaction_ingestion_service.repository.UserRepository;
import com.fraudplatform.transaction_ingestion_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse signup(SignupRequest signupRequest) {

        if(userRepository.findByEmail(signupRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email already in use : " + signupRequest.getEmail());
        }

        String userId = "user-" + UUID.randomUUID().toString().substring(0, 8);

        User user = User.builder()
                .userId(userId)
                .email(signupRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(userId);

        log.info("New user registered email={} userId={}", signupRequest.getEmail(), userId);

        return new AuthResponse(token, userId, signupRequest.getEmail());
    }

    public AuthResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new RuntimeException("Email not found"));

        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUserId());

        return new AuthResponse(token, user.getUserId(), user.getEmail());
    }
}
