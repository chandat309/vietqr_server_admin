package com.vietqradminbe.application.mappers;

import com.vietqradminbe.domain.models.Role;
import com.vietqradminbe.web.dto.request.RoleCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "id", ignore = true)
    Role toRole(RoleCreationRequest role);

}
