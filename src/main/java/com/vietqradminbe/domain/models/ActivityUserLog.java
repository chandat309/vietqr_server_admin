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
@Table(name = "activity_user_log")
public class ActivityUserLog implements Serializable {
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

    @Column(name = "time_log", updatable = false, nullable = false)
    String timeLog;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;
}
