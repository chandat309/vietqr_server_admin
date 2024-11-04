package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "TransactionReceive")
//@SqlResultSetMapping(
//        name = "TransactionReceiveAdminListResponse",
//        classes = @ConstructorResult(
//                targetClass = TransactionReceiveAdminListDTO.class,
//                columns = {
//                        @ColumnResult(name = "id", type = String.class),
//                        @ColumnResult(name = "bank_account", type = String.class),
//                }
//        )
//)
public class TransactionReceiveEntity {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "bankAccount")
    String bankAccount;

    @Column(name = "bankId")
    String bankId;

    @Column(name = "content")
    String content;

    @Column(name = "amount")
    long amount;

    @Column(name = "time")
    long time;

    @Column(name = "timePaid")
    long timePaid;

    // ref id from transaction_bank/(transaction_sms:removed)
    @Column(name = "refId")
    String refId;

    // transaction type: From Bank/(SMS:removed)
    @Column(name = "type")
    int type;

    // transaction status: PAID/UNPAID
    @Column(name = "status")
    int status;

    @Column(name = "traceId")
    String traceId;

    @Column(name = "transType")
    String transType;

    @Column(name = "referenceNumber")
    String referenceNumber;

    // for customers
    @Column(name = "orderId")
    String orderId;

    @Column(name = "sign")
    String sign;

    @Column(name = "customerBankAccount")
    String customerBankAccount;

    @Column(name = "customerBankCode")
    String customerBankCode;

    @Column(name = "customerName")
    String customerName;

    @Column(name = "terminalCode")
    String terminalCode;

    @Column(name = "serviceCode")
    String serviceCode = "";

    @Column(name = "qrCode")
    String qrCode;

    @Column(name = "userId")
    String userId;

    @Column(name = "note")
    String note;

    // 0: init
    // 1: success - Thanh cong
    // 2: pending - Cho xử lý
    // 3: failed - That bai
    // 4: error - Loi
    @Column(name = "transStatus")
    Integer transStatus;

    @Column(name = "urlLink")
    String urlLink = "";

    @Column(name = "billId")
    String billId = "";

    @Column(name = "hashTag")
    String hashTag = "";

    @Column(name = "additionalData", columnDefinition = "JSON")
    String additionalData = "[]";

    @Column(name = "subCode")
    String subCode = "";

}
