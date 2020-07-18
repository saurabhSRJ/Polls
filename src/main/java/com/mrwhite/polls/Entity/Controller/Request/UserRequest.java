package com.mrwhite.polls.Entity.Controller.Request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserRequest {
    @NotNull
    @Size(max = 40)
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String username;
    @NotNull
    private String password;
}
