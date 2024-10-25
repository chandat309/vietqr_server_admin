package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services;

import antlr.StringUtils;
import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.repositories.TransactionRepository;
import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services.interfaces.ITransactionService;
import com.vietqradminbe.web.dto.request.RequestFilterTransactionRequest;
import com.vietqradminbe.web.dto.response.TransactionReceivePaginationResponseDTO;
import com.vietqradminbe.web.dto.response.interfaces.TransactionReceiveAdminListDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    TransactionRepository transactionRepository;

    @Override
    public List<TransactionReceiveAdminListDTO> getTransByBankAccountAllFilter(Long from, Long to,
                                                                               String bankAccount, String id, String orderId,
                                                                               String terminalCode, String subCode, Integer transStatus,
                                                                               int offset, int size) {
        return transactionRepository.getTransByBankAccountAllFilter(from, to, bankAccount,
                id, orderId, terminalCode, subCode,
                transStatus, offset, size);
    }

    @Override
    public TransactionReceivePaginationResponseDTO getTransactionsWithPagination(Long from, Long to, String bankAccount, String id, String orderId,
                                                                                 String terminalCode, String subCode, Integer transStatus,
                                                                                 int page, int size) {
        int offset = (page - 1) * size;
        List<TransactionReceiveAdminListDTO> transactions = transactionRepository.getTransByBankAccountAllFilter(
                from, to, bankAccount, id, orderId, terminalCode, subCode, transStatus, offset, size
        );

        int total = transactionRepository.getTotalTransByBankAccountAllFilter(
                from, to, bankAccount, id, orderId, terminalCode, subCode, transStatus);


        boolean hasNext = (offset + transactions.size()) < total;

        TransactionReceivePaginationResponseDTO pagination = new TransactionReceivePaginationResponseDTO();
        pagination.setTotal(total);
        pagination.setItems(transactions);
        pagination.setPage(page);
        pagination.setLimit(size);
        pagination.setHasNext(hasNext);

        return pagination;
    }

    @Override
    public TransactionReceivePaginationResponseDTO getTransactionsWithPaginationAllFilter(int page, int size) {
        int offset = (page - 1) * size;
        List<TransactionReceiveAdminListDTO> transactions = transactionRepository.getTransactionsWithPaginationAllFilter(offset, size);

        int total = transactionRepository.getTotalTransactionsWithPaginationAllFilter();


        boolean hasNext = (page + transactions.size()) < total;

        TransactionReceivePaginationResponseDTO pagination = new TransactionReceivePaginationResponseDTO();
        pagination.setTotal(total);
        pagination.setItems(transactions);
        pagination.setPage(page);
        pagination.setLimit(size);
        pagination.setHasNext(hasNext);

        return pagination;
    }

    @Override
    public TransactionReceivePaginationResponseDTO getTransactionsWithPaginationByOption(RequestFilterTransactionRequest request) {
        if (request == null) {
            TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = getTransactionsWithPaginationAllFilter(request.getPage(), request.getSize());
            return transactionsWithPaginationAllFilter;
        }

        if (request.getTypeA() != null && request.getTypeB() == null && request.getTypeC() == null && request.getTypeD() == null) {
            //filter transaction from to paging
            int offset = (request.getPage() - 1) * request.getSize();
            List<TransactionReceiveAdminListDTO> transactions =
                    transactionRepository.getTransactionsWithPaginationFilterByTimeCreate(request.getTypeA().getFrom(), request.getTypeA().getTo(), offset, request.getSize());

            //count total of transaction filter
            int total = transactionRepository.getTotalTransactionsWithPaginationFilterByTimeCreate(request.getTypeA().getFrom(), request.getTypeA().getTo());

            //check if has next page
            boolean hasNext = (offset + transactions.size()) < total;

            //return response
            TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
            transactionsWithPaginationAllFilter.setTotal(total);
            transactionsWithPaginationAllFilter.setItems(transactions);
            transactionsWithPaginationAllFilter.setPage(request.getPage());
            transactionsWithPaginationAllFilter.setLimit(request.getSize());
            transactionsWithPaginationAllFilter.setHasNext(hasNext);

            return transactionsWithPaginationAllFilter;
        } else if (request.getTypeA() == null && request.getTypeB() != null && request.getTypeC() == null && request.getTypeD() == null) {
            // set default for search all
            TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = getTransactionsWithPaginationAllFilter(request.getPage(), request.getSize());
            return transactionsWithPaginationAllFilter;
        } else if (request.getTypeA() == null && request.getTypeB() == null && request.getTypeC() != null && request.getTypeD() == null) {
            if (request.getTypeC().getBankAccount() != null && !request.getTypeC().getBankAccount().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByBankAccount(request.getTypeC().getBankAccount(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByBankAccount(request.getTypeC().getBankAccount());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }
            if (request.getTypeC().getReferenceNumber() != null && !request.getTypeC().getReferenceNumber().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByReferenceNumber(request.getTypeC().getReferenceNumber(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByBankAccount(request.getTypeC().getReferenceNumber());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getTerminalCode() != null && !request.getTypeC().getTerminalCode().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByTerminalCode(request.getTypeC().getTerminalCode(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByReferenceNumber(request.getTypeC().getTerminalCode());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getSubCode() != null && !request.getTypeC().getSubCode().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterBySubCode(request.getTypeC().getSubCode(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterBySubCode(request.getTypeC().getSubCode());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getOrderId() != null && !request.getTypeC().getOrderId().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByOrderId(request.getTypeC().getOrderId(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByOrderId(request.getTypeC().getOrderId());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getContent() != null && !request.getTypeC().getContent().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByContent(request.getTypeC().getContent(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByContent(request.getTypeC().getContent());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = getTransactionsWithPaginationAllFilter(request.getPage(), request.getSize());
            return transactionsWithPaginationAllFilter;
        } else if (request.getTypeA() == null && request.getTypeB() == null && request.getTypeC() == null && request.getTypeD() != null) {

            //filter transaction from to paging
            int offset = (request.getPage() - 1) * request.getSize();
            List<TransactionReceiveAdminListDTO> transactions =
                    transactionRepository.getTransactionsWithPaginationFilterByStatus(request.getTypeD().getStatus(), offset, request.getSize());

            //count total of transaction filter
            int total = transactionRepository.getTotalTransactionsWithPaginationFilterByStatus(request.getTypeD().getStatus());

            //check if has next page
            boolean hasNext = (offset + transactions.size()) < total;

            //return response
            TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
            transactionsWithPaginationAllFilter.setTotal(total);
            transactionsWithPaginationAllFilter.setItems(transactions);
            transactionsWithPaginationAllFilter.setPage(request.getPage());
            transactionsWithPaginationAllFilter.setLimit(request.getSize());
            transactionsWithPaginationAllFilter.setHasNext(hasNext);

            return transactionsWithPaginationAllFilter;
        } else if (request.getTypeA() != null && request.getTypeB() == null && request.getTypeC() != null && request.getTypeD() == null) {
            if (request.getTypeC().getBankAccount() != null && !request.getTypeC().getBankAccount().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByCreateTimeAndBankAccount(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getBankAccount(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByCreateTimeAndBankAccount(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getBankAccount());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getReferenceNumber() != null && !request.getTypeC().getReferenceNumber().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByCreateTimeAndReferenceNumber(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getReferenceNumber(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByCreateTimeAndReferenceNumber(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getReferenceNumber());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getOrderId() != null && !request.getTypeC().getOrderId().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByCreateTimeAndOrderId(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getOrderId(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByCreateTimeAndOrderId(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getOrderId());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getTerminalCode() != null && !request.getTypeC().getTerminalCode().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByCreateTimeAndTerminalCode(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getTerminalCode(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByCreateTimeAndTerminalCode(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getTerminalCode());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getSubCode() != null && !request.getTypeC().getSubCode().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByCreateTimeAndTerminalCode(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getSubCode(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByCreateTimeAndTerminalCode(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getSubCode());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getContent() != null && !request.getTypeC().getContent().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByCreateTimeAndTerminalCode(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getContent(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByCreateTimeAndTerminalCode(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeC().getContent());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

        } else if (request.getTypeA() != null && request.getTypeB() == null && request.getTypeC() == null && request.getTypeD() != null) {
            int offset = (request.getPage() - 1) * request.getSize();
            List<TransactionReceiveAdminListDTO> transactions =
                    transactionRepository.getTransactionsWithPaginationFilterByCreateTimeAndStatus(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus(), offset, request.getSize());

            //count total of transaction filter
            int total = transactionRepository.getTotalTransactionsWithPaginationFilterByCreateTimeAndStatus(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus());

            //check if has next page
            boolean hasNext = (offset + transactions.size()) < total;

            //return response
            TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
            transactionsWithPaginationAllFilter.setTotal(total);
            transactionsWithPaginationAllFilter.setItems(transactions);
            transactionsWithPaginationAllFilter.setPage(request.getPage());
            transactionsWithPaginationAllFilter.setLimit(request.getSize());
            transactionsWithPaginationAllFilter.setHasNext(hasNext);

            return transactionsWithPaginationAllFilter;
        } else if (request.getTypeA() == null && request.getTypeB() == null && request.getTypeC() != null && request.getTypeD() != null) {
            if (request.getTypeC().getBankAccount() != null && !request.getTypeC().getBankAccount().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByStatusAndBankAccount(request.getTypeD().getStatus(), request.getTypeC().getBankAccount(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByStatusAndBankAccount(request.getTypeD().getStatus(), request.getTypeC().getBankAccount());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getReferenceNumber() != null && !request.getTypeC().getReferenceNumber().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByStatusAndReferenceNumber(request.getTypeD().getStatus(), request.getTypeC().getReferenceNumber(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByStatusAndReferenceNumber(request.getTypeD().getStatus(), request.getTypeC().getReferenceNumber());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getOrderId() != null && !request.getTypeC().getOrderId().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByStatusAndOrderId(request.getTypeD().getStatus(), request.getTypeC().getOrderId(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByStatusAndOrderId(request.getTypeD().getStatus(), request.getTypeC().getOrderId());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getTerminalCode() != null && !request.getTypeC().getTerminalCode().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByStatusAndTerminalCode(request.getTypeD().getStatus(), request.getTypeC().getTerminalCode(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByStatusAndTerminalCode(request.getTypeD().getStatus(), request.getTypeC().getTerminalCode());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getSubCode() != null && !request.getTypeC().getSubCode().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByStatusAndSubCode(request.getTypeD().getStatus(), request.getTypeC().getSubCode(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByStatusAndSubCode(request.getTypeD().getStatus(), request.getTypeC().getSubCode());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getContent() != null && !request.getTypeC().getContent().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByStatusAndContent(request.getTypeD().getStatus(), request.getTypeC().getContent(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByStatusAndContent(request.getTypeD().getStatus(), request.getTypeC().getContent());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }
        } else if (request.getTypeA() != null && request.getTypeB() == null && request.getTypeC() != null && request.getTypeD() != null) {
            if (request.getTypeC().getBankAccount() != null && !request.getTypeC().getBankAccount().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByTimeCreateAndStatusAndBankAccount(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus(), request.getTypeC().getBankAccount(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndBankAccount(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus(), request.getTypeC().getBankAccount());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getReferenceNumber() != null && !request.getTypeC().getReferenceNumber().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByTimeCreateAndStatusAndReferenceNumber(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus() ,request.getTypeC().getReferenceNumber(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndReferenceNumber(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus(), request.getTypeC().getReferenceNumber());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getOrderId() != null && !request.getTypeC().getOrderId().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByTimeCreateAndStatusAndOrderId(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus() ,request.getTypeC().getOrderId(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndOrderId(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus(), request.getTypeC().getOrderId());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getTerminalCode() != null && !request.getTypeC().getTerminalCode().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByTimeCreateAndStatusAndTerminalCode(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus() ,request.getTypeC().getTerminalCode(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndTerminalCode(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus(),request.getTypeC().getTerminalCode());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getSubCode() != null && !request.getTypeC().getSubCode().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByTimeCreateAndStatusAndSubCode(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus(),request.getTypeC().getSubCode(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndSubCode(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus(),request.getTypeC().getSubCode());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }

            if (request.getTypeC().getContent() != null && !request.getTypeC().getContent().isEmpty()) {
                //filter transaction from to paging
                int offset = (request.getPage() - 1) * request.getSize();
                List<TransactionReceiveAdminListDTO> transactions =
                        transactionRepository.getTransactionsWithPaginationFilterByTimeCreateAndStatusAndContent(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus(),request.getTypeC().getContent(), offset, request.getSize());

                //count total of transaction filter
                int total = transactionRepository.getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndContent(request.getTypeA().getFrom(), request.getTypeA().getTo(), request.getTypeD().getStatus(),request.getTypeC().getContent());

                //check if has next page
                boolean hasNext = (offset + transactions.size()) < total;

                //return response
                TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = new TransactionReceivePaginationResponseDTO();
                transactionsWithPaginationAllFilter.setTotal(total);
                transactionsWithPaginationAllFilter.setItems(transactions);
                transactionsWithPaginationAllFilter.setPage(request.getPage());
                transactionsWithPaginationAllFilter.setLimit(request.getSize());
                transactionsWithPaginationAllFilter.setHasNext(hasNext);

                return transactionsWithPaginationAllFilter;
            }
        }
        // set default for search all
        TransactionReceivePaginationResponseDTO transactionsWithPaginationAllFilter = getTransactionsWithPaginationAllFilter(request.getPage(), request.getSize());
        return transactionsWithPaginationAllFilter;
    }
}
