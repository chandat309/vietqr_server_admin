package com.vietqradminbe.web.dto.request.filtertype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeA implements Serializable {
    String type;
    Long from;
    Long to;
}
