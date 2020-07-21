package com.mrwhite.polls.Controller;

import com.mrwhite.polls.Entity.Controller.Request.RoleRequestBulk;
import com.mrwhite.polls.Entity.Controller.Response.ApiResponse;
import com.mrwhite.polls.Entity.Controller.Response.RoleResponseBulk;
import com.mrwhite.polls.Exception.PollException;
import com.mrwhite.polls.Service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Slf4j
@RequestMapping(value = "/api/role")
@Validated
public class RoleController {
    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @GetMapping
    public RoleResponseBulk getAllRoles() throws PollException{
        return roleService.getAllRoles();
    }

    @GetMapping(value = "/{name}")
    public RoleResponseBulk.RoleResponse getRoleByName(@NotNull @PathVariable("name") String name) throws PollException{
        return roleService.getRoleByName(name);
    }

    @GetMapping(value = "/user/{userId}")
    public RoleResponseBulk getUserRoles(@NotNull @PathVariable("userId") Long userId) throws PollException{
        return roleService.getUserRoles(userId);
    }

    @PostMapping
    public RoleResponseBulk.RoleResponse createRole(@Valid @RequestBody RoleRequestBulk.RoleRequest roleRequest) throws PollException{
        return roleService.createRole(roleRequest);
    }

    @PostMapping(value = "/{name}/user/{userId}")
    public ApiResponse addRoleToUser(@NotNull @PathVariable("userId") Long userId, @NotNull @PathVariable("name") String roleName) throws PollException{
        return roleService.addRoleToUser(userId,roleName);
    }

    @PutMapping(value = "/{id}")
    public RoleResponseBulk.RoleResponse updateRoleById(@NotNull @PathVariable("id") Long id,
                                                        @Valid @RequestBody RoleRequestBulk.RoleRequest roleRequest) throws PollException{
        return roleService.updateRoleById(id,roleRequest);
    }
}
