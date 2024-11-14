package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.KeyActiveBankReceive;
import com.vietqradminbe.web.dto.response.interfaces.KeyActiveBankReceiveDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyActiveBankReceiveRepository extends JpaRepository<KeyActiveBankReceive, String> {
    @Query(value = "SELECT key_active AS keyActive "
            + "FROM key_active_bank_receive "
            + "WHERE key_active IN (:keyActives) ", nativeQuery = true)
    List<String> checkDuplicatedKeyActives(@Param(value = "keyActives") List<String> keyActives);

    @Query(value = "SELECT k.id, k.create_at AS createAt, k.create_by AS createBy, k.duration, " +
            "k.key_active AS keyActive, CONCAT('https://vietqr.vn/service-active?key=', k.key_active) AS qrLink, " +
            "k.status as status, k.bank_account_activated as bankAccountActivated, k.activation_time AS activationTime, " +
            "k.expiration_time AS expirationTime " +
            "FROM key_active_bank_receive k " +
            "WHERE k.create_at BETWEEN :startDate AND :endDate " +
            "ORDER BY k.create_at DESC " +
            "LIMIT :offset , :limit", nativeQuery = true)
    List<KeyActiveBankReceiveDTO> findAllByCreateAtBetween(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("offset") int offset,
            @Param("limit") int limit);

    @Query(value = "SELECT COUNT(k.id) FROM key_active_bank_receive k " +
            "WHERE k.create_at BETWEEN :startDate AND :endDate", nativeQuery = true)
    int countByCreateAtBetween(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    @Query(value = "SELECT k.id, k.create_at AS createAt, k.create_by AS createBy, k.duration, " +
            "k.key_active AS keyActive, CONCAT('https://vietqr.vn/service-active?key=', k.key_active) AS qrLink, " +
            "k.status AS status, k.activation_time AS activationTime, " +
            "k.expiration_time AS expirationTime " +
            "FROM key_active_bank_receive k " +
            "WHERE k.key_active IN :keys", nativeQuery = true)
    List<KeyActiveBankReceiveDTO> findByKeyActiveIn(@Param("keys") List<String> keys);

}
