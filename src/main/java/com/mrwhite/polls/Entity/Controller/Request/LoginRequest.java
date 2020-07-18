package com.mrwhite.polls.Entity.Controller.Request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class LoginRequest {
    @NotNull
    private String usernameOrEmail;

    @NotNull
    private String password;
}
