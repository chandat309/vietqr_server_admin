package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.RefreshToken;
import com.vietqradminbe.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    @Query("SELECT r FROM RefreshToken r WHERE r.token = :token")
    Optional<RefreshToken> findByToken(@Param("token")String token);
    @Modifying
    int deleteByUser(User user);
}
