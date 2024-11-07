package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionRepository extends JpaRepository<Function, String> {
    @Query(value = "SELECT * FROM function AS r WHERE r.function_name COLLATE utf8mb4_unicode_ci = :function_name", nativeQuery = true)
    List<Function> getFunctionByFeatureName(@Param(value = "function_name")String functionName);
}
