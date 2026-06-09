package com.rizalamar.minibankingapi.controller;

import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.dto.WebResponse;
import com.rizalamar.minibankingapi.dto.transfer.TransferRequest;
import com.rizalamar.minibankingapi.dto.transfer.TransferResponse;
import com.rizalamar.minibankingapi.security.annotation.CurrentUser;
import com.rizalamar.minibankingapi.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public WebResponse<TransferResponse> transfer(
            @CurrentUser User currentUser,
            @Valid @RequestBody TransferRequest request
            ) {

        TransferResponse transferResponse = transferService.transfer(currentUser, request);

        return WebResponse.<TransferResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data(transferResponse)
                .message("Transfer successful")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
