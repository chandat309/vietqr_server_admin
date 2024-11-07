package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "key_active_bank_receive")
public class KeyActiveBankReceiveEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private String id;

    //0 : inactive
    //1 : active
    @Column(name = "status")
    private int status;

    @Column(name = "duration")
    private int duration;

    @Column(name = "key_active")
    private String keyActive;

    @Column(name = "value_active")
    private String valueActive;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "create_at")
    private long createAt;

    @Column(name = "version")
    private int version;

    @Column(name = "bank_id")
    private String bankId;
}
