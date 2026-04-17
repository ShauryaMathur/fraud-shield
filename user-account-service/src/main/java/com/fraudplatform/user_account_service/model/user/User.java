package com.fraudplatform.user_account_service.model.user;

import com.fraudplatform.user_account_service.model.BaseDocument;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseDocument {

    private String userId;

    private String fullName;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String address;

    private KycStatus kycStatus;
}
