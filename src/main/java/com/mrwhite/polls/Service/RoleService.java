package com.mrwhite.polls.Service;

import com.mrwhite.polls.Entity.Controller.Request.RoleRequestBulk;
import com.mrwhite.polls.Entity.Controller.Response.ApiResponse;
import com.mrwhite.polls.Entity.Controller.Response.RoleResponseBulk;
import com.mrwhite.polls.Entity.db.Role;
import com.mrwhite.polls.Entity.db.UserRoleMapping;
import com.mrwhite.polls.Exception.PollErrorCode;
import com.mrwhite.polls.Exception.PollException;
import com.mrwhite.polls.Repository.RoleRepository;
import com.mrwhite.polls.Repository.UserRepository;
import com.mrwhite.polls.Repository.UserRoleMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleService {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UserRoleMappingRepository userRoleMappingRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository,UserRepository userRepository,UserRoleMappingRepository userRoleMappingRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
    }

    public RoleResponseBulk getAllRoles(){
         List<Role> roles = roleRepository.findAll();
         List<RoleResponseBulk.RoleResponse> roleResponseList = new ArrayList<>();
         if(!CollectionUtils.isEmpty(roles)){
             for(Role role:roles){
                 RoleResponseBulk.RoleResponse roleResponse = RoleResponseBulk.RoleResponse.builder().roleName(role.getRoleName()).id(role.getId()).build();
                 roleResponseList.add(roleResponse);
             }
             return RoleResponseBulk.builder().roleResponseList(roleResponseList).build();
         }
         else {
             return null;
         }
    }

    public RoleResponseBulk.RoleResponse getRoleByName(String roleName) throws PollException{
        Role role = roleRepository.findByRoleName(roleName);
        if(role == null){
            log.error("No role exist with roleName: " + roleName);
            throw new PollException(PollErrorCode.NOT_FOUND,"No role exists with this name");
        }
        return RoleResponseBulk.RoleResponse.builder().id(role.getId()).roleName(role.getRoleName()).build();
    }

    public RoleResponseBulk getUserRoles(Long userId) throws PollException{
        if(userRepository.existsById(userId) == false){
            log.error("user does not exist with id: "+userId);
            throw new PollException(PollErrorCode.NOT_FOUND,"user does not exist with given id");
        }
        List<UserRoleMapping> userRoleMappingList = userRoleMappingRepository.findByUserId(userId);
        List<Long> roleIds = userRoleMappingList.stream().map(s->s.getRoleId()).collect(Collectors.toList());
        List<Role> roles = roleRepository.findByIdIn(roleIds);

        List<RoleResponseBulk.RoleResponse> roleResponseList = new ArrayList<>();
        for(Role role:roles){
            RoleResponseBulk.RoleResponse roleResponse = RoleResponseBulk.RoleResponse.builder().id(role.getId()).roleName(role.getRoleName()).build();
            roleResponseList.add(roleResponse);
        }
        return RoleResponseBulk.builder().roleResponseList(roleResponseList).build();
    }

    public RoleResponseBulk.RoleResponse createRole(RoleRequestBulk.RoleRequest roleRequest) throws PollException{
        if(roleRepository.existsByRoleName(roleRequest.getRoleName())){
            log.error("Role already exist with name: " + roleRequest.getRoleName());
            throw new PollException(PollErrorCode.BAD_REQUEST,"Role exist with given name");
        }
        Role role = Role.builder().roleName(roleRequest.getRoleName()).build();
        roleRepository.save(role);
        return RoleResponseBulk.RoleResponse.builder().roleName(role.getRoleName()).build();
    }

    public ApiResponse addRoleToUser(Long userId, String roleName) throws PollException{
        if(userRepository.existsById(userId) == false){
            log.error("user does not exist with id: "+userId);
            throw new PollException(PollErrorCode.NOT_FOUND,"user does not exist with given id");
        }

        Role role = roleRepository.findByRoleName(roleName);
        if(roleRepository.findByRoleName(roleName) == null){
            log.error("No role exist with name: " + roleName);
            throw new PollException(PollErrorCode.NOT_FOUND,"Role does not exist with given name");
        }

        if(userRoleMappingRepository.findByUserIdAndRoleId(userId,role.getId()) !=null){
            log.error("Role already assigned to the user");
            throw new PollException(PollErrorCode.BAD_REQUEST,"Role already assigned to the user");
        }
        UserRoleMapping userRoleMapping = UserRoleMapping.builder().roleId(role.getId()).userId(userId).build();
        userRoleMappingRepository.save(userRoleMapping);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(Boolean.TRUE);
        apiResponse.setMessage("Role Successfully assigned to the user");
        return apiResponse;
    }

    public RoleResponseBulk.RoleResponse updateRoleById(Long id, RoleRequestBulk.RoleRequest roleRequest) throws PollException{
        Role role = roleRepository.findById(id).orElse(null);
        if(role == null){
            log.error("No role exist with id: " + id);
            throw new PollException(PollErrorCode.BAD_REQUEST,"No role exist with given id");
        }
        role.setRoleName(roleRequest.getRoleName());
        return RoleResponseBulk.RoleResponse.builder().id(role.getId()).roleName(role.getRoleName()).build();
    }

}
