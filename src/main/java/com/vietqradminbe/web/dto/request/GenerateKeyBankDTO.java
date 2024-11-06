package com.vietqradminbe.web.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenerateKeyBankDTO {
    int duration;
    int numOfKeys;

}
