package com.manu.springboot_backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemsDTO {
    private String name;
    private String description;
    private Integer count;
    private Long branchId;
    private Long categoryId;
    private Long supplierId;
    private BigDecimal regularBuyingPrice;
    private BigDecimal sellingPrice;
    private Integer maxPercentageDiscount;

}
