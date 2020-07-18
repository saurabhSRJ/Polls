package com.mrwhite.polls.Entity.Controller.Response;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class ApiResponse {
    protected String message;
    protected Boolean success;
}
