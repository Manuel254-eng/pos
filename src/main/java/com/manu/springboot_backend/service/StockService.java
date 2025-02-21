package com.manu.springboot_backend.service;

import com.manu.springboot_backend.model.Item;
import com.manu.springboot_backend.model.Stock;
import com.manu.springboot_backend.repository.ItemsRepository;
import com.manu.springboot_backend.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final ItemsRepository itemsRepository;

    public StockService(StockRepository stockRepository, ItemsRepository itemsRepository) {
        this.stockRepository = stockRepository;
        this.itemsRepository = itemsRepository;
    }

    @Transactional
    public Stock addStock(Long itemId, int quantity) {
        Optional<Item> itemOpt = itemsRepository.findById(itemId);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();

            // Create a new stock entry
            Stock stock = new Stock(item, quantity);
            stockRepository.save(stock);

            // Update item quantity
            item.setCount(item.getCount() + quantity);
            itemsRepository.save(item);

            return stock;
        }
        throw new RuntimeException("Item not found");
    }
}
