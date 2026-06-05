package com.rizalamar.minibankingapi.service;

import com.rizalamar.minibankingapi.domain.User;
import com.rizalamar.minibankingapi.dto.auth.AuthResponse;
import com.rizalamar.minibankingapi.dto.auth.RegisterRequest;
import com.rizalamar.minibankingapi.repository.UserRepository;
import com.rizalamar.minibankingapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request){
        validationService.validate(request);

        boolean existsByEmail = userRepository.existsByEmail(request.getEmail());
        if(existsByEmail){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponse.builder().token(token).email(user.getEmail()).build();
    }
}
