package com.vietqradminbe.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "refresh_token")
public class RefreshToken implements Serializable {

    @Id
    @Column(name = "id")
    String id;

    @Column(nullable = false)
    String token;

    @Column(nullable = false, name = "expires_at")
    String expiresAt;

    @Column(nullable = false, updatable = false, name = "created_at")
    String createdAt;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;

}
