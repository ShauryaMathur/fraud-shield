package com.fraudplatform.user_account_service.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
@Data
@Builder
public class User {
    @Id
    private String id;

    private String userId;

    private String fullName;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String address;

    private KycStatus kycStatus;

    @CreatedDate
    private Instant createdDate;
}
