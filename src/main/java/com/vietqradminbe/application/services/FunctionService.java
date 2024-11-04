package com.vietqradminbe.application.services;

import com.vietqradminbe.application.mappers.FunctionMapper;
import com.vietqradminbe.application.services.interfaces.IFunctionService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.Function;
import com.vietqradminbe.domain.repositories.FunctionRepository;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.request.FunctionCreationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FunctionService implements IFunctionService {
    @Autowired
    FunctionRepository functionRepository;
    FunctionMapper functionMapper;

    @Override
    public void createFunctionRequest(FunctionCreationRequest request) {
        List<Function> features = functionRepository.getFunctionByFeatureName(request.getFeatureName().trim());
        if (features.isEmpty()) {
            Function function = functionMapper.toFunction(request);
            function.setFunctionName(request.getFeatureName().trim());
            function.setCreateAt(TimeHelperUtil.getCurrentTime());
            function.setUpdateAt("");
            function.setId(UUID.randomUUID().toString());
            function.setDescription(request.getDescription().trim());
            function.setIsAvailable(0);
            functionRepository.save(function);
        } else throw new BadRequestException(ErrorCode.ROLE_NAME_EXISTED);
    }

    @Override
    public List<Function> getAllFunctions() {
        List<Function> features = functionRepository.findAll();
        return features;
    }

}
