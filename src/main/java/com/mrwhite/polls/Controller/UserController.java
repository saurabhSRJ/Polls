package com.mrwhite.polls.Controller;

import com.mrwhite.polls.Entity.Controller.Request.UserRequest;
import com.mrwhite.polls.Entity.Controller.Response.ApiResponse;
import com.mrwhite.polls.Entity.Controller.Response.UserResponse;
import com.mrwhite.polls.Exception.PollException;
import com.mrwhite.polls.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
@Slf4j
@Validated
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping
    public List<UserResponse> getAllUsers() throws PollException{
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public UserResponse getUserById(@NotNull @PathVariable("id") Long id) throws PollException{
        return userService.getUserById(id);
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserRequest userRequest) throws PollException{
        return userService.createUser(userRequest);
    }

    @PutMapping(value = "/{id}")
    public UserResponse updateUser(@NotNull @PathVariable("id") Long id,@Valid @RequestBody UserRequest userRequest) throws PollException{
        return userService.updateUser(id,userRequest);
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse deleteUser(@NotNull @PathVariable("id") Long id) throws PollException{
        return userService.deleteUser(id);
    }
}
