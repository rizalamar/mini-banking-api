package com.rizalamar.minibankingapi.service;

import com.rizalamar.minibankingapi.domain.Account;
import com.rizalamar.minibankingapi.domain.AccountType;
import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.dto.account.AccountResponse;
import com.rizalamar.minibankingapi.dto.account.CreateAccountRequest;
import com.rizalamar.minibankingapi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public AccountResponse createAccount(User currentUser, CreateAccountRequest request){
        String accountNumber = generateUniqueAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(request.getInitialDeposit())
                .accountType(request.getAccountType())
                .user(currentUser)
                .build();

        Account savedAccount = accountRepository.save(account);
        return mapToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getMyAccounts(User currentUser){
        return accountRepository.findAllByUser(currentUser).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private String generateUniqueAccountNumber(){
        String number;
        do{
            long n = (long) (secureRandom.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
            number = String.valueOf(n);
        } while(accountRepository.existsByAccountNumber(number));
        return number;
    }

    private AccountResponse mapToResponse(Account account){
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .ownerName(account.getUser().getFullName())
                .build();
    }
}



