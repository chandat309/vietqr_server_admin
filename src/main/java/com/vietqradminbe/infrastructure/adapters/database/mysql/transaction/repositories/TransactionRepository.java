package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.repositories;

import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.entities.TransactionReceiveEntity;
import com.vietqradminbe.web.dto.response.interfaces.TransReceiveAdminDetailDTO;
import com.vietqradminbe.web.dto.response.interfaces.TransactionReceiveAdminListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionReceiveEntity, String> {

    @Query(value = "SELECT temp.id,bankAccount, temp.amount, bankId, " +
            "            temp.content, orderId, " +
            "            timeCreated, timePaid," +
            "            transStatus, c.bank_short_name as bankShortName," +
            "            statusResponse, terminalCode," +
            "            subCode " +
            "            FROM (SELECT a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "            a.content, a.order_id as orderId, " +
            "            a.time as timeCreated, a.time_paid as timePaid," +
            "            a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "            a.sub_code as subCode FROM transaction_receive a ORDER BY a.time DESC ) AS temp" +
            "            LEFT JOIN account_bank_receive b ON temp.bankAccount = b.id " +
            "            LEFT JOIN bank_type c ON b.bank_type_id = c.id " +
            "            WHERE (:bankAccount IS NULL OR temp.bankAccount = :bankAccount) " +
            "            AND (:from IS NULL OR temp.timeCreated >= :from) " +
            "            AND (:to IS NULL OR temp.timeCreated <= :to) " +
            "            AND (:id IS NULL OR temp.id = :id) " +
            "            AND (:orderId IS NULL OR temp.orderId = :orderId) " +
            "            AND (:terminalCode IS NULL OR temp.terminalCode = :terminalCode) " +
            "            AND (:subCode IS NULL OR temp.subCode = :subCode) " +
            "            AND (:transStatus IS NULL OR temp.transStatus = :transStatus) " +
            "            LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransByBankAccountAllFilter(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("bankAccount") String bankAccount,
            @Param("id") String id,
            @Param("orderId") String orderId,
            @Param("terminalCode") String terminalCode,
            @Param("subCode") String subCode,
            @Param("transStatus") Integer transStatus,
            @Param("offset") int offset,
            @Param("limit") int size
    );


    @Query(value = "SELECT COUNT(temp.id)" +
            "            FROM (SELECT a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "            a.content, a.order_id as orderId, " +
            "            a.time as timeCreated, a.time_paid as timePaid," +
            "            a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "            a.sub_code as subCode FROM transaction_receive a ORDER BY a.time DESC ) AS temp" +
            "            WHERE (:bankAccount IS NULL OR temp.bankAccount = :bankAccount) " +
            "            AND (:from IS NULL OR temp.timeCreated >= :from) " +
            "            AND (:to IS NULL OR temp.timeCreated <= :to) " +
            "            AND (:id IS NULL OR temp.id = :id) " +
            "            AND (:orderId IS NULL OR temp.orderId = :orderId) " +
            "            AND (:terminalCode IS NULL OR temp.terminalCode = :terminalCode) " +
            "            AND (:subCode IS NULL OR temp.subCode = :subCode) " +
            "            AND (:transStatus IS NULL OR temp.transStatus = :transStatus)", nativeQuery = true)
    int getTotalTransByBankAccountAllFilter(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("bankAccount") String bankAccount,
            @Param("id") String id,
            @Param("orderId") String orderId,
            @Param("terminalCode") String terminalCode,
            @Param("subCode") String subCode,
            @Param("transStatus") Integer transStatus
    );

    //case 1: filter all - default filter
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status != 0" +
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationAllFilter(@Param("offset") int offset,
                                                                                @Param("limit") int size);

    @Query(value = "SELECT count(a.id) FROM transaction_receive a WHERE a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationAllFilter();
    //end case 1

    //case 2: filter from to - time create filter
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.`time` >= :from AND a.`time` <= :to AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByTimeCreate(
                                                                                @Param("from") Long from,
                                                                                @Param("to") Long to,
                                                                                @Param("offset") int offset,
                                                                                @Param("limit") int size);

    @Query(value = "SELECT count(a.id) FROM transaction_receive a " +
            "WHERE a.time >= :from AND a.time <= :to AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByTimeCreate(@Param("from") Long from,
                                                             @Param("to") Long to);
    //end case 2

    //case 3: filter merchant name - merchant name filter --- PENDING
//    @Query(value = "SELECT temp.id, temp.amount, temp.timePaid, temp.referenceNumber, temp.orderId," +
//            "temp.terminalCode, temp.subCode, temp.type, temp.timeCreated, " +
//            "temp.bankAccount, temp.bankId, c.bank_short_name as bankShortName, " +
//            "            temp.content, temp.transStatus, temp.statusResponse, temp.note, temp.transType," +
//            "            temp1.name" +
//            "            FROM (SELECT ms.name, mbr.bank_id FROM merchant ms RIGHT JOIN merchant_bank_receive mbr ON mbr.merchant_id = ms.id WHERE ms.name LIKE CONCAT('%', :value, '%')) as temp1," +
//            "            (SELECT a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
//            "            a.content, a.order_id as orderId, " +
//            "            a.time as timeCreated, a.time_paid as timePaid," +
//            "            a.reference_number as referenceNumber," +
//            "            a.type," +
//            "            a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
//            "            a.sub_code as subCode, a.note," +
//            "            a.trans_type as transType" +
//            "            FROM transaction_receive a ORDER BY a.time DESC" +
//            "            ) AS temp" +
//            "            LEFT JOIN account_bank_receive b ON temp.bankId = b.id " +
//            "            LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
//            "            WHERE temp.bankId = temp1.bank_id" +
//            "            LIMIT :offset, :limit", nativeQuery = true)
//    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByMerchantName(
//            @Param("value") String value,
//            @Param("offset") int offset,
//            @Param("limit") int size);
//
//    @Query(value = "SELECT count(*)" +
//            "            FROM (SELECT ms.name, mbr.bank_id FROM merchant ms RIGHT JOIN merchant_bank_receive mbr ON mbr.merchant_id = ms.id WHERE ms.name LIKE CONCAT('%', :value, '%')) as temp1,\n" +
//            "            (SELECT a.bank_id as bankId FROM transaction_receive a) AS temp" +
//            "            LEFT JOIN account_bank_receive b ON temp.bankId = b.id " +
//            "            WHERE temp.bankId = temp1.bank_id", nativeQuery = true)
//    int getTotalTransactionsWithPaginationFilterByMerchantName(@Param("value") Long value);
    //end case 3

    //case 4: filter group transaction - group transaction filter - bankAccount
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.bank_account LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByBankAccount(
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.bank_account LIKE CONCAT('%', :value, '%') AND a.status != 0" , nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByBankAccount(@Param("value") String value);
    //end case 4


    //case 5: filter group transaction - group transaction filter - referenceNumber
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.reference_number LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByReferenceNumber(
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.reference_number LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByReferenceNumber(@Param("value") String value);
    //end case 5


    //case 6: filter group transaction - group transaction filter - orderId
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.order_id LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByOrderId(
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.order_id LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByOrderId(@Param("value") String value);
    //end case 6

    //case 7: filter group transaction - group transaction filter - terminalCode
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.terminal_code LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByTerminalCode(
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.terminal_code LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByTerminalCode(@Param("value") String value);
    //end case 7

    //case 8: filter group transaction - group transaction filter - subCode
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.sub_code LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterBySubCode(
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.sub_code LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterBySubCode(@Param("value") String value);
    //end case 8

    //case 9: filter group transaction - group transaction filter - content
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.content LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByContent(
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.content LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByContent(@Param("value") String value);
    //end case 9


    //case 10: filter transaction status - search by transaction status
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status" +
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByStatus(
            @Param("status") int status,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByStatus(@Param("status") int status);
    //end case 10


    //case 11: filter transaction by time create from to && group transaction filter - bankAccount
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.time >= :from AND a.time <= :to AND a.bank_account LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByCreateTimeAndBankAccount(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.time >= :from AND a.time <= :to AND a.bank_account LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByCreateTimeAndBankAccount(@Param("from") Long from,
                                                                           @Param("to") Long to,
                                                                           @Param("value") String value);
    //end case 11

    //case 12: filter transaction by time create from to && group transaction filter - referenceNumber
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.time >= :from AND a.time <= :to AND a.reference_number LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByCreateTimeAndReferenceNumber(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.time >= :from AND a.time <= :to AND a.reference_number LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByCreateTimeAndReferenceNumber(@Param("from") Long from,
                                                                           @Param("to") Long to,
                                                                           @Param("value") String value);
    //end case 12


    //case 13: filter transaction by time create from to && group transaction filter - orderId
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.time >= :from AND a.time <= :to AND a.order_id LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByCreateTimeAndOrderId(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "            WHERE a.time >= :from AND a.time <= :to AND a.order_id LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByCreateTimeAndOrderId(@Param("from") Long from,
                                                                               @Param("to") Long to,
                                                                               @Param("value") String value);
    //end case 13


    //case 14: filter transaction by time create from to && group transaction filter - terminalCode
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.time >= :from AND a.time <= :to AND a.terminal_code LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByCreateTimeAndTerminalCode(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "            WHERE a.time >= :from AND a.time <= :to AND a.terminal_code LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByCreateTimeAndTerminalCode(@Param("from") Long from,
                                                                       @Param("to") Long to,
                                                                       @Param("value") String value);
    //end case 14



    //case 15: filter transaction by time create from to && group transaction filter - subCode
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.time >= :from AND a.time <= :to AND a.sub_code LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByCreateTimeAndSubCode(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "            WHERE a.time >= :from AND a.time <= :to AND a.sub_code LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByCreateTimeAndSubCode(@Param("from") Long from,
                                                                            @Param("to") Long to,
                                                                            @Param("value") String value);
    //end case 15


    //case 16: filter transaction by time create from to && group transaction filter - content
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.time >= :from AND a.time <= :to AND a.content LIKE CONCAT('%', :value, '%') AND a.status != 0"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByCreateTimeAndContent(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "            WHERE a.time >= :from AND a.time <= :to AND a.content LIKE CONCAT('%', :value, '%') AND a.status != 0", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByCreateTimeAndContent(@Param("from") Long from,
                                                                       @Param("to") Long to,
                                                                       @Param("value") String value);
    //end case 16


    //case 17: filter transaction by time create from to && transaction status
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.time >= :from AND a.time <= :to AND a.status = :status"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByCreateTimeAndStatus(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "            WHERE a.time >= :from AND a.time <= :to AND a.status = :status", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByCreateTimeAndStatus(@Param("from") Long from,
                                                                       @Param("to") Long to,
                                                                       @Param("status") int status);
    //end case 17


    //case 18: filter transaction by status && group transaction filter - bankAccount
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.bank_account LIKE CONCAT('%', :value, '%')"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByStatusAndBankAccount(
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.bank_account LIKE CONCAT('%', :value, '%')", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByStatusAndBankAccount(@Param("status") int status,
                                                                           @Param("value") String value);
    //end case 18


    //case 19: filter transaction by status && group transaction filter - referenceNumber
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.reference_number LIKE CONCAT('%', :value, '%')"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByStatusAndReferenceNumber(
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.reference_number LIKE CONCAT('%', :value, '%')", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByStatusAndReferenceNumber(@Param("status") int status,
                                                                       @Param("value") String value);
    //end case 19


    //case 20: filter transaction by status && group transaction filter - orderId
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.order_id LIKE CONCAT('%', :value, '%')"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByStatusAndOrderId(
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.order_id LIKE CONCAT('%', :value, '%')", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByStatusAndOrderId(@Param("status") int status,
                                                                           @Param("value") String value);
    //end case 20


    //case 21: filter transaction by status && group transaction filter - terminalCode
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.terminal_code LIKE CONCAT('%', :value, '%')"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByStatusAndTerminalCode(
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.terminal_code LIKE CONCAT('%', :value, '%')", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByStatusAndTerminalCode(@Param("status") int status,
                                                                   @Param("value") String value);
    //end case 21


    //case 22: filter transaction by status && group transaction filter - subCode
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.sub_code LIKE CONCAT('%', :value, '%')"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByStatusAndSubCode(
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.sub_code LIKE CONCAT('%', :value, '%')", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByStatusAndSubCode(@Param("status") int status,
                                                                         @Param("value") String value);
    //end case 22



    //case 23: filter transaction by status && group transaction filter - content
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.content LIKE CONCAT('%', :value, '%')"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByStatusAndContent(
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.content LIKE CONCAT('%', :value, '%')", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByStatusAndContent(@Param("status") int status,
                                                                         @Param("value") String value);
    //end case 23

    //case 24: filter transaction by time create from to && status && group transaction filter - bankAccount
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.bank_account LIKE CONCAT('%', :value, '%')"+
            "                        AND a.time >= :from AND a.time <= :to"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByTimeCreateAndStatusAndBankAccount(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.bank_account LIKE CONCAT('%', :value, '%')" +
            "                        AND a.time >= :from AND a.time <= :to", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndBankAccount(
                                                                   @Param("from") Long from,
                                                                   @Param("to") Long to,
                                                                   @Param("status") int status,
                                                                   @Param("value") String value);
    //end case 24


    //case 25: filter transaction by time create from to && status && group transaction filter - referenceNumber
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.reference_number LIKE CONCAT('%', :value, '%')"+
            "                        AND a.time >= :from AND a.time <= :to"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByTimeCreateAndStatusAndReferenceNumber(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.reference_number LIKE CONCAT('%', :value, '%')" +
            "                        AND a.time >= :from AND a.time <= :to", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndReferenceNumber(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value);
    //end case 25

    //case 26: filter transaction by time create from to && status && group transaction filter - orderId
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.order_id LIKE CONCAT('%', :value, '%')"+
            "                        AND a.time >= :from AND a.time <= :to"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByTimeCreateAndStatusAndOrderId(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.order_id LIKE CONCAT('%', :value, '%')" +
            "                        AND a.time >= :from AND a.time <= :to", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndOrderId(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value);
    //end case 26

    //case 27: filter transaction by time create from to && status && group transaction filter - terminalCode
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.terminal_code LIKE CONCAT('%', :value, '%')"+
            "                        AND a.time >= :from AND a.time <= :to"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByTimeCreateAndStatusAndTerminalCode(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.terminal_code LIKE CONCAT('%', :value, '%')" +
            "                        AND a.time >= :from AND a.time <= :to", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndTerminalCode(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value);
    //end case 27


    //case 28: filter transaction by time create from to && status && group transaction filter - subCode
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.sub_code LIKE CONCAT('%', :value, '%')"+
            "                        AND a.time >= :from AND a.time <= :to"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByTimeCreateAndStatusAndSubCode(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.sub_code LIKE CONCAT('%', :value, '%')" +
            "                        AND a.time >= :from AND a.time <= :to", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndSubCode(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value);
    //end case 28

    //case 29: filter transaction by time create from to && status && group transaction filter - content
    @Query(value = "SELECT c.bank_short_name as bankShortName, " +
            "                        a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, " +
            "                        a.content, a.order_id as orderId, " +
            "                        a.time as timeCreated, a.time_paid as timePaid," +
            "                        a.reference_number as referenceNumber," +
            "                        a.type," +
            "                        a.trans_status as transStatus, a.status_response as statusResponse, a.terminal_code as terminalCode," +
            "                        a.sub_code as subCode,a.note, a.trans_type as transType, a.status" +
            "                        FROM transaction_receive a" +
            "                        LEFT JOIN account_bank_receive b ON a.bank_id = b.id " +
            "                        LEFT JOIN bank_type c ON b.bank_type_id = c.id" +
            "                        WHERE a.status = :status AND a.content LIKE CONCAT('%', :value, '%')"+
            "                        AND a.time >= :from AND a.time <= :to"+
            "                        ORDER BY a.`time` DESC" +
            "                        LIMIT :offset, :limit", nativeQuery = true)
    List<TransactionReceiveAdminListDTO> getTransactionsWithPaginationFilterByTimeCreateAndStatusAndContent(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value,
            @Param("offset") int offset,
            @Param("limit") int size);

    @Query(value = "SELECT COUNT(a.id) FROM transaction_receive a" +
            "                        WHERE a.status = :status AND a.content LIKE CONCAT('%', :value, '%')" +
            "                        AND a.time >= :from AND a.time <= :to", nativeQuery = true)
    int getTotalTransactionsWithPaginationFilterByTimeCreateAndStatusAndContent(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("status") int status,
            @Param("value") String value);
    //end case 29





    @Query(value = "SELECT a.id, a.bank_account as bankAccount, a.amount, a.bank_id as bankId, a.content, a.order_id as orderId, a.reference_number as referenceNumber, "
            + "a.status, a.time as timeCreated, a.time_paid as timePaid, a.trans_type as transType, a.sign, a.trace_id as traceId, a.type, b.bank_account_name as userBankName,  "
            + "b.national_id as nationalId, b.phone_authenticated as phoneAuthenticated, b.is_authenticated as sync, c.bank_code as bankCode, c.bank_short_name as bankShortName, c.bank_name as bankName, c.img_id as imgId, "
            + "CASE  "
            + "WHEN b.is_sync = true AND b.mms_active = false THEN 1  "
            + "WHEN b.is_sync = true AND b.mms_active = true THEN 2  "
            + "WHEN b.is_sync = false AND b.mms_active = true THEN 2  "
            + "ELSE 0  "
            + "END as flow, "
            + "a.terminal_code as terminalCode, a.note, "
            + "a.sub_code as subCode, "
            + "a.service_code AS serviceCode, "
            + "a.additional_data as additionalData, "
            + "b.mms_active as mmsActive "
            + "FROM transaction_receive a  "
            + "LEFT JOIN account_bank_receive b  "
            + "ON a.bank_id = b.id  "
            + "LEFT JOIN bank_type c  "
            + "ON b.bank_type_id = c.id  "
            + "WHERE a.id = :transactionId ", nativeQuery = true)
    TransReceiveAdminDetailDTO getDetailTransReceiveAdmin(@Param(value = "transactionId") String transactionId);
}
