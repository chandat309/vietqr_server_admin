package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.repositories;

import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.entities.TransactionReceiveEntity;
import com.vietqradminbe.web.dto.response.interfaces.TransactionRefundAdminDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRefundRepository extends JpaRepository<TransactionReceiveEntity, String> {

    @Query(value = "SELECT tr.status, tr.amount, tr.reference_number, tr2.order_id " +
            "                       FROM transaction_refund tr, transaction_receive tr2 " +
            "                       WHERE tr.ref_number LIKE CONCAT('%', :referenceNumber , '%') AND tr2.id = tr.transaction_id", nativeQuery = true)
    public List<TransactionRefundAdminDetailDTO> getTransactionRefundAdminDetail(@Param("referenceNumber")String referenceNumber);
}
