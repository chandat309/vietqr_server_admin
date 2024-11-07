package com.vietqradminbe.web.dto.response.interfaces;

public interface TransactionRefundAdminDetailDTO {
    Integer getStatus();

    Integer getAmount();

    String getReferenceNumber();

    String getOrderId();
}
