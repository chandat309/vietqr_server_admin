package com.vietqradminbe.web.dto.response.interfaces;

import org.springframework.beans.factory.annotation.Value;

public interface TransReceiveAdminDetailDTO {
    @Value("#{target.id ?: ''}")
    String getId();

    @Value("#{target.bankAccount ?: ''}")
    String getBankAccount();

    @Value("#{target.amount ?: 0}")
    Integer getAmount();

    @Value("#{target.bankId ?: ''}")
    String getBankId();

    @Value("#{target.content ?: ''}")
    String getContent();

    @Value("#{target.orderId ?: ''}")
    String getOrderId();

    @Value("#{target.referenceNumber ?: ''}")
    String getReferenceNumber();

    @Value("#{target.status ?: 0}")
    Integer getStatus();

    @Value("#{target.timeCreated ?: 0L}")
    Long getTimeCreated();

    @Value("#{target.timePaid ?: 0L}")
    Long getTimePaid();

    @Value("#{target.transType ?: ''}")
    String getTransType();

    @Value("#{target.sign ?: ''}")
    String getSign();

    @Value("#{target.traceId ?: ''}")
    String getTraceId();

    @Value("#{target.type ?: 0}")
    Integer getType();

    @Value("#{target.userBankName ?: ''}")
    String getUserBankName();

    @Value("#{target.nationalId ?: ''}")
    String getNationalId();

    @Value("#{target.phoneAuthenticated ?: ''}")
    String getPhoneAuthenticated();

    Boolean getSync();

    @Value("#{target.bankCode ?: ''}")
    String getBankCode();

    @Value("#{target.bankShortName ?: ''}")
    String getBankShortName();

    @Value("#{target.bankName ?: ''}")
    String getBankName();

    @Value("#{target.imgId ?: ''}")
    String getImgId();

    @Value("#{target.flow ?: 0}")
    Integer getFlow();

    @Value("#{target.terminalCode ?: ''}")
    String getTerminalCode();

    @Value("#{target.subCode ?: ''}")
    String getSubCode();

    @Value("#{target.note ?: ''}")
    String getNote();

    @Value("#{target.serviceCode ?: ''}")
    String getServiceCode();

    @Value("#{target.additionalData ?: ''}")
    String getAdditionalData();

    @Value("#{target.mmsActive ?: 0}")
    boolean getMmsActive();
}
