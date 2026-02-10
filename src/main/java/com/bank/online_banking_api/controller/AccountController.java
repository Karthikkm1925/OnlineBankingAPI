package com.bank.online_banking_api.controller;

import com.bank.online_banking_api.dto.AccountResponse;
import com.bank.online_banking_api.dto.AmountRequest;
import com.bank.online_banking_api.dto.CreateAccountRequest;
import com.bank.online_banking_api.entity.Account;
import com.bank.online_banking_api.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        Account account = accountService.createAccount(
                request.getUserId(),
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

    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        return response;
    }
}
