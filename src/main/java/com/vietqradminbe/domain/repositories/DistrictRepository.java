package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.District;
import com.vietqradminbe.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    @Query(value = "SELECT d.district_code, d.district_name, " +
            "d.district_type, d.province_code FROM district d " +
            "WHERE d.province_code = :province_code", nativeQuery = true)
    List<District> getDistrictByProvinceCode(@Param(value = "province_code") int provinceCode);
}
