package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.Function;
import com.vietqradminbe.web.dto.request.FunctionCreationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IFunctionService {
    public void createFunctionRequest(FunctionCreationRequest request);
    public List<Function> getAllFunctions();
}
