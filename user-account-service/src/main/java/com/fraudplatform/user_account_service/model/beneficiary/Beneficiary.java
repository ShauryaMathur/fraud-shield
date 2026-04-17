package com.fraudplatform.user_account_service.model.beneficiary;

import com.fraudplatform.user_account_service.model.BaseDocument;
import com.fraudplatform.user_account_service.model.account.AccountType;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "beneficiaries")
@CompoundIndexes({
        @CompoundIndex(
                name = "userId_accountNumber_idx",
                def = "{'userId': 1, 'accountNumber': 1}",
                unique = true
        )
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiary extends BaseDocument {
    private String userId;
    private String accountNickName;
    private String accountNumber;
    private AccountType accountType;
    private String accountHolderName;
    private boolean active;
}
