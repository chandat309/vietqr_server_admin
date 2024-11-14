package com.vietqradminbe.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "key_active_bank_receive")
public class KeyActiveBankReceive implements Serializable {
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

    @Column(name = "bank_account_activated", nullable = false)
    String bankAccountActivated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;

    @Column(name = "activation_time", nullable = false)
    String activationTime;

    @Column(name = "expiration_time", nullable = false)
    String expirationTime;

    @Override
    public String toString() {
        return "KeyActiveBankReceive{id=" + id + ", keyActive='" + keyActive + "'}";
    }
}
