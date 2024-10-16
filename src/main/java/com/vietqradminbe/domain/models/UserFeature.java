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
@Table(name = "user_feature")
public class UserFeature implements Serializable {
    @Id
    @Column(name = "id")
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    @JsonIgnore
    Feature feature;

    @Column(name = "is_enabled", nullable = false)
    int isEnabled;

    @Override
    public String toString() {
        return "UserFeature{id=" + id + ", featureName='" + feature.getFeatureName() + "'}";
    }
}
