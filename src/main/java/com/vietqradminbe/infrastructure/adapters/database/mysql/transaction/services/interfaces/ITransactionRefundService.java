package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services.interfaces;

import com.vietqradminbe.web.dto.response.interfaces.TransactionRefundAdminDetailDTO;

import java.util.List;

public interface ITransactionRefundService {
    public List<TransactionRefundAdminDetailDTO> getTransactionRefundAdminDetail(String referenceNumber);
}
