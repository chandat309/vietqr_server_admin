package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.Feature;
import com.vietqradminbe.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, String> {
    @Query(value = "SELECT * FROM feature AS r WHERE r.feature_name COLLATE utf8mb4_unicode_ci = :feature_name", nativeQuery = true)
    List<Feature> getFeatureByFeatureName(@Param(value = "feature_name")String featureName);
}
