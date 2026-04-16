package com.fraudplatform.user_account_service.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseDocument{

    @Indexed(unique = false)
    private String userId;

    private AccountType accountType;

    private BigDecimal balance;
}
