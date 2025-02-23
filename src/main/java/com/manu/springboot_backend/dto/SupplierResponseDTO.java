package com.manu.springboot_backend.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SupplierResponseDTO {
    private Long id;
    private String supplierCode;
    private String supplierName;
    private String contactPerson;
    private String phoneNumber;
    private String emailAddress;
    private String physicalAddress;
    private String postalAddress;
    private UserResponseDTO postedBy;
    private String postedFlag;
    private LocalDateTime postedTime;
    private UserResponseDTO modifiedBy;
    private LocalDateTime modifiedTime;
    private String deletedFlag;
    private UserResponseDTO deletedBy;
    private LocalDateTime deletedTime;
}
