package com.piatek.rafal.account_service.controller;

import com.piatek.rafal.account_service.domain.Account;
import com.piatek.rafal.account_service.domain.CreateAccountRequest;
import com.piatek.rafal.account_service.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final ClientService clientService;

    public AccountController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Account> createOrGetAccount(@Valid @RequestBody CreateAccountRequest account) {
        return ResponseEntity.ok(clientService.createOrGetAccount(account));
    }


}
