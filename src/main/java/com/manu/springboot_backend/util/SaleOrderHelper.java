package com.manu.springboot_backend.util;

import com.manu.springboot_backend.model.SaleOrder;
import com.manu.springboot_backend.repository.SaleOrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SaleOrderHelper {
    private final SaleOrderRepository saleOrderRepository;

    public SaleOrderHelper(SaleOrderRepository saleOrderRepository) {
        this.saleOrderRepository = saleOrderRepository;
    }

    public String generateNextReferenceNumber() {
        Optional<SaleOrder> lastSaleOrder = saleOrderRepository.findTopByOrderByIdDesc();

        if (lastSaleOrder.isPresent()) {
            Long lastId = lastSaleOrder.get().getId(); // Get the last order ID
            return String.format("RF%06d", lastId + 1); // Format to RF000001, RF000002, etc.
        }

        return "RF000001"; // Default for first order
    }

}
