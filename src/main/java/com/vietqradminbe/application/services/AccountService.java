package com.vietqradminbe.application.services;

import com.vietqradminbe.application.mappers.AccountMapper;
import com.vietqradminbe.application.services.interfaces.IAccountService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.Account;
import com.vietqradminbe.domain.repositories.AccountRepository;
import com.vietqradminbe.web.dto.request.AccountCreationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    @Autowired
    AccountRepository accountRepo;
    AccountMapper accountMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }
    @Override
    public Account getAccountByUsername(String username) {
        return accountRepo.getAccountByUsername(username);
    }

    @Override
    public Account createAccountRequest(AccountCreationRequest request) {
        if(accountRepo.getAccountByUsername(request.getUsername()) != null){
            throw new BadRequestException(ErrorCode.ACCOUNT_EXISTED);
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Account admin = accountMapper.toAccount(request);
        return accountRepo.save(admin);
    }
}
