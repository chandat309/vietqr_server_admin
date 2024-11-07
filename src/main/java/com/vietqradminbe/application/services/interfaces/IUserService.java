package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.web.dto.request.ResetPasswordRequest;
import com.vietqradminbe.web.dto.request.UserCreationRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface IUserService {
    public List<User> getAllUsers();
    public User getUserByUsername(String username);
    public User createUserRequest(UserCreationRequest request);
    public User getUserByRefreshToken(String refreshToken);
    public void resetPasswordForUser(ResetPasswordRequest request);
}
