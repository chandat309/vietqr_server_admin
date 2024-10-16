package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, String> {
}
