package com.vietqradminbe.application.mappers;

import com.vietqradminbe.domain.models.Function;
import com.vietqradminbe.web.dto.request.FunctionCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FunctionMapper {
    @Mapping(target = "id", ignore = true)
    Function toFunction(FunctionCreationRequest function);
}
