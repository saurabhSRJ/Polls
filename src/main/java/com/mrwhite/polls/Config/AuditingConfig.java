package com.mrwhite.polls.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration //Indicates that a class declares one or more @Bean methods and may be processed by the Spring container
// to generate bean definitions and service requests for those beans at runtime
@EnableJpaAuditing(auditorAwareRef = "auditProvider") //auditorAwareRef where we need to pass the name of the AuditorAware bean
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditProvider(){
        return new AuditorAwareImpl();
    }
}
