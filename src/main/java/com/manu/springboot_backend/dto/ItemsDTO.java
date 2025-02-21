package com.manu.springboot_backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemsDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer count;
    private Long branchId;
    private Long categoryId;
}
