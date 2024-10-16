package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.ActionLog;
import com.vietqradminbe.web.dto.request.FeatureCreationRequest;
import org.springframework.stereotype.Service;

@Service
public interface IActionLogService {
    public void createActionLog(ActionLog actionLog);
}
