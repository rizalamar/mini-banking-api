package com.rizalamar.minibankingapi.dto.account;

import com.rizalamar.minibankingapi.domain.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private UUID id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private String ownerName;
}
