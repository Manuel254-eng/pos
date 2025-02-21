package com.manu.springboot_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private Long branchId;
    private BigDecimal price;
    private Long categoryId;
}
