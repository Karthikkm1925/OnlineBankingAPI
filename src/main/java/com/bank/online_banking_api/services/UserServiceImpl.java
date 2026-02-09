package com.bank.online_banking_api.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.online_banking_api.dto.auth.LoginRequest;
import com.bank.online_banking_api.dto.auth.LoginResponse;
import com.bank.online_banking_api.dto.auth.RegisterRequest;
import com.bank.online_banking_api.dto.auth.RegisterResponse;
import com.bank.online_banking_api.entity.Role;
import com.bank.online_banking_api.entity.User;
import com.bank.online_banking_api.exception.BusinessException;
import com.bank.online_banking_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName()); 
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User saved = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setUserId(saved.getId());
        response.setEmail(saved.getEmail());
        response.setRole(saved.getRole());

        return response;
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid email or password");
        }

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}
