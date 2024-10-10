package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.AccountService;
import com.vietqradminbe.domain.models.Account;
import com.vietqradminbe.web.dto.request.AccountCreationRequest;
import com.vietqradminbe.web.dto.response.APIResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {

    AccountService accountService;

    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("auth/register")
    public APIResponse<Account> createAccount(@RequestBody @Valid AccountCreationRequest request) {
        APIResponse<Account> response = new APIResponse<>();
        response.setCode(200);
        response.setMessage("Create successfully!");
        response.setResult(accountService.createAccountRequest(request));
        return response;
    }
}
