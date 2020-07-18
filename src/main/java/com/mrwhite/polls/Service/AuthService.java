package com.mrwhite.polls.Service;

import com.mrwhite.polls.Entity.Controller.Request.LoginRequest;
import com.mrwhite.polls.Entity.Controller.Request.SignUpRequest;
import com.mrwhite.polls.Entity.Controller.Response.ApiResponse;
import com.mrwhite.polls.Entity.Controller.Response.UserResponse;
import com.mrwhite.polls.Entity.db.User;
import com.mrwhite.polls.Exception.PollErrorCode;
import com.mrwhite.polls.Exception.PollException;

import com.mrwhite.polls.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse authenticateUser(LoginRequest loginRequest) throws PollException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),loginRequest.getPassword()));

//            UsernamePasswordAuthenticationToken is an implementation of Authentication interface
            SecurityContextHolder.getContext().setAuthentication(authentication);
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setSuccess(Boolean.TRUE);
            apiResponse.setMessage("Successfully Authenticated");
            return apiResponse;
        }
        catch (AuthenticationException authentication){
            log.error("Wrong user credentials provided");
            throw new PollException(PollErrorCode.UNAUTHORISED,"Wrong user credentials provided");
        }
    }

    public UserResponse registerUser(SignUpRequest signUpRequest) throws PollException{
        if(userRepository.findByEmailOrUsername(signUpRequest.getEmail(),signUpRequest.getUsername()) != null){
            log.error("user with given email-id already exist");
            throw new PollException(PollErrorCode.BAD_REQUEST, "user with given email-id already exist");
        }

        User user = User.builder().name(signUpRequest.getName()).username(signUpRequest.getUsername()).email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword()).build();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        UserResponse userResponse = UserResponse.builder().id(user.getId()).email(user.getEmail()).username(user.getUsername()).name(user.getName()).build();
        userResponse.setSuccess(Boolean.TRUE);
        userResponse.setMessage("User Successfully Created");
        return userResponse;
    }
}
