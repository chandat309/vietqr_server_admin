package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.ActionLog;
import com.vietqradminbe.web.dto.response.interfaces.ActionLogListDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, String> {
    @Query(value = "SELECT al.id, al.created_at AS createAt, al.description, al.email, " +
            "al.firstname, al.lastname, al.phone_number AS phoneNumber, al.updated_at AS updateAt, " +
            "al.username, al.user_id AS userId " +
            "FROM action_log al ORDER BY al.created_at DESC " +
            "LIMIT :offset , :limit", nativeQuery = true)
    List<ActionLogListDTO> getAllLogs(@Param("offset") int offset,
                                      @Param("limit") int size);

    @Query(value = "SELECT COUNT(al.id) FROM action_log al ", nativeQuery = true)
    int getTotalAllLogs();
}
