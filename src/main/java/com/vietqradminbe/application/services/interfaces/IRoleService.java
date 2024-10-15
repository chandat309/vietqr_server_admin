package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.Role;
import com.vietqradminbe.web.dto.request.RoleCreationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IRoleService {
    public List<Role> getAllRoles();
    public List<Role> getRoleByRoleName(String roleName);
    public List<String> getRolesNameByUsername(String username);
    public void createRoleRequest(RoleCreationRequest request);
}
