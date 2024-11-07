package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.ActivityUserLog;
import com.vietqradminbe.web.dto.response.PagingDTO;
import org.springframework.stereotype.Service;

@Service
public interface IActivityUserLogService {
    public void createActivityUserLog(ActivityUserLog actionLog);
    public PagingDTO getAllActivityUserLogs(int page, int size);
}
