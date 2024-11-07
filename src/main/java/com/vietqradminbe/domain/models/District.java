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
@Table(name = "district")
public class District implements Serializable {
    @Id
    @Column(name = "district_code")
    Integer districtCode;

    @Column(name = "district_name")
    String districtName;

    @Column(name = "district_type")
    String districtType;

    @Column(name = "province_code")
    Integer provinceCode;
}
