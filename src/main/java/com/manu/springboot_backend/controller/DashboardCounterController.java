package com.manu.springboot_backend.controller;

import com.manu.springboot_backend.service.SaleOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/")
public class DashboardCounterController {

    private final SaleOrderService saleOrderService;

    public DashboardCounterController(SaleOrderService saleOrderService) {
        this.saleOrderService = saleOrderService;
    }

    /**
     * Retrieves the total number of sale orders.
     *
     * @return A JSON response containing the total count of sale orders.
     */
    @Operation(summary = "Get total sale orders count",
            description = "Returns the total number of sale orders in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total sale orders retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/sales/count")
    public ResponseEntity<Map<String, Object>> countSaleOrders() {
        long count = saleOrderService.countSaleOrders();
        return ResponseEntity.ok(Map.of(
                "message", "Total sale orders retrieved successfully",
                "status", HttpStatus.OK.value(),
                "count", count
        ));
    }

}
