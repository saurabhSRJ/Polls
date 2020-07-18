package com.mrwhite.polls.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mrwhite.polls.Entity.db.Role;
import com.mrwhite.polls.Entity.db.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String name;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetails create(User user){
        List<String> roleList = new ArrayList<String>();
        roleList.add("ROLE_DEFAULT");
        List<GrantedAuthority> authorities = roleList.stream().map(s->new SimpleGrantedAuthority(s)).collect(Collectors.toList());
        return new CustomUserDetails(user.getId(),user.getName(),user.getEmail(),user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
