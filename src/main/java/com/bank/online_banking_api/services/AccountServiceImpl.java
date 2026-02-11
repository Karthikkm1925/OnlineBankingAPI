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
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @PreAuthorize("hasRole('USER')")
    public Account createAccount(String accountType) {

        String email = getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Account account = Account.builder()
                .accountNumber(UUID.randomUUID().toString())
                .accountType(accountType)
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        return accountRepository.save(account);
    }
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return authentication.getName();
    }
    
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
       
    private Account getAccountForCurrentUser(String accountNumber) {

        String email = getCurrentUserEmail();

        return accountRepository
                .findByAccountNumberAndUserEmail(accountNumber, email)
                .orElseThrow(() -> new BusinessException("Unauthorized account access"));
    }
    
    @Override
    @PreAuthorize("hasRole('USER')")
    public Account deposit(String accountNumber, BigDecimal amount) {
    	
    	if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
    	    throw new BusinessException("Amount must be greater than zero");
    	}
        Account account =  getAccountForCurrentUser(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        saveTransaction(account, "DEPOSIT", amount);
        return accountRepository.save(account);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public Account withdraw(String accountNumber, BigDecimal amount) {
        Account account =  getAccountForCurrentUser(accountNumber);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new  BusinessException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        saveTransaction(account, "WITHDRAW", amount);
        return accountRepository.save(account);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void transfer(String fromAccount, String toAccount, BigDecimal amount) {

        Account source = getAccountForCurrentUser(fromAccount);
        
        if (fromAccount.equals(toAccount)) {
            throw new BusinessException("Cannot transfer to same account");
        }
        
        if (source.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Insufficient balance");
        }

        Account destination = accountRepository.findByAccountNumber(toAccount)
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));

        saveTransaction(source, "TRANSFER_OUT", amount);
        saveTransaction(destination, "TRANSFER_IN", amount);

        accountRepository.save(source);
        accountRepository.save(destination);
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
    
    @Override
    @PreAuthorize("hasRole('USER')")
    public Page<Transaction> getTransactions(String accountNumber, int page, int size) {

        Account account = getAccountForCurrentUser(accountNumber);

        Pageable pageable = PageRequest.of(page, size);

        return transactionRepository
                .findByAccountIdOrderByTimestampDesc(account.getId(), pageable);
    }
}
