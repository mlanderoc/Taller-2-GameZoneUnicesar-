
package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRecord;
import com.gamezone.persistence.SaleRepository;

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

    public SaleService(SaleRepository saleRepository, ProductService productService, PersonService personService,PromotionService promotionService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.personService = personService;
        this.promotionService = promotionService;
    }
    
  /**
     * Registers a new sale: validates that there is at least one product,
     * verifies and updates stock for each product, and persists the sale.
     *
     * @param customer the customer making the purchase
     * @param seller   the seller attending the sale
     * @param products the products included in the sale
     * @return the registered Sale
     * @throws IllegalArgumentException if products is null/empty or stock is insufficient
     */ 
    
    public Sale registerSale(Customer customer, Seller seller, List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("A sale must contain at least one product.");
        }

        for (Product product : products) {
            Product current = productService.findById(product.getId());
            if (current.getStock() <= 0) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: " + current.getTitle());
            }
        }

        for (Product product : products) {
            productService.updateStock(product.getId(), -1);
        }

        String saleId = UUID.randomUUID().toString();
        Sale sale = new Sale(saleId, LocalDate.now(), customer, seller, products);
        
        Promotion bestPromotion = promotionService.findBestPromotionFor(sale);
        
        if (bestPromotion != null) {
            double discount = bestPromotion.calculateDiscount(sale);
            sale.applyDiscount(bestPromotion.getName(), discount);
        }  
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
                sale.getDiscountAmount()
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
            products.add(productService.findById(productId));
        }

        Sale sale = new Sale(record.getSaleId(), record.getDate(), customer, seller, products);
        sale.setAppliedPromotionName(record.getAppliedPromotionName());
        sale.setDiscountAmount(record.getDiscountAmount());
        
        return sale;
    }
}