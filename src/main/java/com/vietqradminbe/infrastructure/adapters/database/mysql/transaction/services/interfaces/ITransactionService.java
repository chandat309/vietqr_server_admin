package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services.interfaces;


import com.vietqradminbe.web.dto.request.RequestFilterTransactionRequest;
import com.vietqradminbe.web.dto.response.TransactionReceivePaginationResponseDTO;
import com.vietqradminbe.web.dto.response.interfaces.TransactionReceiveAdminListDTO;

import java.util.List;


public interface ITransactionService {
    public List<TransactionReceiveAdminListDTO> getTransByBankAccountAllFilter(Long from, Long to,
                                                                        String bankAccount, String id, String orderId,
                                                                        String terminalCode, String subCode, Integer transStatus,
                                                                        int offset, int size);

    public TransactionReceivePaginationResponseDTO getTransactionsWithPagination(Long from, Long to, String bankAccount, String id, String orderId,
                                                                        String terminalCode, String subCode, Integer transStatus,
                                                                        int offset, int limit);
    public TransactionReceivePaginationResponseDTO getTransactionsWithPaginationAllFilter(int offset, int size);

    public TransactionReceivePaginationResponseDTO getTransactionsWithPaginationByOption(RequestFilterTransactionRequest request);
}
