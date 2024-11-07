package com.vietqradminbe.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @Column(name = "id")
    String id;

    @Column(nullable = false, name = "role_name")
    String roleName;

    @Column(nullable = false, name = "create_at", updatable = false)
    String createAt;

    @Column(nullable = false, name = "update_at")
    String updateAt;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    @JsonIgnore
    List<UserRole> userRoles;



}
