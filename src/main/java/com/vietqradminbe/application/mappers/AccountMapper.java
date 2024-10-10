package com.vietqradminbe.application.mappers;

import com.vietqradminbe.domain.models.Account;
import com.vietqradminbe.web.dto.request.AccountCreationRequest;
import com.vietqradminbe.web.dto.response.AccountResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toAccountResponse(Account account);
    Account toAccount(AccountCreationRequest request);
}
