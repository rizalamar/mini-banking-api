package com.rizalamar.minibankingapi.service;

import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.dto.user.UpdateUserRequest;
import com.rizalamar.minibankingapi.dto.user.UserResponse;
import com.rizalamar.minibankingapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ValidationService validationService;

    public UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : "CUSTOMER")
                .build();
    }

    @Transactional(readOnly = true)
    public UserResponse getMyProfile(User user){
        return toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Transactional
    public UserResponse updateProfile(User currentUser, UpdateUserRequest request){
        currentUser.setFullName(request.getFullName());
        User updatedUser = userRepository.save(currentUser);
        return toUserResponse(updatedUser);
    }
}
