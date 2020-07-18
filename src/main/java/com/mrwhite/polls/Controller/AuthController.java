package com.mrwhite.polls.Controller;

import com.mrwhite.polls.Entity.Controller.Request.LoginRequest;
import com.mrwhite.polls.Entity.Controller.Request.SignUpRequest;
import com.mrwhite.polls.Entity.Controller.Response.ApiResponse;
import com.mrwhite.polls.Entity.Controller.Response.UserResponse;
import com.mrwhite.polls.Exception.PollException;
import com.mrwhite.polls.Service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(value = "/api/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping(value = "/signin")
    public ApiResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws PollException{
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping(value = "signup")
    public UserResponse registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws PollException{
        return authService.registerUser(signUpRequest);
    }
}
