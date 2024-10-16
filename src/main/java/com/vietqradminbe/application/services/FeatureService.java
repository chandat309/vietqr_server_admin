package com.vietqradminbe.application.services;

import com.vietqradminbe.application.mappers.FeatureMapper;
import com.vietqradminbe.application.services.interfaces.IFeatureService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.Feature;
import com.vietqradminbe.domain.models.Role;
import com.vietqradminbe.domain.repositories.FeatureRepository;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.request.FeatureCreationRequest;
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
public class FeatureService implements IFeatureService {
    @Autowired
    FeatureRepository featureRepository;
    FeatureMapper featureMapper;

    @Override
    public void createFeatureRequest(FeatureCreationRequest request) {
        List<Feature> features = featureRepository.getFeatureByFeatureName(request.getFeatureName().trim());
        if (features.isEmpty()) {
            Feature feature = featureMapper.toFeature(request);
            feature.setFeatureName(request.getFeatureName().trim());
            feature.setCreateAt(TimeHelperUtil.getCurrentTime());
            feature.setUpdateAt("");
            feature.setId(UUID.randomUUID().toString());
            feature.setDescription(request.getDescription().trim());
            feature.setIsAvailable(0);
            featureRepository.save(feature);
        } else throw new BadRequestException(ErrorCode.ROLE_NAME_EXISTED);
    }

}
