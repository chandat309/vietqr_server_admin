package com.vietqradminbe.application.services;

import com.vietqradminbe.application.services.interfaces.IActionLogService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.ActionLog;
import com.vietqradminbe.domain.models.Role;
import com.vietqradminbe.domain.repositories.ActionLogRepository;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.request.RoleCreationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ActionLogService implements IActionLogService {

    @Autowired
    ActionLogRepository actionLogRepository;


    @Override
    public void createActionLog(ActionLog actionLog) {
        actionLogRepository.save(actionLog);
    }

}
