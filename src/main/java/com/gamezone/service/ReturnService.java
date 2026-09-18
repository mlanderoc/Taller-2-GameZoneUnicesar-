package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.persistence.ReturnRecord;
import com.gamezone.persistence.ReturnRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles business rules for registering and querying product returns,
 * validating the 30-day return window and that returned products
 * actually belong to the referenced sale, and restoring stock through
 * ProductService or AccessoryService as appropriate.
 */
public class ReturnService {

    private ReturnRepository returnRepository;
    private SaleService saleService;
    private ProductService productService;
    private AccessoryService accessoryService;

    public ReturnService(ReturnRepository returnRepository, SaleService saleService,
                          ProductService productService, AccessoryService accessoryService) {
        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.productService = productService;
        this.accessoryService = accessoryService;
    }

    /**
     * Registers a new return: validates the original sale exists, that it is
     * still within the 30-day return window, and that every product being
     * returned actually belongs to that sale. Restores stock for each
     * returned product and persists the return.
     *
     * @param saleId     id of the original sale
     * @param productIds ids of the products being returned
     * @param reason     reason for the return
     * @return the registered Return
     * @throws IllegalArgumentException if any validation fails
     */
    public Return registerReturn(String saleId, List<String> productIds, String reason) {
        Sale originalSale = saleService.findSaleById(saleId);
        if (originalSale == null) {
            throw new IllegalArgumentException("La venta indicada no existe.");
        }

        if (!originalSale.canBeReturned()) {
            throw new IllegalArgumentException("El plazo de 30 días para devolver esta venta ya expiró.");
        }

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Debe indicar al menos un producto a devolver.");
        }

        List<Product> returnedProducts = new ArrayList<>();
        for (String productId : productIds) {
            Product match = findInSale(originalSale, productId);
            if (match == null) {
                throw new IllegalArgumentException(
                        "El producto " + productId + " no pertenece a la venta indicada.");
            }
            returnedProducts.add(match);
        }

        String returnId = UUID.randomUUID().toString();
        Return returnObj = new Return(returnId, LocalDate.now(), originalSale, returnedProducts, reason);
        returnObj.calculateRefundAmount();

        for (Product product : returnedProducts) {
            if (product instanceof Accessory) {
                accessoryService.restoreStock(product.getId(), 1);
            } else {
                productService.restoreStock(product.getId(), 1);
            }
        }

        ReturnRecord record = toRecord(returnObj);
        List<ReturnRecord> records = returnRepository.loadAll();
        records.add(record);
        returnRepository.saveAll(records);

        return returnObj;
    }

    /**
     * Returns all registered returns.
     */
    public List<Return> viewAllReturns() {
        List<Return> returns = new ArrayList<>();
        for (ReturnRecord record : returnRepository.loadAll()) {
            returns.add(toReturn(record));
        }
        return returns;
    }

    /**
     * Returns all returns whose original sale belongs to the given customer.
     */
    public List<Return> viewReturnsByCustomer(String customerId) {
        List<Return> result = new ArrayList<>();
        for (Return returnObj : viewAllReturns()) {
            if (returnObj.getOriginalSale().getCustomer().getId().equals(customerId)) {
                result.add(returnObj);
            }
        }
        return result;
    }

    /**
     * Returns all returns associated with a specific sale.
     */
    public List<Return> viewReturnsBySale(String saleId) {
        List<Return> result = new ArrayList<>();
        for (Return returnObj : viewAllReturns()) {
            if (returnObj.getOriginalSale().getId().equals(saleId)) {
                result.add(returnObj);
            }
        }
        return result;
    }

    /**
     * Calculates the net balance (total sales minus total returns) for a
     * given month and year.
     */
    public double generateMonthlyBalance(int month, int year) {
        double totalSales = 0.0;
        for (Sale sale : saleService.viewAllSales()) {
            if (sale.getDate().getMonthValue() == month && sale.getDate().getYear() == year) {
                totalSales += sale.getTotalAmount();
            }
        }

        double totalReturns = 0.0;
        for (Return returnObj : viewAllReturns()) {
            if (returnObj.getReturnDate().getMonthValue() == month
                    && returnObj.getReturnDate().getYear() == year) {
                totalReturns += returnObj.getRefundAmount();
            }
        }

        return totalSales - totalReturns;
    }

    /**
     * Finds a product with the given id within a sale's product list.
     */
    private Product findInSale(Sale sale, String productId) {
        for (Product product : sale.getProducts()) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    private ReturnRecord toRecord(Return returnObj) {
        List<String> productIds = new ArrayList<>();
        for (Product product : returnObj.getReturnedProducts()) {
            productIds.add(product.getId());
        }
        return new ReturnRecord(
                returnObj.getReturnId(),
                returnObj.getReturnDate(),
                returnObj.getOriginalSale().getId(),
                productIds,
                returnObj.getReason(),
                returnObj.getRefundAmount()
        );
    }

    private Return toReturn(ReturnRecord record) {
        Sale originalSale = saleService.findSaleById(record.getSaleId());

        List<Product> returnedProducts = new ArrayList<>();
        for (String productId : record.getProductIds()) {
            Product product = productService.findById(productId);
            if (product == null) {
                product = accessoryService.findById(productId);
            }
            returnedProducts.add(product);
        }

        Return returnObj = new Return(record.getReturnId(), record.getReturnDate(),
                originalSale, returnedProducts, record.getReason());
        returnObj.setRefundAmount(record.getRefundedAmount());
        return returnObj;
    }
}
