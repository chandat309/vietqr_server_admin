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
@Table(name = "action_log")
public class ActionLog implements Serializable {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "username", nullable = false)
    String username;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String firstname;

    @Column(nullable = false)
    String lastname;

    @Column(name = "phone_number",nullable = false)
    String phoneNumber;

    @Column(name = "created_at", updatable = false, nullable = false)
    String createAt;

    @Column(name = "updated_at", nullable = false)
    String updateAt;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;
}
