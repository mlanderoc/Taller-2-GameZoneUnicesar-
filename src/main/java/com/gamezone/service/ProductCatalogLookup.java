package com.gamezone.service;

import com.gamezone.model.Product;

/**
 * Segregated read-only catalog lookup interface allowing collaborating services
 * (such as {@link WarrantyService}) to resolve products or accessories by ID
 * without depending on registration or stock-mutation operations (ISP & DIP).
 */
public interface ProductCatalogLookup {

    /**
     * Finds a product or accessory in the catalog by its unique identifier.
     *
     * @param id the identifier to search for
     * @return the matching {@link Product}, or {@code null} if not found
     */
    Product findById(String id);
}
