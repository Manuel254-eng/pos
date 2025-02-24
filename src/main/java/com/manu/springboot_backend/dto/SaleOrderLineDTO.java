package com.manu.springboot_backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleOrderLineDTO {
    private Long itemId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
