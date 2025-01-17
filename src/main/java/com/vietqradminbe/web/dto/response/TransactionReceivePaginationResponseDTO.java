package com.vietqradminbe.web.dto.response;

import com.vietqradminbe.web.dto.response.interfaces.TransactionReceiveAdminListDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionReceivePaginationResponseDTO {
    boolean hasNext;
    List<TransactionReceiveAdminListDTO> items;
    int limit;
    int page;
    long total;
}
