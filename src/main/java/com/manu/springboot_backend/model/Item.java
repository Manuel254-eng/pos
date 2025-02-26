package com.manu.springboot_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private Integer count;



    @Column(nullable = false)
    private BigDecimal regularBuyingPrice;


    @Column(nullable = false)
    private BigDecimal sellingPrice;


    @Column(nullable = false)
    private Integer maxPercentageDiscount;



    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "posted_by", nullable = false, updatable = false)
    private User postedBy;

    @Column(nullable = false)
    private String postedFlag = "N";

    @Column(nullable = true, updatable = false)
    private LocalDateTime postedTime;

    @ManyToOne
    @JoinColumn(name = "deleted_by", updatable = true)
    private User deletedBy;

    @Column(nullable = false)
    private String deletedFlag = "N";

    @Column(nullable = true, updatable = true)
    private LocalDateTime deletedTime;

    @ManyToOne
    @JoinColumn(name = "modified_by", updatable = true)
    private User modifiedBy;

    @Column(nullable = false)
    private String modifiedFlag = "N";

    @Column(nullable = true, updatable = true)
    private LocalDateTime modifiedTime;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private ProductCategory category;

    // ✅ Added Supplier field (nullable)
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = true)
    private Supplier supplier;

    public Item(String name, String description, Integer count, BigDecimal regularBuyingPrice, BigDecimal sellingPrice, User postedBy, Supplier supplier) {
        this.name = name;
        this.description = description;
        this.count = count;
        this.regularBuyingPrice = regularBuyingPrice;
        this.sellingPrice = sellingPrice;
        this.postedBy = postedBy;
        this.supplier = supplier;
        this.postedTime = LocalDateTime.now();
        this.postedFlag = "Y";
        this.modifiedFlag = "N";
    }
}
