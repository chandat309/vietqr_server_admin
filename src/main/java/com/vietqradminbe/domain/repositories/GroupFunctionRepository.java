package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.GroupFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupFunctionRepository extends JpaRepository<GroupFunction, String> {
    @Query(value = "SELECT * FROM group_function AS r WHERE r.group_name COLLATE utf8mb4_unicode_ci = :group_name", nativeQuery = true)
    List<GroupFunction> getGroupFunctionByGroupName(@Param(value = "group_name")String groupName);
}
