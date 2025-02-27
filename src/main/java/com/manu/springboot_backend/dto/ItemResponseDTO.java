package com.manu.springboot_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.manu.springboot_backend.model.Branch;
import com.manu.springboot_backend.model.ProductCategory;
import lombok.Data;

@Data
public class ItemResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer count;
    private UserResponseDTO postedBy;
    private String postedFlag;
    private LocalDateTime postedTime;
    private UserResponseDTO modifiedBy;
    private LocalDateTime modifiedTime;
    private String deletedFlag;
    private UserResponseDTO deletedBy;
    private LocalDateTime deletedTime;
    private Branch branch;
    private ProductCategory productCategory;
    private BigDecimal price;
    private SupplierDTO supplier;
    private BigDecimal regularBuyingPrice;
    private BigDecimal sellingPrice;
    private Integer maxPercentageDiscount;
}
