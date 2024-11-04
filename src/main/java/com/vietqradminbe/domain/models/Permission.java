package com.vietqradminbe.domain.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "permission")
public class Permission implements java.io.Serializable {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "function_id", nullable = false)
    String functionId;

    @Column(name = "role_id", nullable = false)
    String roleId;

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "path", nullable = false)
    String path;

    @Column(name = "method", nullable = false)
    String method;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "created_at", nullable = false)
    String createAt;

    @Column(name = "update_at", nullable = false)
    String updateAt;

    @Column(name = "is_available", nullable = false)
    boolean isAvailable;
}
