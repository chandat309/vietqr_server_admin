package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.web.dto.request.GenerateKeyBankDTO;
import com.vietqradminbe.web.dto.response.interfaces.KeyActiveBankReceiveDTO;
import com.vietqradminbe.web.dto.response.PagingDTO;

import java.util.List;

public interface IKeyActiveBankReceiveService {
    List<String> checkDuplicatedKeyActives(List<String> keys);

    List<String> generateAndSaveKeys(User user, GenerateKeyBankDTO dto);

    PagingDTO<KeyActiveBankReceiveDTO> getKeyActiveBankReceives(String startDate, String endDate, int page, int size);
}
