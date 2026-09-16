/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gamezone.service;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Return;
import com.gamezone.persistence.ReturnRepository;
import java.util.UUID;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReturnService {

    private final ReturnRepository returnRepository;
    private final SaleService saleService;
    private final ProductService productService;

    public ReturnService(ReturnRepository returnRepository, SaleService saleService, ProductService productService) {
        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.productService = productService;
    }

    public Return registerReturn(String saleId, List<String> productIds, String reason) {

        Sale originalSale = null;

        for (Sale sale : saleService.viewAllSales()) {
            if (sale.getId().equals(saleId)) {
                originalSale = sale;
                break;
            }
        }

        if (originalSale == null) {
            throw new IllegalArgumentException("The original sale does not exist.");
        }

        if (!originalSale.canBeReturned()) {
            throw new IllegalArgumentException(
                    "The sale is outside the 30-day return period."
            );
        }

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one product must be selected for the return."
            );
        }

        List<Product> returnedProducts = new ArrayList<>();

        for (String productId : productIds) {

            Product product = productService.findById(productId);

            if (product == null) {
                throw new IllegalArgumentException(
                        "The product with ID " + productId + " does not exist."
                );
            }

            boolean belongsToSale = false;

            for (Product saleProduct : originalSale.getProducts()) {
                if (saleProduct.getId().equals(productId)) {
                    belongsToSale = true;
                    break;
                }
            }

            if (!belongsToSale) {
                throw new IllegalArgumentException(
                        "The product with ID " + productId
                        + " does not belong to the original sale."
                );
            }

            returnedProducts.add(product);
        }

        String returnId = UUID.randomUUID().toString();

        Return productReturn = new Return(
                returnId,
                LocalDate.now(),
                originalSale,
                returnedProducts,
                reason
        );

        for (Product product : returnedProducts) {
            product.updateStock(1);
        }

        List<Return> returns = returnRepository.loadAll();
        returns.add(productReturn);

        returnRepository.saveAll(returns);

        return productReturn;
    }

    public List<Return> viewAllReturns() {
        return returnRepository.loadAll();
    }

    public List<Return> viewReturnsByCustomer(String customerId) {

        List<Return> result = new ArrayList<>();

        for (Return productReturn : returnRepository.loadAll()) {

            Sale sale = productReturn.getOriginalSale();

            if (sale.getCustomer().getId().equals(customerId)) {
                result.add(productReturn);
            }
        }

        return result;
    }

    public List<Return> viewReturnsBySale(String saleId) {

        List<Return> result = new ArrayList<>();

        for (Return productReturn : returnRepository.loadAll()) {

            if (productReturn.getOriginalSale().getId().equals(saleId)) {
                result.add(productReturn);
            }
        }

        return result;
    }

    public double generateMonthlyBalance(int month, int year) {

        double totalSales = 0.0;
        double totalReturns = 0.0;

        for (Sale sale : saleService.viewAllSales()) {

            LocalDate saleDate = sale.getDate();

            if (saleDate.getMonthValue() == month
                    && saleDate.getYear() == year) {

                totalSales += sale.getTotalAmount();
            }
        }

        for (Return productReturn : returnRepository.loadAll()) {

            LocalDate returnDate = productReturn.getReturnDate();

            if (returnDate.getMonthValue() == month
                    && returnDate.getYear() == year) {

                totalReturns += productReturn.getRefundAmount();
            }
        }

        return totalSales - totalReturns;
    }

}
