package com.manu.springboot_backend.controller;

import com.manu.springboot_backend.model.Item;
import com.manu.springboot_backend.model.Stock;
import com.manu.springboot_backend.repository.ItemsRepository;
import com.manu.springboot_backend.repository.StockRepository;
import com.manu.springboot_backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/api/v1/stock")
public class StockController {

    private final ItemsRepository itemsRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    public StockController(ItemsRepository itemsRepository, StockRepository stockRepository, UserRepository userRepository) {
        this.itemsRepository = itemsRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
    }

    // ✅ Add stock to an item
    @PostMapping("/{id}/add")
    public ResponseEntity<Map<String, Object>> addStock(@PathVariable Long id, @RequestParam int quantity) {
        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied", "status", HttpStatus.FORBIDDEN.value()));

        Optional<Item> itemOpt = itemsRepository.findById(id);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();

            // 1️⃣ Update item count in `items` table
            item.setCount(item.getCount() + quantity);
            itemsRepository.save(item);

            // 2️⃣ Create new stock entry in `stock` table
            Stock stock = new Stock(item, quantity);
            stockRepository.save(stock);

            return ResponseEntity.ok(Map.of(
                    "message", "Stock added successfully",
                    "newQuantity", item.getCount(),
                    "status", HttpStatus.OK.value()
            ));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Item not found", "status", HttpStatus.NOT_FOUND.value()));
    }

    // 🔹 Helper method to check if logged-in user is ADMIN
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        return userRepository.findByEmail(loggedInEmail)
                .map(user -> user.getRole().name().equals("ADMIN"))
                .orElse(false);
    }
}
