package com.bank.online_banking_api.controller;

import com.bank.online_banking_api.dto.AccountResponse;
import com.bank.online_banking_api.dto.AmountRequest;
import com.bank.online_banking_api.dto.CreateAccountRequest;
import com.bank.online_banking_api.dto.TransferRequest;
import com.bank.online_banking_api.entity.Account;
import com.bank.online_banking_api.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bank.online_banking_api.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccountForUser(
            @Valid @RequestBody CreateAccountRequest request) {

        Account account = accountService.createAccount(
                request.getAccountType()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToResponse(account));
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody AmountRequest request) {

        Account account = accountService.deposit(accountNumber, request.getAmount());
        return ResponseEntity.ok(mapToResponse(account));
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody AmountRequest request) {

        Account account = accountService.withdraw(accountNumber, request.getAmount());
        return ResponseEntity.ok(mapToResponse(account));
    }
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            @Valid @RequestBody TransferRequest request) {

        accountService.transfer(
                request.getFromAccount(),
                request.getToAccount(),
                request.getAmount()
        );

        return ResponseEntity.ok().build();
    }
    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        return response;
    }
    
    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<Page<Transaction>> getTransactions(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Transaction> transactions =
                accountService.getTransactions(accountNumber, page, size);

        return ResponseEntity.ok(transactions);
    }
}
