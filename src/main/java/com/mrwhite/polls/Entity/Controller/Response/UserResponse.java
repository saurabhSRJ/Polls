package com.mrwhite.polls.Entity.Controller.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse extends ApiResponse {
    private Long id;
    private String name;
    private String email;
    private String username;
}
