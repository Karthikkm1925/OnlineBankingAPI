package com.bank.online_banking_api.services;

import java.math.BigDecimal;
import java.util.List;
import com.bank.online_banking_api.entity.Account;
import com.bank.online_banking_api.entity.Transaction;

import org.springframework.data.domain.Page;
public interface AccountService {
    Account createAccount(String accountType);

    Account deposit(String accountNumber, BigDecimal amount);

    Account withdraw(String accountNumber, BigDecimal amount);

    void transfer(String fromAccount, String toAccount, BigDecimal amount);
    
    Page<Transaction> getTransactions(String accountNumber, int page, int size);
    
    List<Account> getAllAccounts();
}
