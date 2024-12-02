package com.example.SpringSecurityDemo.Entity.User;


import com.example.SpringSecurityDemo.Entity.Role.RoleResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponseDto {
    private String userID;
    private String username;
    private String email;
    private String phone;
    private String address;
    private Set<RoleResponseDto> roles;
}
