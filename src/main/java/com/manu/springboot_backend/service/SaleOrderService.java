package com.manu.springboot_backend.service;

import com.manu.springboot_backend.dto.SaleOrderDTO;
import com.manu.springboot_backend.dto.SaleOrderLineDTO;
import com.manu.springboot_backend.model.*;
import com.manu.springboot_backend.repository.*;

import com.manu.springboot_backend.util.SaleOrderHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleOrderService {
    private final SaleOrderRepository saleOrderRepository;
    private final SaleOrderLineRepository saleOrderLineRepository;
    private final BranchRepository branchRepository;
    private final ItemsRepository itemRepository;
    private final SaleOrderHelper saleOrderHelper;

    public SaleOrderService(SaleOrderRepository saleOrderRepository,
                            SaleOrderLineRepository saleOrderLineRepository,
                            BranchRepository branchRepository,
                            ItemsRepository itemRepository,
                            SaleOrderHelper saleOrderHelper) {
        this.saleOrderRepository = saleOrderRepository;
        this.saleOrderLineRepository = saleOrderLineRepository;
        this.branchRepository = branchRepository;
        this.itemRepository = itemRepository;
        this.saleOrderHelper = saleOrderHelper;
    }


    @Transactional
    public SaleOrder createSaleOrder(SaleOrderDTO saleOrderDTO) {
        Branch branch = branchRepository.findById(saleOrderDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setCustomerName(saleOrderDTO.getCustomerName());
        saleOrder.setReferenceNumber(saleOrderHelper.generateNextReferenceNumber()); // ✅ Call helper function
        saleOrder.setBranch(branch);

        List<SaleOrderLine> saleOrderLines = saleOrderDTO.getSaleOrderLines().stream().map(lineDTO -> {
            Item item = itemRepository.findById(lineDTO.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found: " + lineDTO.getItemId()));

            if ("Y".equals(item.getDeletedFlag())) {
                throw new RuntimeException("Item has been deleted: " + item.getName());
            }

            if (item.getCount() < lineDTO.getQuantity()) {
                throw new RuntimeException("Insufficient stock for item: " + item.getName());
            }

            item.setCount(item.getCount() - lineDTO.getQuantity());
            itemRepository.save(item);

            SaleOrderLine saleOrderLine = new SaleOrderLine();
            saleOrderLine.setSaleOrder(saleOrder);
            saleOrderLine.setItem(item);
            saleOrderLine.setQuantity(lineDTO.getQuantity());
            saleOrderLine.setPrice(lineDTO.getPrice());
            saleOrderLine.setExpectedSubtotal(lineDTO.getPrice().multiply(BigDecimal.valueOf(lineDTO.getQuantity())));
            saleOrderLine.setSubTotal(lineDTO.getSubTotal());

            return saleOrderLine;
        }).collect(Collectors.toList());

        BigDecimal totalAmount = saleOrderLines.stream()
                .map(SaleOrderLine::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        saleOrder.setSaleOrderLines(saleOrderLines);
        saleOrder.setTotalAmount(totalAmount);

        return saleOrderRepository.save(saleOrder);
    }
    public List<SaleOrder> getAllSaleOrders() {
        return saleOrderRepository.findAll();
    }

    public Optional<SaleOrder> getSaleOrderById(Long id) {
        return saleOrderRepository.findById(id);
    }

    public long countSaleOrders() {
        return saleOrderRepository.count();
    }





}
