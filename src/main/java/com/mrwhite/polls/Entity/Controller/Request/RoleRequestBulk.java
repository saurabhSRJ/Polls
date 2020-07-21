package com.mrwhite.polls.Entity.Controller.Request;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class RoleRequestBulk{
    @Valid
    @NotEmpty
    private List<RoleRequest> roleRequestList;

    @Data
    @Builder
    public static class RoleRequest{
        private Long id;
        @NotNull
        private String roleName;
    }
}
