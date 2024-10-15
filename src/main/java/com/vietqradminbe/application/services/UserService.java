package com.vietqradminbe.application.services;

import com.vietqradminbe.application.mappers.UserMapper;
import com.vietqradminbe.application.services.interfaces.IUserService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.exceptions.NotFoundException;
import com.vietqradminbe.domain.models.Role;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.domain.models.UserRole;
import com.vietqradminbe.domain.repositories.RoleRepository;
import com.vietqradminbe.domain.repositories.UserRepository;
import com.vietqradminbe.domain.repositories.UserRoleRepository;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.request.ResetPasswordRequest;
import com.vietqradminbe.web.dto.request.UserCreationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepo;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepo;
    UserRoleRepository userRoleRepo;


    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }

    @Override
    public void resetPasswordForUser(ResetPasswordRequest request) {
        Optional<User> user = userRepo.findById(request.getId().trim());
        if (!user.isPresent()) {
            throw new NotFoundException(ErrorCode.USER_NOTFOUND);
        }
        if (user.get().getPasswordHash().trim().equals(passwordEncoder.encode(request.getOldPassword()))) {
            throw new BadRequestException(ErrorCode.OLD_PASSWORD_NOT_ALLOWED);
        }
        user.get().setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user.get());
    }

    @Override
    @Transactional
    public User createUserRequest(UserCreationRequest request) {
        request.setUsername(request.getUsername().trim());
        request.setPassword(request.getPassword().trim());
        if (userRepo.getUserByUsername(request.getUsername()) != null) {
            throw new BadRequestException(ErrorCode.ACCOUNT_EXISTED);
        }
        //create user first
        User user = userMapper.toUser(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword().trim()));
        user.setCreateAt(TimeHelperUtil.getCurrentTime());
        user.setUpdateAt(" ");
        user.setId(UUID.randomUUID().toString());
        user.setEmail(" ");
        user.setIsActive(false);
        User userAdded = userRepo.save(user);

        //find role
        Role role = roleRepo.findById(request.getRoleId()).orElse(null);
        if (role == null) {
            throw new NotFoundException(ErrorCode.ROLE_NOTFOUND);
        }

        //create user_role final

        UserRole userRole = new UserRole();
        userRole.setId(UUID.randomUUID().toString());
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepo.save(userRole);

        return userAdded;
    }

    @Override
    public User getUserByRefreshToken(String refreshToken) {
        return userRepo.getUserByRefreshToken(refreshToken).get();
    }
}
