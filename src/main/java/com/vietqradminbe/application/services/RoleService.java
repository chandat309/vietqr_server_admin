package com.vietqradminbe.application.services;

import com.vietqradminbe.application.mappers.RoleMapper;
import com.vietqradminbe.application.services.interfaces.IRoleService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.Role;
import com.vietqradminbe.domain.repositories.RoleRepository;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.request.RoleCreationRequest;
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
public class RoleService implements IRoleService {

    @Autowired
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getRoleByRoleName(String roleName) {
        List<Role> roles = roleRepository.getRolesByRoleName(roleName.trim());
        return roles;
    }

    @Override
    public List<String> getRolesNameByUsername(String username) {
        return roleRepository.getRolesNameByUsername(username);
    }

    @Override
    public void createRoleRequest(RoleCreationRequest request) {
        List<Role> roles = roleRepository.getRolesByRoleName(request.getRoleName());
        if (roles.isEmpty()) {
            Role role = roleMapper.toRole(request);
            role.setRoleName(request.getRoleName().trim());
            role.setCreateAt(TimeHelperUtil.getCurrentTime());
            role.setUpdateAt("");
            role.setId(UUID.randomUUID().toString());
            roleRepository.save(role);
        } else throw new BadRequestException(ErrorCode.ROLE_NAME_EXISTED);
    }
}
