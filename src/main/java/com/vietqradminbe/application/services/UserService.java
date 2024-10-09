package com.vietqradminbe.application.services;

import com.vietqradminbe.application.mappers.UserMapper;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.exceptions.NotFoundException;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.domain.repositories.UserRepository;
import com.vietqradminbe.web.dto.request.UserCreationRequest;


import com.vietqradminbe.web.dto.response.UserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public User createUserRequest(UserCreationRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public UserResponse getUserById(String id){
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOTFOUND)));
    }
}
