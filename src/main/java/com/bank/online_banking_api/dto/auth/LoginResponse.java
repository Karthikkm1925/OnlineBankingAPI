package com.bank.online_banking_api.dto.auth;

import com.bank.online_banking_api.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private Long userId;
    private String email;
    private Role role;
}
