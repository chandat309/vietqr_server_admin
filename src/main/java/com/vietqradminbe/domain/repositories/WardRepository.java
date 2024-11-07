package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.District;
import com.vietqradminbe.domain.models.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    @Query(value = "SELECT w.ward_code, w.ward_name, " +
            "w.ward_type, w.district_code FROM ward w " +
            "WHERE w.district_code = :district_code", nativeQuery = true)
    List<Ward> getWardsByDistrictCode(@Param(value = "district_code") int districtCode);
}
