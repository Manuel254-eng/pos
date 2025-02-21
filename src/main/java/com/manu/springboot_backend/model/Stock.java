package com.manu.springboot_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "entry_date", nullable = false, updatable = false)
    private LocalDateTime entryDate = LocalDateTime.now();

    public Stock(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
        this.entryDate = LocalDateTime.now();
    }
}
