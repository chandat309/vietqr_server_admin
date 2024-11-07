package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.Role;
import com.vietqradminbe.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @Query(value = "SELECT * FROM user_role AS us_r, role AS r WHERE user_id = us_r.user_id", nativeQuery = true)
    List<Role> getRolesByUserId(@Param(value = "user_id")UUID userId);

    @Query(value = "SELECT * FROM role AS r WHERE r.role_name COLLATE utf8mb4_unicode_ci = :role_name", nativeQuery = true)
    List<Role> getRolesByRoleName(@Param(value = "role_name")String roleName);
    @Query("SELECT r FROM Role r JOIN UserRole ur ON ur.role.id = r.id JOIN User u ON ur.user.id = u.id WHERE u.username = :username")
    List<Role> getRolesByUsername(@Param(value = "username") String username);
    @Query("SELECT r.roleName FROM Role r JOIN UserRole ur ON ur.role.id = r.id JOIN User u ON ur.user.id = u.id WHERE u.username = :username")
    List<String> getRolesNameByUsername(@Param("username") String username);
}
