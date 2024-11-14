package com.vietqradminbe.web.dto.response.interfaces;


public interface KeyActiveBankReceiveDTO {
    String getId();
    String getCreateAt();
    String getCreateBy();
    int getDuration();
    String getKeyActive();
    String getQrLink();
    String getBankAccountActivated();
    int getStatus();
    String getActivationTime();
    String getExpirationTime();
}
