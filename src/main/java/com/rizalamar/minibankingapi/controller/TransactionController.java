package com.rizalamar.minibankingapi.controller;

import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.dto.WebResponse;
import com.rizalamar.minibankingapi.dto.transaction.TransactionRequest;
import com.rizalamar.minibankingapi.dto.transaction.TransactionResponse;
import com.rizalamar.minibankingapi.security.annotation.CurrentUser;
import com.rizalamar.minibankingapi.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/{accountNumber}/mutation")
    public WebResponse<List<TransactionResponse>> getMutation(
            @CurrentUser User user,
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<TransactionResponse> mutation = transactionService.getMutation(user, accountNumber, page, size);

        return WebResponse.<List<TransactionResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data(mutation.getContent())
                .message("Mutation retrieved successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
