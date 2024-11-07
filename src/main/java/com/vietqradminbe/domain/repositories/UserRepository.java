package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.Role;
import com.vietqradminbe.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u JOIN FETCH u.userRoles WHERE u.username = :username")
    User getUserByUsername(@Param(value = "username") String username);
    @Query(value = "SELECT * FROM user WHERE email = :email AND available = true", nativeQuery = true)
    User getUserByEmail(@Param(value = "email") String email);
    @Query(value = "SELECT user_tb.role FROM user AS user_tb", nativeQuery = true)
    List<String> getAllUsers();
    @Query("SELECT u FROM User u JOIN RefreshToken rt ON u.id = rt.user.id WHERE rt.token = :refreshToken")
    Optional<User> getUserByRefreshToken(@Param(value = "refreshToken")String refreshToken);


}



