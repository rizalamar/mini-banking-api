package com.rizalamar.minibankingapi.controller;

import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.dto.WebResponse;
import com.rizalamar.minibankingapi.dto.transaction.TransactionRequest;
import com.rizalamar.minibankingapi.security.annotation.CurrentUser;
import com.rizalamar.minibankingapi.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public WebResponse<String> deposit(
            @CurrentUser User user,
            @Valid @RequestBody TransactionRequest request
            ) {
        transactionService.deposit(user, request);
        return WebResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data("Deposit successfull")
                .message("Balance has been updated")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostMapping("/withdraw")
    public WebResponse<String> withdraw(
            @CurrentUser User user,
            @Valid @RequestBody TransactionRequest request
    ){
        transactionService.withdraw(user, request);
        return WebResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data("Deposit successful")
                .message("Balance has been updated")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
