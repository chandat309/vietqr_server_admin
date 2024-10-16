package com.vietqradminbe.application.mappers;

import com.vietqradminbe.domain.models.Feature;
import com.vietqradminbe.web.dto.request.FeatureCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeatureMapper {
    @Mapping(target = "id", ignore = true)
    Feature toFeature(FeatureCreationRequest feature);
}
