package com.mrwhite.polls.Service;

import com.mrwhite.polls.Entity.Controller.Request.LoginRequest;
import com.mrwhite.polls.Entity.Controller.Request.SignUpRequest;
import com.mrwhite.polls.Entity.Controller.Response.JwtAuthenticationResponse;
import com.mrwhite.polls.Entity.Controller.Response.UserResponse;
import com.mrwhite.polls.Entity.db.Role;
import com.mrwhite.polls.Entity.db.User;
import com.mrwhite.polls.Entity.db.UserRoleMapping;
import com.mrwhite.polls.Exception.PollErrorCode;
import com.mrwhite.polls.Exception.PollException;

import com.mrwhite.polls.Repository.RoleRepository;
import com.mrwhite.polls.Repository.UserRepository;
import com.mrwhite.polls.Repository.UserRoleMappingRepository;
import com.mrwhite.polls.Security.JwtTokenProvider;
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
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private UserRoleMappingRepository userRoleMappingRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserRoleMappingRepository userRoleMappingRepository){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRoleMappingRepository = userRoleMappingRepository;
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) throws PollException {
        try {
            log.info("Entering login api");
            //Uses CustomUserDetailsService to authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),loginRequest.getPassword()));
            //UsernamePasswordAuthenticationToken is an implementation of Authentication interface

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtTokenProvider.generateToken(authentication);
            return JwtAuthenticationResponse.builder().token(jwtToken).build();
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
        Role role = roleRepository.findByRoleName("ROLE_USER");
        if(role == null){
            log.error("Consider defining a default user role");
            throw new PollException(PollErrorCode.NOT_FOUND,"Default user Role not set");
        }

        User user = User.builder().name(signUpRequest.getName()).username(signUpRequest.getUsername()).email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword()).build();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = userRepository.save(user);
        UserRoleMapping userRoleMapping = UserRoleMapping.builder().userId(user.getId()).roleId(role.getId()).build();
        userRoleMappingRepository.save(userRoleMapping);

        UserResponse userResponse = UserResponse.builder().id(user.getId()).email(user.getEmail()).username(user.getUsername()).name(user.getName()).build();
        userResponse.setSuccess(Boolean.TRUE);
        userResponse.setMessage("User Successfully Created");
        return userResponse;
    }


}
