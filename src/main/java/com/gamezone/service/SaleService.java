
package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.model.Accessory;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.SaleRecord;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.model.Console;
import com.gamezone.model.ExtendedWarranty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles the business rules for registering and querying sales,
 * coordinating stock validation and updates through ProductService,
 * and resolving persisted SaleRecords back into full Sale objects.
 */
public class SaleService {
    private SaleRepository saleRepository;
    private ProductService productService;
    private PersonService personService;
    private PromotionService promotionService;
    private AccessoryService accessoryService;
    private WarrantyService warrantyService;

    public SaleService(SaleRepository saleRepository, ProductService productService, PersonService personService, PromotionService promotionService, AccessoryService accessoryService) {
        this(saleRepository, productService, personService, promotionService, accessoryService, null);
    }

    public SaleService(SaleRepository saleRepository, ProductService productService, PersonService personService,
                       PromotionService promotionService, AccessoryService accessoryService, WarrantyService warrantyService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.personService = personService;
        this.promotionService = promotionService;
        this.accessoryService = accessoryService;
        this.warrantyService = warrantyService;
    }

    public void setWarrantyService(WarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }
    /**
     * Registers a new sale following the unified 8-step registration flow:
     * 1. Validate that the sale has at least one item.
     * 2. Resolve each item as a product or accessory and validate its stock.
     * 3. Create the sale and calculate the item subtotal.
     * 4. Query the best active promotion and apply the discount over the subtotal.
     * 5. Generate basic warranties for consoles and requested extended warranties, summing their cost.
     * 6. Calculate the final total: subtotal - discount + extended warranty cost.
     * 7. Update inventory delegating to ProductService or AccessoryService according to item type.
     * 8. Persist the sale and warranties.
     *
     * @param customer the customer making the purchase
     * @param seller   the seller attending the sale
     * @param items    the products and accessories included in the sale
     * @param productIdsWithExtendedWarranty console IDs that requested an extended warranty
     * @return the registered Sale
     * @throws IllegalArgumentException if items is null/empty or stock is insufficient
     */
    public Sale registerSale(Customer customer, Seller seller, List<Product> items, List<String> productIdsWithExtendedWarranty) {
        // 1. Validate that the sale has at least one item
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Una venta debe contener al menos un producto");
        }

        // 2. Resolve each item as product or accessory and validate its stock
        List<Product> resolvedItems = new ArrayList<>();
        for (Product item : items) {
            Product current;
            if (item instanceof Accessory) {
                current = accessoryService.findById(item.getId());
            } else {
                current = productService.findById(item.getId());
            }

            if (current == null) {
                throw new IllegalArgumentException("Producto o accesorio no encontrado: " + item.getId());
            }
            if (current.getStock() <= 0) {
                throw new IllegalArgumentException("Inventario insuficiente: " + current.getTitle());
            }
            resolvedItems.add(current);
        }

        // 3. Create the sale and calculate the subtotal
        String saleId = UUID.randomUUID().toString();
        Sale sale = new Sale(saleId, LocalDate.now(), customer, seller, resolvedItems);

        // 4. Query PromotionService.findBestPromotionFor(sale) and register discount over item subtotal
        Promotion bestPromotion = promotionService.findBestPromotionFor(sale);
        if (bestPromotion != null) {
            double discount = bestPromotion.calculateDiscount(sale);
            sale.applyDiscount(bestPromotion.getName(), discount);
        }

        // 5. Generate basic warranty for each console and requested extended warranties, summing their cost
        double extendedWarrantyCost = 0.0;
        for (Product item : resolvedItems) {
            if (item instanceof Console && warrantyService != null) {
                warrantyService.assignBasicWarranty(item, sale, sale.getDate());
                if (productIdsWithExtendedWarranty != null && productIdsWithExtendedWarranty.contains(item.getId())) {
                    ExtendedWarranty warranty = warrantyService.assignExtendedWarranty(item, sale, sale.getDate());
                    extendedWarrantyCost += warranty.getAdditionalCost();
                }
            }
        }

        // 6. Calculate final total: subtotal - discount + extended warranty cost
        sale.setExtendedWarrantyCost(extendedWarrantyCost);

        // 7. Update inventory delegating to ProductService or AccessoryService according to item type
        for (Product item : resolvedItems) {
            if (item instanceof Accessory) {
                accessoryService.updateStock(item.getId(), -1);
            } else {
                productService.updateStock(item.getId(), -1);
            }
        }

        // 8. Persist the sale (warranties were persisted upon assignment)
        SaleRecord record = toRecord(sale);
        List<SaleRecord> records = saleRepository.loadAll();
        records.add(record);
        saleRepository.saveAll(records);

        return sale;
    }
    
    /**
     * Returns the full history of sales, resolving each SaleRecord into a Sale object.
     *
     * @return the list of all sales
     */
    
    public List<Sale> viewAllSales() {
        List<Sale> sales = new ArrayList<>();
        for (SaleRecord record : saleRepository.loadAll()) {
            sales.add(toSale(record));
        }
        return sales;
    }
    
    /**
     * Returns the purchase history of a specific customer.
     *
     * @param customer the customer to filter by
     * @return the list of sales made by that customer
     */

    public List<Sale> viewSalesByCustomer(Customer customer) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : viewAllSales()) {
            if (sale.getCustomer().getId().equals(customer.getId())) {
                result.add(sale);
            }
        }
        return result;
    }
    
     /**
     * Returns the sales attended by a specific seller.
     *
     * @param seller the seller to filter by
     * @return the list of sales attended by that seller
     */
    
    public List<Sale> viewSalesBySeller(Seller seller) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : viewAllSales()) {
            if (sale.getSeller().getId().equals(seller.getId())) {
                result.add(sale);
            }
        }
        return result;
    }
    /**
     * Finds a sale by its id.
     *
     * @param saleId the id of the sale to find
     * @return the sale, or null if not found
     */
    public Sale findSaleById(String saleId) {
        for (Sale sale : viewAllSales()) {
            if (sale.getId().equals(saleId)) {
                return sale;
            }
        }
        return null;
    }
    
    /**
     * Converts a full Sale object into its persisted record form (raw ids).
     */
    
    private SaleRecord toRecord(Sale sale) {
        List<String> productIds = new ArrayList<>();
        for (Product product : sale.getProducts()) {
            productIds.add(product.getId());
        }
        return new SaleRecord(
                sale.getId(),
                sale.getDate(),
                sale.getCustomer().getId(),
                sale.getSeller().getId(),
                productIds,
                sale.getSubtotal(),
                sale.getAppliedPromotionName(),
                sale.getDiscountAmount(),
                sale.getExtendedWarrantyCost()
        );
    }
    
    /**
     * Resolves a persisted SaleRecord back into a full Sale object,
     * looking up the referenced customer, seller, and products.
     */
    
    private Sale toSale(SaleRecord record) {
        Customer customer = personService.findCustomerById(record.getCustomerId());
        Seller seller = personService.findSellerById(record.getSellerId());

        List<Product> products = new ArrayList<>();
        
        for (String productId : record.getProductIds()) {
            Product item = productService.findById(productId);
            
            if (item == null) {
                item = accessoryService.findById(productId);
            }
            if (item != null) {
                products.add(item);
            }
        }

        Sale sale = new Sale(record.getSaleId(), record.getDate(), customer, seller, products);
        sale.setSubtotal(record.getSubtotal());
        sale.setAppliedPromotionName(record.getAppliedPromotionName());
        sale.setDiscountAmount(record.getDiscountAmount());
        sale.setExtendedWarrantyCost(record.getExtendedWarrantyCost());
        
        return sale;
    }
}