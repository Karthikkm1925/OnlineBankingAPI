package com.bank.online_banking_api.services;

import com.bank.online_banking_api.dto.auth.LoginRequest;
import com.bank.online_banking_api.dto.auth.LoginResponse;
import com.bank.online_banking_api.dto.auth.RegisterRequest;
import com.bank.online_banking_api.dto.auth.RegisterResponse;

public interface UserService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
