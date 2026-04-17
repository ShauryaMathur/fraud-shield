package com.fraudplatform.user_account_service.model.account;

import com.fraudplatform.user_account_service.model.BaseDocument;
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
public class Account extends BaseDocument {

    @Indexed(unique = false)
    private String userId;

    private String accountHolderName;

    private AccountType accountType;

    private BigDecimal balance;

    private Currency currency;

    private boolean active;

}
