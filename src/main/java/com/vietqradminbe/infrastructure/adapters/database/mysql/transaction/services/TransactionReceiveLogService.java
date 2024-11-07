package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services;

import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.repositories.TransactionReceiveLogRepository;
import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services.interfaces.ITransactionReceiveLogService;
import com.vietqradminbe.web.dto.response.interfaces.TransactionReceiveLogDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TransactionReceiveLogService implements ITransactionReceiveLogService {

    TransactionReceiveLogRepository transactionReceiveLogRepository;

    @Override
    public List<TransactionReceiveLogDTO> getTransactionLogsByTransId(String transactionId) {
        return transactionReceiveLogRepository.getTransactionLogsByTransId(transactionId);
    }
}
