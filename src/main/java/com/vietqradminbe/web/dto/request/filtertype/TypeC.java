package com.vietqradminbe.web.dto.request.filtertype;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeC implements java.io.Serializable {
    String type;
    String bankAccount;
    String referenceNumber;
    String orderId;
    String terminalCode;
    String subCode;
    String content;
}
