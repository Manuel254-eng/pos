package com.manu.springboot_backend.dto;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String phone;
    private String address;
    private String fullName;
    private String publishedBy;
}
