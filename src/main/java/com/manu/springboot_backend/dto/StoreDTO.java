package com.manu.springboot_backend.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDTO {
    private String name;
    private String description;
    private String location;
    private String contactNumber;
    private String email;
}
