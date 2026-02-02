package com.bank.online_banking_api.services;

import com.bank.online_banking_api.entity.Account;
import com.bank.online_banking_api.entity.Transaction;
import com.bank.online_banking_api.entity.User;
import com.bank.online_banking_api.exception.BusinessException;
import com.bank.online_banking_api.exception.ResourceNotFoundException;
import com.bank.online_banking_api.repository.AccountRepository;
import com.bank.online_banking_api.repository.TransactionRepository;
import com.bank.online_banking_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              UserRepository userRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Account createAccount(Long userId, String accountType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new  ResourceNotFoundException("User not found"));

        Account account = Account.builder()
                .accountNumber(UUID.randomUUID().toString())
                .accountType(accountType)
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        return accountRepository.save(account);
    }

    @Override
    public Account deposit(String accountNumber, BigDecimal amount) {
        Account account = getAccount(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        saveTransaction(account, "DEPOSIT", amount);
        return accountRepository.save(account);
    }

    @Override
    public Account withdraw(String accountNumber, BigDecimal amount) {
        Account account = getAccount(accountNumber);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new  BusinessException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        saveTransaction(account, "WITHDRAW", amount);
        return accountRepository.save(account);
    }

    @Override
    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {
        withdraw(fromAccount, amount);
        deposit(toAccount, amount);
    }

    private Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new  ResourceNotFoundException("Account not found"));
    }

    private void saveTransaction(Account account, String type, BigDecimal amount) {
        Transaction transaction = Transaction.builder()
                .account(account)
                .type(type)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }
}
