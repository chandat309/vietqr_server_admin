package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.ActionLog;
import com.vietqradminbe.web.dto.request.FeatureCreationRequest;
import com.vietqradminbe.web.dto.response.PagingDTO;
import com.vietqradminbe.web.dto.response.interfaces.ActionLogListDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IActionLogService {
    public void createActionLog(ActionLog actionLog);
    public PagingDTO getAllActionLogs(int page, int size);
}
