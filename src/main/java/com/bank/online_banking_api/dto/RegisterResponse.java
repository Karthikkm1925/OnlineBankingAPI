package com.bank.online_banking_api.dto;

import com.bank.online_banking_api.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    private Long userId;
    private String email;
    private Role role;
}
