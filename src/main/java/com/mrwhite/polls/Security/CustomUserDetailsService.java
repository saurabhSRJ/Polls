package com.mrwhite.polls.Security;

import com.mrwhite.polls.Entity.db.Role;
import com.mrwhite.polls.Entity.db.User;
import com.mrwhite.polls.Entity.db.UserRoleMapping;
import com.mrwhite.polls.Exception.PollErrorCode;
import com.mrwhite.polls.Exception.PollException;
import com.mrwhite.polls.Repository.RoleRepository;
import com.mrwhite.polls.Repository.UserRepository;
import com.mrwhite.polls.Repository.UserRoleMappingRepository;
import com.mrwhite.polls.Service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
//This class is used by spring security to load user from the database, wrapped as a UserDetail Object
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleMappingRepository userRoleMappingRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository, UserRoleMappingRepository userRoleMappingRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
    }

    @Override
    @Transactional
    //This method is used by spring security
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(usernameOrEmail,usernameOrEmail);
        if(user == null){
            log.error("Username or Email not found");
            throw new UsernameNotFoundException("Username or Email not found"   );
        }
        List<UserRoleMapping> userRoleMappingList = userRoleMappingRepository.findByUserId(user.getId());
        List<Long> roleIds = userRoleMappingList.stream().map(s->s.getRoleId()).collect(Collectors.toList());
        List<Role> roleList = roleRepository.findByIdIn(roleIds);
        return CustomUserDetails.create(user, roleList);
    }

    @Transactional
    //This method is used by JwtAuthentication Filter
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            log.error("Username or Email not found");
            throw new UsernameNotFoundException("Username or Email not found"   );
        }
        List<UserRoleMapping> userRoleMappingList = userRoleMappingRepository.findByUserId(user.getId());
        List<Long> roleIds = userRoleMappingList.stream().map(s->s.getRoleId()).collect(Collectors.toList());
        List<Role> roleList = roleRepository.findByIdIn(roleIds);
        return CustomUserDetails.create(user, roleList);
    }
}
