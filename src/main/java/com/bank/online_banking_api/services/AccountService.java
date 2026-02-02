package com.bank.online_banking_api.services;

import java.math.BigDecimal;

import com.bank.online_banking_api.entity.Account;

public interface AccountService {
    Account createAccount(Long userId, String accountType);

    Account deposit(String accountNumber, BigDecimal amount);

    Account withdraw(String accountNumber, BigDecimal amount);

    void transfer(String fromAccount, String toAccount, BigDecimal amount);
}
