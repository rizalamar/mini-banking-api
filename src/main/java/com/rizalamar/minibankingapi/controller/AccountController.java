package com.rizalamar.minibankingapi.controller;

import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.dto.WebResponse;
import com.rizalamar.minibankingapi.dto.account.AccountResponse;
import com.rizalamar.minibankingapi.dto.account.CreateAccountRequest;
import com.rizalamar.minibankingapi.security.annotation.CurrentUser;
import com.rizalamar.minibankingapi.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public WebResponse<AccountResponse> createAccount(
            @CurrentUser User user,
            @Valid @RequestBody CreateAccountRequest request
            ) {
        AccountResponse accountResponse = accountService.createAccount(user, request);
        return WebResponse.<AccountResponse>builder()
                .code(HttpStatus.CREATED.value())
                .status("CREATED")
                .data(accountResponse)
                .message("Account created successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping
    public WebResponse<List<AccountResponse>> getMyAccounts(@CurrentUser User user){
        List<AccountResponse> myAccounts = accountService.getMyAccounts(user);
        return WebResponse.<List<AccountResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data(myAccounts)
                .message("Accounts retrieve successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/{accountNumber}")
    public WebResponse<AccountResponse> getDetailAccount(
            @CurrentUser User user,
            @PathVariable("accountNumber") String accountNumber
    ){
        AccountResponse detailAccountResponse = accountService.getDetailAccount(user, accountNumber);
        return WebResponse.<AccountResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data(detailAccountResponse)
                .message("Detail account retrieve successful")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/verify/{accountNumber}")
    public WebResponse<String> verifyRecipient(
            @PathVariable("accountNumber") String accountNumber
    ) {
        String accountOwnerName = accountService.getAccountOwnerName(accountNumber);
        return WebResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data(accountOwnerName)
                .message("Recipient verified")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
