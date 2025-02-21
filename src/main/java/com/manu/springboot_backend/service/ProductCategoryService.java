package com.manu.springboot_backend.service;

import com.manu.springboot_backend.dto.ProductCategoryDTO;
import com.manu.springboot_backend.model.ProductCategory;
import com.manu.springboot_backend.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAll();
    }

    public Optional<ProductCategory> getCategoryById(Long id) {
        return productCategoryRepository.findById(id);
    }

    public ProductCategory createCategory(ProductCategoryDTO categoryDTO) {
        ProductCategory category = new ProductCategory();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return productCategoryRepository.save(category);
    }

    public Optional<ProductCategory> updateCategory(Long id, ProductCategoryDTO categoryDTO) {
        return productCategoryRepository.findById(id).map(category -> {
            if (categoryDTO.getName() != null) category.setName(categoryDTO.getName());
            if (categoryDTO.getDescription() != null) category.setDescription(categoryDTO.getDescription());
            return productCategoryRepository.save(category);
        });
    }

    public boolean deleteCategory(Long id) {
        if (productCategoryRepository.existsById(id)) {
            productCategoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
