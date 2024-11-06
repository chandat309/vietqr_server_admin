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
@Table(name = "province")
public class Province implements Serializable {
    @Id
    @Column(name = "province_code")
    Integer provinceCode;

    @Column(name = "province_name")
    String provinceName;

    @Column(name = "province_short_name")
    String provinceShortName;

    @Column(name = "province_type")
    String provinceType;
}
