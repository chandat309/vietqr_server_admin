package com.vietqradminbe.domain.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "username", nullable = false)
    String username;

    @Column(nullable = false)
    String email;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "created_at", updatable = false, nullable = false)
    String createAt;

    @Column(name = "updated_at", nullable = false)
    String updateAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    @JsonIgnore
    List<UserRole> userRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    @JsonIgnore
    List<RefreshToken> refreshTokens;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}"; // Avoid referencing UserRole
    }
}
