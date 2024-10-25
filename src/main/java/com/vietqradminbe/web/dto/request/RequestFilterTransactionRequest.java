package com.vietqradminbe.web.dto.request;

import com.vietqradminbe.web.dto.request.filtertype.TypeA;
import com.vietqradminbe.web.dto.request.filtertype.TypeB;
import com.vietqradminbe.web.dto.request.filtertype.TypeC;
import com.vietqradminbe.web.dto.request.filtertype.TypeD;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFilterTransactionRequest implements Serializable {
    TypeA typeA;
    TypeB typeB;
    TypeC typeC;
    TypeD typeD;
    int page;
    int size;
}
