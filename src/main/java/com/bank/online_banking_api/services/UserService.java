package com.bank.online_banking_api.services;

import com.bank.online_banking_api.dto.LoginRequest;
import com.bank.online_banking_api.dto.LoginResponse;
import com.bank.online_banking_api.dto.RegisterRequest;
import com.bank.online_banking_api.dto.RegisterResponse;

public interface UserService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
