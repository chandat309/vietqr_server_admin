package com.vietqradminbe.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "key_active_bank_receive")
public class KeyActiveBankReceive {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "status")
    int status;

    @Column(name = "duration")
    int duration;

    @Column(name = "key_active")
    String keyActive;

    @Column(name = "value_active")
    String valueActive;

    @Column(name = "secret_key")
    String secretKey;

    @Column(name = "create_at", updatable = false, nullable = false)
    String createAt;

    @Column(name = "create_by", nullable = false)
    String createBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;

    @Override
    public String toString() {
        return "KeyActiveBankReceive{id=" + id + ", keyActive='" + keyActive + "'}";
    }
}
