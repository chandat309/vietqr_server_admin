package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.web.dto.request.FeatureCreationRequest;
import org.springframework.stereotype.Service;

@Service
public interface IFeatureService {
    public void createFeatureRequest(FeatureCreationRequest request);
}
