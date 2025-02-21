package com.manu.springboot_backend.controller;

import com.manu.springboot_backend.dto.ProductCategoryDTO;
import com.manu.springboot_backend.model.ProductCategory;
import com.manu.springboot_backend.service.ProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;



@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        return ResponseEntity.ok(Map.of(
                "message", "Categories retrieved successfully",
                "status", HttpStatus.OK.value(),
                "data", categories
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<ProductCategory> category = productCategoryService.getCategoryById(id);
        return category.map(value -> ResponseEntity.ok(Map.of(
                "message", "Category retrieved successfully",
                "status", HttpStatus.OK.value(),
                "data", value
        ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", "Category not found",
                "status", HttpStatus.NOT_FOUND.value()
        )));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody ProductCategoryDTO categoryDTO) {
        ProductCategory savedCategory = productCategoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Category created successfully",
                "status", HttpStatus.CREATED.value(),
                "data", savedCategory
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable Long id, @RequestBody ProductCategoryDTO categoryDTO) {
        Optional<ProductCategory> updatedCategory = productCategoryService.updateCategory(id, categoryDTO);
        if (updatedCategory.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "message", "Category updated successfully",
                    "status", HttpStatus.OK.value(),
                    "data", updatedCategory.get()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Category not found",
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
        boolean deleted = productCategoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of(
                    "message", "Category deleted successfully",
                    "status", HttpStatus.OK.value()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Category not found",
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }
}
