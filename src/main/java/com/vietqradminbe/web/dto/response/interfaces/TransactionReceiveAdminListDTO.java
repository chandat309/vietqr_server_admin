package com.vietqradminbe.web.dto.response.interfaces;

import org.springframework.beans.factory.annotation.Value;

public interface TransactionReceiveAdminListDTO {
    @Value("#{target.id ?: ''}")
    String getId();

    @Value("#{target.amount ?: 0L}")
    Long getAmount();

    @Value("#{target.timePaid ?: 0L}")
    Long getTimePaid();

    @Value("#{target.referenceNumber ?: ''}")
    String getReferenceNumber();

    @Value("#{target.orderId ?: ''}")
    String getOrderId();

    @Value("#{target.terminalCode ?: ''}")
    String getTerminalCode();

    @Value("#{target.subCode ?: ''}")
    String getSubCode();

    @Value("#{target.timeCreated ?: 0L}")
    Long getTimeCreated();

    @Value("#{target.bankAccount ?: ''}")
    String getBankAccount();

    @Value("#{target.bankShortName ?: ''}")
    String getBankShortName();

    @Value("#{target.status ?: 0}")
    Integer getStatus();

    @Value("#{target.content ?: ''}")
    String getContent();

    @Value("#{target.transStatus ?: 0}")
    Integer getTransStatus();

    @Value("#{target.type ?: 0}")
    Integer getType();

    @Value("#{target.statusResponse ?: 0}")
    Integer getStatusResponse();

    @Value("#{target.transType ?: ''}")
    String getTransType();

    @Value("#{target.note ?: ''}")
    String getNote();

}
