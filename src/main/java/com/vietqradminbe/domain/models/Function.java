package com.vietqradminbe.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "function")
public class Function implements Serializable {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "function_name", nullable = false)
    String functionName;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "created_at", updatable = false, nullable = false)
    String createAt;

    @Column(name = "update_at", nullable = false)
    String updateAt;

    @Column(name = "is_available", nullable = false)
    int isAvailable;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.PERSIST)
    @JsonIgnore
    List<UserFeature> userFeatures;
}
