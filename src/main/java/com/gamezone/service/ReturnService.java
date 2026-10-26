package com.gamezone.service;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.persistence.Repository;
import com.gamezone.persistence.ReturnRecord;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.validation.SaleValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles business rules for registering and querying product returns,
 * validating the 30-day return window and that returned products
 * actually belong to the referenced sale, restoring stock through
 * ProductService or AccessoryService, and cancelling associated console
 * warranties through WarrantyService.
 */
public class ReturnService {

    private final Repository<ReturnRecord> returnRepository;
    private final SaleQueryService saleService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final WarrantyService warrantyService;

    /**
     * Creates the return service using the {@link Repository} and {@link SaleQueryService}
     * abstractions without a warranty service reference (ISP & DIP).
     *
     * @param returnRepository repository abstraction for return records
     * @param saleService      segregated query service used to look up original sales
     * @param productService   service used to restore and resolve products
     * @param accessoryService service used to restore and resolve accessories
     */
    public ReturnService(Repository<ReturnRecord> returnRepository, SaleQueryService saleService,
                          ProductService productService, AccessoryService accessoryService) {
        this(returnRepository, saleService, productService, accessoryService, null);
    }

    /**
     * Creates the return service using the {@link Repository} and {@link SaleQueryService}
     * abstractions with access to product, accessory, and warranty services (ISP & DIP).
     *
     * @param returnRepository repository abstraction for return records
     * @param saleService      segregated query service used to look up original sales
     * @param productService   service used to restore and resolve products
     * @param accessoryService service used to restore and resolve accessories
     * @param warrantyService  service used to cancel warranties of returned consoles
     */
    public ReturnService(Repository<ReturnRecord> returnRepository, SaleQueryService saleService,
                          ProductService productService, AccessoryService accessoryService,
                          WarrantyService warrantyService) {
        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.warrantyService = warrantyService;
    }

    /**
     * Creates the return service without a warranty service reference.
     *
     * @param returnRepository repository for return records
     * @param saleService service used to look up original sales
     * @param productService service used to restore and resolve products
     * @param accessoryService service used to restore and resolve accessories
     */
    public ReturnService(ReturnRepository returnRepository, SaleService saleService,
                          ProductService productService, AccessoryService accessoryService) {
        this((Repository<ReturnRecord>) returnRepository, (SaleQueryService) saleService, productService, accessoryService, null);
    }

    /**
     * Creates the return service with access to product, accessory, and warranty services
     * so returned products have their stock restored and returned consoles have their
     * associated warranties cancelled and refunded.
     *
     * @param returnRepository repository for return records
     * @param saleService service used to look up original sales
     * @param productService service used to restore and resolve products
     * @param accessoryService service used to restore and resolve accessories
     * @param warrantyService service used to cancel warranties of returned consoles
     */
    public ReturnService(ReturnRepository returnRepository, SaleService saleService,
                          ProductService productService, AccessoryService accessoryService,
                          WarrantyService warrantyService) {
        this((Repository<ReturnRecord>) returnRepository, (SaleQueryService) saleService, productService, accessoryService, warrantyService);
    }

    /**
     * Registers a new return: validates the original sale exists, that it is
     * still within the 30-day return window, and that every product being
     * returned actually belongs to that sale. Restores stock for each
     * returned product, cancels warranties for returned consoles, and persists the return.
     *
     * @param saleId     id of the original sale
     * @param productIds ids of the products being returned
     * @param reason     reason for the return
     * @return the registered Return
     */
    public Return registerReturn(String saleId, List<String> productIds, String reason) {
        Sale originalSale = saleService.findSaleById(saleId);
        SaleValidator.validateReturnData(originalSale, saleId, productIds, reason);

        List<Product> returnedProducts = new ArrayList<>();
        for (String productId : productIds) {
            Product match = findInSale(originalSale, productId);
            if (match == null) {
                throw new BusinessRuleException(
                        "El producto " + productId + " no pertenece a la venta indicada.");
            }
            returnedProducts.add(match);
        }

        String returnId = UUID.randomUUID().toString();
        Return returnObj = new Return(returnId, LocalDate.now(), originalSale, returnedProducts, reason);

        double warrantyRefund = 0.0;
        for (Product product : returnedProducts) {
            if (product instanceof Accessory) {
                accessoryService.restoreStock(product.getId(), 1);
            } else {
                productService.restoreStock(product.getId(), 1);
                if (product instanceof Console && warrantyService != null) {
                    warrantyRefund += warrantyService.cancelWarranties(product.getId(), saleId);
                }
            }
        }

        returnObj.setWarrantyRefundAmount(warrantyRefund);
        returnObj.calculateRefundAmount();

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
     * Calculates the total final amount of all sales registered in the given month and year,
     * after promotional discounts and including extended warranty costs.
     *
     * @param month month of the year (1-12)
     * @param year  calendar year
     * @return total sales amount for the period
     */
    public double calculateMonthlySales(int month, int year) {
        double totalSales = 0.0;
        for (Sale sale : saleService.viewAllSales()) {
            if (sale.getDate() != null
                    && sale.getDate().getMonthValue() == month
                    && sale.getDate().getYear() == year) {
                totalSales += sale.getTotalAmount();
            }
        }
        return totalSales;
    }

    /**
     * Calculates the total refunded amount of all returns registered in the given month and year.
     *
     * @param month month of the year (1-12)
     * @param year  calendar year
     * @return total returns amount for the period
     */
    public double calculateMonthlyReturns(int month, int year) {
        double totalReturns = 0.0;
        for (Return returnObj : viewAllReturns()) {
            if (returnObj.getReturnDate() != null
                    && returnObj.getReturnDate().getMonthValue() == month
                    && returnObj.getReturnDate().getYear() == year) {
                totalReturns += returnObj.getRefundAmount();
            }
        }
        return totalReturns;
    }

    /**
     * Calculates the net balance (total sales minus total returns) for a
     * given month and year.
     *
     * @param month month of the year (1-12)
     * @param year  calendar year
     * @return net balance for the period
     */
    public double generateMonthlyBalance(int month, int year) {
        return calculateMonthlySales(month, year) - calculateMonthlyReturns(month, year);
    }

    /**
     * Finds a product with the given id within a sale's product list
     * by delegating to the Sale Information Expert.
     */
    private Product findInSale(Sale sale, String productId) {
        return sale != null ? sale.findProductById(productId) : null;
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
            if (product == null && accessoryService != null) {
                product = accessoryService.findById(productId);
            }
            if (product != null) {
                returnedProducts.add(product);
            }
        }

        Return returnObj = new Return(record.getReturnId(), record.getReturnDate(),
                originalSale, returnedProducts, record.getReason());
        double productsOnlyRefund = returnObj.calculateRefundAmount();
        double warrantyDiff = record.getRefundedAmount() - productsOnlyRefund;
        if (warrantyDiff > 0.001) {
            returnObj.setWarrantyRefundAmount(warrantyDiff);
        }
        returnObj.setRefundAmount(record.getRefundedAmount());
        return returnObj;
    }
}
