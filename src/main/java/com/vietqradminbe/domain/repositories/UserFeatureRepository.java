package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.UserFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeatureRepository extends JpaRepository<UserFeature, String> {

}
