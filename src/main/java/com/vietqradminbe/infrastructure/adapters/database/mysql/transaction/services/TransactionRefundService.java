package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services;

import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.repositories.TransactionRefundRepository;
import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services.interfaces.ITransactionRefundService;
import com.vietqradminbe.web.dto.response.interfaces.TransactionRefundAdminDetailDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TransactionRefundService implements ITransactionRefundService {

    TransactionRefundRepository transactionRefundRepository;

    @Override
    public List<TransactionRefundAdminDetailDTO> getTransactionRefundAdminDetail(String referenceNumber) {
        referenceNumber = referenceNumber.trim().replaceAll("\n", "");
        return transactionRefundRepository.getTransactionRefundAdminDetail(referenceNumber);
    }
}
