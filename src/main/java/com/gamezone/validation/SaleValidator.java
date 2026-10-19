package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;

import java.util.List;

/**
 * Provides static validation methods for sales and returns,
 * verifying customer, seller, non-empty product lists, sale existence,
 * and return window rules.
 */
public class SaleValidator {

    private SaleValidator() {
        // Utility class with static methods only
    }

    /**
     * Validates that a sale has a valid customer, a valid seller, and at least one product.
     *
     * @param customer customer making the purchase
     * @param seller   seller attending the sale
     * @param products list of products included in the sale
     * @throws InvalidDataException  if the customer or seller reference is null
     * @throws BusinessRuleException if the product list is null or empty
     */
    public static void validateSaleData(Customer customer, Seller seller, List<Product> products) {
        if (customer == null) {
            throw new InvalidDataException("cliente", "El cliente de la venta no puede ser nulo.");
        }
        if (seller == null) {
            throw new InvalidDataException("vendedor", "El vendedor de la venta no puede ser nulo.");
        }
        if (products == null || products.isEmpty()) {
            throw new BusinessRuleException("Una venta debe contener al menos un producto.");
        }
    }

    /**
     * Validates that a looked-up sale reference is not null.
     *
     * @param sale   sale reference returned from a lookup
     * @param saleId identifier that was searched
     * @throws ResourceNotFoundException if the sale is null
     */
    public static void validateSaleExists(Sale sale, String saleId) {
        if (sale == null) {
            throw new ResourceNotFoundException("venta", saleId);
        }
    }

    /**
     * Validates that a return request meets the 30-day return policy and has at least one product.
     *
     * @param originalSale original sale being returned
     * @param saleId       identifier of the original sale
     * @param productIds   identifiers of the products to return
     * @param reason       reason for the return
     * @throws ResourceNotFoundException if the original sale does not exist
     * @throws BusinessRuleException     if the 30-day return window has expired or product list is empty
     * @throws InvalidDataException      if the return reason is empty
     */
    public static void validateReturnData(Sale originalSale, String saleId, List<String> productIds, String reason) {
        validateSaleExists(originalSale, saleId);
        if (!originalSale.canBeReturned()) {
            throw new BusinessRuleException("El plazo de 30 días para devolver esta venta ya expiró.");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new BusinessRuleException("Debe indicar al menos un producto a devolver.");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new InvalidDataException("motivo", "El motivo de la devolución no puede estar vacío.");
        }
    }
}
