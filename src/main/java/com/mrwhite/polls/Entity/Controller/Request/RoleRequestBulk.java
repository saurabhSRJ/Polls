package com.mrwhite.polls.Entity.Controller.Request;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
public class RoleRequestBulk{
    @Valid
    @NotEmpty
    private List<RoleRequest> roleRequests;

    @Data
    @Builder
    public static class RoleRequest{
        private Long id;
        private String name;
    }
}
