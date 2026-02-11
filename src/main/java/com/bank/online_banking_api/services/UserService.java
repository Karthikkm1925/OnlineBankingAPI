package com.bank.online_banking_api.services;

import java.util.List;

import com.bank.online_banking_api.dto.LoginRequest;
import com.bank.online_banking_api.dto.LoginResponse;
import com.bank.online_banking_api.dto.RegisterRequest;
import com.bank.online_banking_api.dto.RegisterResponse;
import com.bank.online_banking_api.entity.User;

public interface UserService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    List<User> getAllUsers();
}
