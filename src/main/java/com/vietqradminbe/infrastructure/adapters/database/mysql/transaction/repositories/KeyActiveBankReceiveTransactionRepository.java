package com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.repositories;

import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.entities.KeyActiveBankReceiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyActiveBankReceiveTransactionRepository extends JpaRepository<KeyActiveBankReceiveEntity, String> {
}
