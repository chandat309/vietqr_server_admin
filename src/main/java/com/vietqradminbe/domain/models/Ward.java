package com.vietqradminbe.domain.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "ward")
public class Ward implements Serializable {
    @Id
    @Column(name = "ward_code")
    Integer wardCode;

    @Column(name = "ward_name")
    String wardName;

    @Column(name = "ward_type")
    String wardType;

    @Column(name = "district_code")
    Integer districtCode;
}
