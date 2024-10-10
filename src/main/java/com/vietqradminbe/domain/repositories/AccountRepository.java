package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * FROM account WHERE username = :username AND available = true", nativeQuery = true)
    Account getAccountByUsername(@Param(value = "username") String username);
    @Query(value = "SELECT * FROM account WHERE email = :email AND available = true", nativeQuery = true)
    Account getAccountByEmail(@Param(value = "email") String email);
    @Query(value = "SELECT acc_tb.role FROM account AS acc_tb", nativeQuery = true)
    List<String> getAllRoles();
}
