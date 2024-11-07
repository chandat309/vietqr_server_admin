package com.vietqradminbe.application.services;

import com.vietqradminbe.application.mappers.GroupFunctionMapper;
import com.vietqradminbe.application.services.interfaces.IGroupFunctionService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.GroupFunction;
import com.vietqradminbe.domain.repositories.GroupFunctionRepository;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.request.GroupFunctionCreationRequest;
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
public class GroupFunctionService implements IGroupFunctionService {
    @Autowired
    GroupFunctionRepository groupFunctionRepository;
    GroupFunctionMapper groupFunctionMapper;

    @Override
    public void createGroupFunctionRequest(GroupFunctionCreationRequest request) {
        List<GroupFunction> groupFunctions = groupFunctionRepository.getGroupFunctionByGroupName(request.getGroupName().trim());
        if (groupFunctions.isEmpty()) {
            GroupFunction groupFunction = groupFunctionMapper.toGroupFunction(request);
            groupFunction.setGroupName(request.getGroupName().trim());
            groupFunction.setCreateAt(TimeHelperUtil.getCurrentTime());
            groupFunction.setId(UUID.randomUUID().toString());
            groupFunction.setDescription(request.getDescription().trim());
            groupFunction.setAvailable(false);
            groupFunctionRepository.save(groupFunction);
        } else throw new BadRequestException(ErrorCode.ROLE_NAME_EXISTED);
    }

    @Override
    public List<GroupFunction> getAllGroupFunctions() {
        return groupFunctionRepository.findAll();
    }
}
