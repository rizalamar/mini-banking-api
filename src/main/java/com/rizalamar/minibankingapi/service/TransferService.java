package com.rizalamar.minibankingapi.service;

import com.rizalamar.minibankingapi.domain.Account;
import com.rizalamar.minibankingapi.domain.Transaction;
import com.rizalamar.minibankingapi.domain.TransactionType;
import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.dto.transfer.TransferRequest;
import com.rizalamar.minibankingapi.dto.transfer.TransferResponse;
import com.rizalamar.minibankingapi.repository.AccountRepository;
import com.rizalamar.minibankingapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransferResponse transfer(User user, TransferRequest request){
        Account senderAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender account not found"));

        if(!senderAccount.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        Account receiverAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver account not found"));

        if(senderAccount.getAccountNumber().equals(receiverAccount.getAccountNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot transfer to the same account");
        }


        if(senderAccount.getBalance().compareTo(request.getAmount()) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(request.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(request.getAmount()));
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        Transaction debit = Transaction.builder()
                .account(senderAccount)
                .amount(request.getAmount())
                .transactionType(TransactionType.TRANSFER_OUT)
                .description("Transfer to " + receiverAccount.getAccountNumber() + " - " + request.getRemark())
                .build();
        Transaction savedDebit = transactionRepository.save(debit);

        Transaction credit = Transaction.builder()
                .account(receiverAccount)
                .amount(request.getAmount())
                .transactionType(TransactionType.TRANSFER_IN)
                .description("Transfer from " + senderAccount.getAccountNumber() + " - " + request.getRemark())
                .build();
        transactionRepository.save(credit);

        return TransferResponse.builder()
                .transactionId(savedDebit.getId().toString())
                .fromAccountNumber(senderAccount.getAccountNumber())
                .toAccountNumber(request.getToAccountNumber())
                .amount(request.getAmount())
                .remark(request.getRemark())
                .timestamp(savedDebit.getCreatedAt())
                .build();
    }
}
