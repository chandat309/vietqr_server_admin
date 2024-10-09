package com.vietqradminbe.web.controllers;


import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.domain.models.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping ("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;


    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
