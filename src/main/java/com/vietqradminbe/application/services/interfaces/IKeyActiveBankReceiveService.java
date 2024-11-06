package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.KeyActiveBankReceive;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IKeyActiveBankReceiveService {
    List<String> checkDuplicatedKeyActives(List<String> keys);


     void insertAll(List<KeyActiveBankReceive> entities);

    List<KeyActiveBankReceive> getListKeyByBankId(String bankId);

    String getBankIdByKey(String key);

    int getStatusByKeyAndBankId(String key);
}
