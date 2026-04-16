package com.fraudplatform.user_account_service.service;

import com.fraudplatform.user_account_service.dto.AuthResponse;
import com.fraudplatform.user_account_service.dto.LoginRequest;
import com.fraudplatform.user_account_service.dto.SignupRequest;
import com.fraudplatform.user_account_service.entity.Role;
import com.fraudplatform.user_account_service.entity.RoleName;
import com.fraudplatform.user_account_service.entity.RolePermission;
import com.fraudplatform.user_account_service.entity.UserRole;
import com.fraudplatform.user_account_service.model.KycStatus;
import com.fraudplatform.user_account_service.model.User;
import com.fraudplatform.user_account_service.repository.RolePermissionsRepository;
import com.fraudplatform.user_account_service.repository.RoleRepository;
import com.fraudplatform.user_account_service.repository.UserRepository;
import com.fraudplatform.user_account_service.repository.UserRoleRepository;
import com.fraudplatform.user_account_service.security.JwtUtil;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionsRepository rolePermissionsRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse signup(SignupRequest signupRequest) {

        if(userRepository.findByEmail(signupRequest.getEmail()).isPresent()){
            throw new DuplicateRequestException("Email already in use : " + signupRequest.getEmail());
        }

        String userId = "user-" + UUID.randomUUID().toString().substring(0, 8);

        User user = User.builder()
                .userId(userId)
                .fullName(signupRequest.getFullName())
                .email(signupRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .kycStatus(KycStatus.PENDING)
                .build();

        User savedUser = userRepository.save(user);

        Role customerRole = roleRepository.findByName(String.valueOf(RoleName.CUSTOMER))
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        UserRole userRole = new UserRole(savedUser.getId(), customerRole);

        userRoleRepository.save(userRole);

        return AuthResponse.builder().userId(userId).emai(signupRequest.getEmail()).build();
    }

    public AuthResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new RuntimeException("Email not found"));

        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }

        Role customerRole = roleRepository.findByName(String.valueOf(RoleName.CUSTOMER))
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        UserRole userRole = new UserRole(user.getId(), customerRole);

        userRoleRepository.save(userRole);

        List<RolePermission> permissions = rolePermissionsRepository.findAllByRole(userRole.getRole());

        String token = jwtUtil.generateToken(user.getId(), permissions, userRole.getRole());

        log.info("User logged in with email={} userId={}", loginRequest.getEmail(), user.getEmail());

        return new AuthResponse(token, user.getUserId(), loginRequest.getEmail());
    }
}
