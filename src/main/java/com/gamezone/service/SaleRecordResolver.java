package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.persistence.Repository;
import com.gamezone.persistence.SaleRecord;
import java.util.ArrayList;
import java.util.List;

/**
 * Dedicated helper responsible for reconstructing {@link Sale} domain references
 * from persisted {@link SaleRecord} instances on behalf of warranty queries,
 * separating sale-record mapping from warranty lifecycle management (SRP, ISP, DIP).
 */
public class SaleRecordResolver {

    private final Repository<SaleRecord> saleRepository;
    private final ProductCatalogLookup productLookup;

    /**
     * Creates a resolver backed by a sale-record repository abstraction and a
     * read-only product catalog lookup.
     *
     * @param saleRepository repository used to load persisted sale records
     * @param productLookup  lookup service used to resolve product identifiers
     */
    public SaleRecordResolver(Repository<SaleRecord> saleRepository, ProductCatalogLookup productLookup) {
        this.saleRepository = saleRepository;
        this.productLookup = productLookup;
    }

    /**
     * Resolves a persisted {@link SaleRecord} by its identifier into a {@link Sale}
     * object containing the associated products and promotional totals.
     *
     * @param saleId          identifier of the sale to resolve
     * @param fallbackProduct fallback product if no catalog items are resolved
     * @return the reconstructed {@link Sale}, or {@code null} if not found
     */
    public Sale resolveSaleById(String saleId, Product fallbackProduct) {
        for (SaleRecord saleRecord : saleRepository.loadAll()) {
            if (saleRecord.getSaleId().equals(saleId)) {
                List<Product> products = new ArrayList<>();
                for (String pid : saleRecord.getProductIds()) {
                    Product item = productLookup.findById(pid);
                    if (item != null) {
                        products.add(item);
                    }
                }
                if (products.isEmpty() && fallbackProduct != null) {
                    products.add(fallbackProduct);
                }
                if (products.isEmpty()) {
                    return null;
                }
                Sale sale = new Sale(saleRecord.getSaleId(), saleRecord.getDate(), null, null, products);
                sale.setSubtotal(saleRecord.getSubtotal());
                sale.setAppliedPromotionName(saleRecord.getAppliedPromotionName());
                sale.setDiscountAmount(saleRecord.getDiscountAmount());
                return sale;
            }
        }
        return null;
    }
}
