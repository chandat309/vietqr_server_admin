package com.vietqradminbe.web.dto.response.interfaces;

public interface TransactionReceiveLogDTO {
    String getId();
    Integer getType();
    String getTransactionId();
    String getStatus();
    Integer getStatusCode();
    String getMessage();
    String getUrlCallback();
    Long getTimeRequest();
    Long getTimeResponse();
}
