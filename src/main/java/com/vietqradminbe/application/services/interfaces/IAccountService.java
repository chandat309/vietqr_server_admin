package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.Account;
import com.vietqradminbe.web.dto.request.AccountCreationRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface IAccountService {
    public List<Account> getAllAccounts();
    public Account getAccountByUsername(String username);
    public Account createAccountRequest(AccountCreationRequest request);
}
