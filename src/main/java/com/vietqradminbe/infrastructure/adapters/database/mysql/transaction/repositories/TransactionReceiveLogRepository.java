package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.repositories;

import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.entities.TransactionReceiveLogEntity;
import com.vietqradminbe.web.dto.response.interfaces.TransactionReceiveLogDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionReceiveLogRepository extends JpaRepository<TransactionReceiveLogEntity, String> {

    @Query(value = "SELECT id AS id, type AS type, status AS status, "
            + "time AS timeRequest, time_response AS timeResponse, "
            + "status_code AS statusCode, message AS message, transaction_id AS transactionId, "
            + "url_callback AS urlCallback "
            + "FROM transaction_receive_log WHERE transaction_id = :transactionId ", nativeQuery = true)
    List<TransactionReceiveLogDTO> getTransactionLogsByTransId(@Param(value = "transactionId") String transactionId);
}
