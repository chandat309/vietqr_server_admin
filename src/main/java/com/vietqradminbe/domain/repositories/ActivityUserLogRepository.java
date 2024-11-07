package com.vietqradminbe.domain.repositories;

import com.vietqradminbe.domain.models.ActivityUserLog;
import com.vietqradminbe.web.dto.response.interfaces.ActivityUserLogListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityUserLogRepository extends JpaRepository<ActivityUserLog, String> {
    @Query(value = "SELECT al.id, al.time_log AS timeLog, al.description, al.email, " +
            "al.firstname, al.lastname, al.phone_number AS phoneNumber, " +
            "al.username, al.user_id AS userId " +
            "FROM activity_user_log al ORDER BY al.time_log DESC " +
            "LIMIT :offset , :limit", nativeQuery = true)
    List<ActivityUserLogListDTO> getAllLogs(@Param("offset") int offset,
                                            @Param("limit") int size);

    @Query(value = "SELECT COUNT(al.id) FROM activity_user_log al ", nativeQuery = true)
    int getTotalAllLogs();
}
