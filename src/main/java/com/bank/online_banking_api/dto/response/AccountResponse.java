package com.bank.online_banking_api.dto.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AccountResponse {

    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
 
}

