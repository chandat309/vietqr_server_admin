package com.vietqradminbe.application.mappers;

import com.vietqradminbe.domain.models.Function;
import com.vietqradminbe.domain.models.GroupFunction;
import com.vietqradminbe.web.dto.request.FunctionCreationRequest;
import com.vietqradminbe.web.dto.request.GroupFunctionCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupFunctionMapper {
    @Mapping(target = "id", ignore = true)
    GroupFunction toGroupFunction(GroupFunctionCreationRequest groupFunctionRequest);
}
