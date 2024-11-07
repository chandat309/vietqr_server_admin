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
@Table(name = "group_function")
public class GroupFunction implements Serializable {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "group_name", nullable = false)
    String groupName;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "created_at", nullable = false)
    String createAt;

    @Column(name = "is_avaiable", nullable = false)
    boolean isAvailable;
}
