package com.mrwhite.polls.Service;

import com.mrwhite.polls.Entity.Controller.Request.UserRequest;
import com.mrwhite.polls.Entity.Controller.Response.UserResponse;
import com.mrwhite.polls.Entity.db.User;
import com.mrwhite.polls.Exception.PollErrorCode;
import com.mrwhite.polls.Exception.PollException;
import com.mrwhite.polls.Repository.RoleRepository;
import com.mrwhite.polls.Repository.UserRepository;
import com.mrwhite.polls.Repository.UserRoleMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleMappingRepository userRoleMappingRepository;

    @Autowired
    public UserService(UserRepository userRepository,RoleRepository roleRepository,UserRoleMappingRepository userRoleMappingRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
    }

    private UserResponse convertUserToUserResponse(User user){
        UserResponse userResponse = UserResponse.builder().id(user.getId()).email(user.getEmail()).username(user.getUsername()).name(user.getName()).build();
        return userResponse;
    }
    public List<UserResponse> getAllUsers(){
        List<User> userList = userRepository.findAll();
        if(!userList.isEmpty()) {
            List<UserResponse> userResponseList = new ArrayList<>();
            for(User user: userList){
                UserResponse userResponse = convertUserToUserResponse(user);
                userResponseList.add(userResponse);
            }
            return userResponseList;
        }
        else{
            return null;
        }
    }

    public UserResponse getUserById(Long id) throws PollException {
        User user = userRepository.findById(id).orElse(null);
        if(user==null){
            log.error("user does not exist with id: " + id);
            throw new PollException(PollErrorCode.NOT_FOUND,"user does not exist with given id");
        }
        UserResponse userResponse = convertUserToUserResponse(user);
        return userResponse;
    }

    public UserResponse createUser(UserRequest userRequest) throws PollException{
        if(userRepository.existsByEmail(userRequest.getEmail()) == true){
            log.error("user with given email-id already exist");
            throw new PollException(PollErrorCode.BAD_REQUEST, "user with given email-id already exist");
        }
        User user = User.builder().name(userRequest.getName()).email(userRequest.getEmail()).username(userRequest.getUsername()).
                password(userRequest.getPassword()).build();
        userRepository.save(user);
        UserResponse userResponse = convertUserToUserResponse(user);
        userResponse.setSuccess(Boolean.TRUE);
        userResponse.setMessage("User Successfully Created");
        return userResponse;
    }

    public UserResponse updateUser(Long id,UserRequest userRequest) throws PollException{
        User user = userRepository.findById(id).orElse(null);
        if(user==null){
            log.error("user does not exist with id: " + id);
            throw new PollException(PollErrorCode.NOT_FOUND,"user does not exist with given id");
        }

        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        userRepository.save(user);
        UserResponse userResponse = convertUserToUserResponse(user);
        userResponse.setSuccess(Boolean.TRUE);
        userResponse.setMessage("User Successfully Updated");
        return userResponse;
    }

    @Transactional
    public UserResponse deleteUser(Long id) throws PollException{
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            log.error("user does not exist with id: " + id);
            throw new PollException(PollErrorCode.NOT_FOUND,"user does not exist with given id");
        }
        user.setDeleted(id);
        userRoleMappingRepository.deleteByUserId(id);
        UserResponse userResponse = new UserResponse();
        userResponse.setSuccess(Boolean.TRUE);
        userResponse.setMessage("User successfully deleted");
        return userResponse;
    }

}
