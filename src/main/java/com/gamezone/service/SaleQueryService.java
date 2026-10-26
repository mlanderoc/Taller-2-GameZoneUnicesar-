package com.gamezone.service;

import com.gamezone.model.Sale;
import java.util.List;

/**
 * Segregated read-only query contract for looking up registered sales.
 * Applies the Interface Segregation Principle (ISP) and Dependency Inversion
 * Principle (DIP) so collaborating services such as ReturnService do not need
 * to depend on the full concrete SaleService class.
 */
public interface SaleQueryService {

    /**
     * Finds a registered sale by its unique identifier.
     *
     * @param saleId identifier of the sale to find
     * @return the matching Sale, or {@code null} if not found
     */
    Sale findSaleById(String saleId);

    /**
     * Returns the full history of registered sales.
     *
     * @return list of all sales
     */
    List<Sale> viewAllSales();
}
