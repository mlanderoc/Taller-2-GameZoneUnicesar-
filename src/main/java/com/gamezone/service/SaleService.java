
package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRecord;
import com.gamezone.persistence.SaleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SaleService {
    private SaleRepository saleRepository;
    private ProductService productService;
    private PersonService personService;

    public SaleService(SaleRepository saleRepository, ProductService productService, PersonService personService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.personService = personService;
    }
    
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

        SaleRecord record = toRecord(sale);
        List<SaleRecord> records = saleRepository.loadAll();
        records.add(record);
        saleRepository.saveAll(records);

        return sale;
    }
    
    public List<Sale> viewAllSales() {
        List<Sale> sales = new ArrayList<>();
        for (SaleRecord record : saleRepository.loadAll()) {
            sales.add(toSale(record));
        }
        return sales;
    }
    
    public List<Sale> viewSalesByCustomer(Customer customer) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : viewAllSales()) {
            if (sale.getCustomer().getId().equals(customer.getId())) {
                result.add(sale);
            }
        }
        return result;
    }
     
    public List<Sale> viewSalesBySeller(Seller seller) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : viewAllSales()) {
            if (sale.getSeller().getId().equals(seller.getId())) {
                result.add(sale);
            }
        }
        return result;
    }
    
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
                sale.getTotalAmount()
        );
    }
    
    private Sale toSale(SaleRecord record) {
        Customer customer = personService.findCustomerById(record.getCustomerId());
        Seller seller = personService.findSellerById(record.getSellerId());

        List<Product> products = new ArrayList<>();
        for (String productId : record.getProductIds()) {
            products.add(productService.findById(productId));
        }

        return new Sale(record.getSaleId(), record.getDate(), customer, seller, products);
    }
}
