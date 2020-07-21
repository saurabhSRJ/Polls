package com.mrwhite.polls.Entity.Controller.Response;

import com.mrwhite.polls.Entity.Controller.Request.RoleRequestBulk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseBulk extends ApiResponse {
    @Valid
    @NotEmpty
    private List<RoleResponse> roleResponseList;

    @Data
    @Builder
    public static class RoleResponse{
        private Long id;
        private String roleName;
    }
}
