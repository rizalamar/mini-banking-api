package com.rizalamar.minibankingapi.controller;

import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.dto.WebResponse;
import com.rizalamar.minibankingapi.dto.user.UserResponse;
import com.rizalamar.minibankingapi.security.CurrentUser;
import com.rizalamar.minibankingapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public WebResponse<UserResponse> getMyProfile(@CurrentUser User user){
        UserResponse myProfileResponse = userService.getMyProfile(user);
        return WebResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data(myProfileResponse)
                .message("Profile retrieve successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public WebResponse<List<UserResponse>> getAllUsers(){
        List<UserResponse> userResponses = userService.getAllUsers();
        return WebResponse.<List<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data(userResponses)
                .message("All users retrieve successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
