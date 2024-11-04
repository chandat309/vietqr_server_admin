package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services.interfaces;

import com.vietqradminbe.web.dto.response.interfaces.TransactionReceiveLogDTO;

import java.util.List;

public interface ITransactionReceiveLogService {
    List<TransactionReceiveLogDTO> getTransactionLogsByTransId(String transactionId);
}
