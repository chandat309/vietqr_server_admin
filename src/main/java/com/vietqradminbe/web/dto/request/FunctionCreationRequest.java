package com.vietqradminbe.web.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

//khoi tao getter setter thay vi phai tao manual
@Data
//end khoi tao getter setter
//tao constructor voi so luong tham so truyen vao la tat ca va khong can truyen tham so nao vao
@AllArgsConstructor
@NoArgsConstructor
//end tao tham so
//annotation nay giup cho chung ta khong can phai goi gettter, setter thu cong nhu ban dau nua
//ma thay vao do thi ta co the goi truc tiep cac field cua no ra de dung
//VD: UserCreationRequest request = UserCreationRequest.builder().username("abc").build();
@Builder
//end khai bao builder
//set mac dinh access level cho tung field cua entity la pham vi truy cap nhu the nao
//VD: private thi dung nhu ben duoi
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FunctionCreationRequest implements Serializable {
    String featureName;
    String description;
    String groupFunctionId;
}
