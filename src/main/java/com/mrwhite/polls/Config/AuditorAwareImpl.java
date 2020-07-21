package com.mrwhite.polls.Config;

import com.mrwhite.polls.Security.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

//To tell JPA about currently logged in user we will need to provide an implementation of AuditorAware and override getCurrentAuditor() method.
// And inside getCurrentAuditor() we will need to fetch currently logged in user.
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken){
            return Optional.ofNullable("Anonymous");
        }

        CustomUserDetails customUserDetails= (CustomUserDetails) authentication.getPrincipal();
        return Optional.ofNullable(customUserDetails.getUsername());
    }
}
