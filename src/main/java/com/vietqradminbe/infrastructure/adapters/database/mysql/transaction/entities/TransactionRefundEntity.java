package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "TransactionRefund")
public class TransactionRefundEntity implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "bankAccount")
    private String bankAccount;

    @Column(name = "bankId")
    private String bankId;

    @Column(name = "content")
    private String content;

    @Column(name = "amount")
    private long amount;

    @Column(name = "time")
    private long time;

    @Column(name = "timePaid")
    private long timePaid;

    // ref reference_number from transaction_receive
    @Column(name = "refNumber")
    private String refNumber;

    @Column(name = "transactionId")
    private String transactionId;

    // transaction status: SUCCESS == 1/FAILED == 0
    @Column(name = "status")
    private int status;

    // default = D
    @Column(name = "transType")
    private String transType;

    @Column(name = "referenceNumber")
    private String referenceNumber;

    @Column(name = "userId")
    private String userId;

    @Column(name = "note")
    private String note;

    @Column(name = "multiTimes")
    private boolean multiTimes;
}
