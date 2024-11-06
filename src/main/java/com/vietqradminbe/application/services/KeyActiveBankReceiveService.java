package com.vietqradminbe.application.services;

import com.vietqradminbe.application.services.interfaces.IKeyActiveBankReceiveService;
import com.vietqradminbe.domain.models.KeyActiveBankReceive;
import com.vietqradminbe.domain.repositories.KeyActiveBankReceiveRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyActiveBankReceiveService implements IKeyActiveBankReceiveService {

    KeyActiveBankReceiveRepository keyActiveBankReceiveRepository;


    @Override
    public List<String> checkDuplicatedKeyActives(List<String> keys) {
        return keyActiveBankReceiveRepository.checkDuplicatedKeyActives(keys);
    }

    @Override
    public void insertAll(List<KeyActiveBankReceive> entities) {
        keyActiveBankReceiveRepository.saveAll(entities);
    }

    @Override
    public List<KeyActiveBankReceive> getListKeyByBankId(String bankId) {
        return null;
    }

    @Override
    public String getBankIdByKey(String key) {
        return null;
    }

    @Override
    public int getStatusByKeyAndBankId(String key) {
        return 0;
    }
}
