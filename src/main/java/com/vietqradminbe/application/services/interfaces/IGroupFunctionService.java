package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.GroupFunction;
import com.vietqradminbe.web.dto.request.GroupFunctionCreationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IGroupFunctionService {
    public void createGroupFunctionRequest(GroupFunctionCreationRequest request);
    public List<GroupFunction> getAllGroupFunctions();
}
