package com.manu.springboot_backend.controller;

import com.manu.springboot_backend.dto.SaleOrderDTO;
import com.manu.springboot_backend.model.SaleOrder;
import com.manu.springboot_backend.service.SaleOrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;



@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/sale-orders")
public class SaleController {
    private final SaleOrderService saleOrderService;

    public SaleController(SaleOrderService saleOrderService) {
        this.saleOrderService = saleOrderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createSaleOrder(@RequestBody SaleOrderDTO saleOrderDTO) {
        SaleOrder saleOrder = saleOrderService.createSaleOrder(saleOrderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Sale order created successfully",
                "status", HttpStatus.CREATED.value(),
                "data", saleOrder
        ));
    }

    @GetMapping
    public ResponseEntity<List<SaleOrder>> getAllSaleOrders() {
        return ResponseEntity.ok(saleOrderService.getAllSaleOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleOrderById(@PathVariable Long id) {
        Optional<SaleOrder> saleOrder = saleOrderService.getSaleOrderById(id);

        if (saleOrder.isPresent()) {
            return ResponseEntity.ok(saleOrder.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Sale order not found",
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }


}
