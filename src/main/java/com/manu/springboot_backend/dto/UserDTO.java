package com.manu.springboot_backend.dto;

import com.manu.springboot_backend.model.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String fullName;
    private String userName;
    private String email;
    private String phone;
    private String address;
    private String password;
    private Role role;
}
