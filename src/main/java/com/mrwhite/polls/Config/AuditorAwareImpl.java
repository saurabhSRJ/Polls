package com.mrwhite.polls.Config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

//To tell JPA about currently logged in user we will need to provide an implementation of AuditorAware and override getCurrentAuditor() method.
// And inside getCurrentAuditor() we will need to fetch currently logged in user.
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String user = "Admin";
        return Optional.ofNullable(user);
    }
}
