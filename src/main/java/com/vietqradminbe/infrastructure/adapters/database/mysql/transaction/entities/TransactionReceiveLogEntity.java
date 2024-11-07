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
@Table(name = "TransactionReceiveLog")
public class TransactionReceiveLogEntity implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "transactionId")
    private String transactionId;

    @Column(name = "status")
    private String status;

    @Column(name = "message")
    private String message;

    @Column(name = "urlCallback")
    private String urlCallback;

    @Column(name = "time")
    private long time;

    @Column(name = "timeResponse")
    private long timeResponse;

    @Column(name = "statusCode")
    private Integer statusCode;

    // 0: GET TOKEN
    // 1: TRANS SYNC
    @Column(name = "type")
    private Integer type;
}
