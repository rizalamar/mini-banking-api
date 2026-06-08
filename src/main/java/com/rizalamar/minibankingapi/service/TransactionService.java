package com.rizalamar.minibankingapi.service;

import com.rizalamar.minibankingapi.domain.Account;
import com.rizalamar.minibankingapi.domain.Transaction;
import com.rizalamar.minibankingapi.domain.TransactionType;
import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.dto.transaction.TransactionRequest;
import com.rizalamar.minibankingapi.repository.AccountRepository;
import com.rizalamar.minibankingapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void deposit(User currentUser, TransactionRequest request){
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(!account.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account does not belong to you");
        }

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(request.getAmount())
                .transactionType(TransactionType.DEPOSIT)
                .description("Deposit via api")
                .build();

        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(User currentUser, TransactionRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(!account.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account does not belong to you");
        }

        if (account.getBalance().compareTo( request.getAmount()) < 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(request.getAmount())
                .transactionType(TransactionType.WITHDRAW)
                .description("Withdraw via api")
                .build();

        transactionRepository.save(transaction);

    }
}
