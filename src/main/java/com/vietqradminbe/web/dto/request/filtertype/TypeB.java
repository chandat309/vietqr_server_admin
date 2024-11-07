package com.vietqradminbe.web.dto.request.filtertype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeB implements Serializable {
    String type;
    String merchantName;
}
