package com.manu.springboot_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sale_order_lines")


public class SaleOrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sale_order_id", nullable = false)
    private SaleOrder saleOrder;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price; // Price per unit

    @Column(nullable = false)
    private BigDecimal subTotal; // quantity * price


    @Column(nullable = false)
    private BigDecimal expectedSubtotal; // quantity * price undiscounted

    @PrePersist
    public void calculateTotal() {
        this.expectedSubtotal = this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}
