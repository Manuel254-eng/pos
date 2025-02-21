package com.manu.springboot_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaleOrderDTO {
    private String customerName;
    private Long branchId;
    private List<SaleOrderLineDTO> saleOrderLines;
}
