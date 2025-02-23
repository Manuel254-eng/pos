package com.manu.springboot_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierDTO {
    private String supplierCode;
    private String supplierName;
    private String contactPerson;
    private String phoneNumber;
    private String emailAddress;
    private String physicalAddress;
    private String postalAddress;
}
