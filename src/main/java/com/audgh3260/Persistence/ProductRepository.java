package com.audgh3260.Persistence;

import com.audgh3260.Domain.Product;
import com.audgh3260.Persistence.ProductStorage;

import java.util.Map;

public class ProductRepository {
    private final ProductStorage productStorage;

    public ProductRepository(ProductStorage productStorage) {
        this.productStorage = productStorage;
    }
    public void insertOrUpdateProduct(int id, String name, int price, int quantity) {
        Product product = new Product(id, name, price, quantity);
        productStorage.addOrUpdateProduct(product);
    }

    public Product getProduct(int productId) {
        return productStorage.getProduct(productId);
    }

    public boolean removeProduct(int productId) {
        return productStorage.removeProduct(productId);
    }

    public Map<Integer, Product> getAllProducts() {
        return productStorage.getAllProducts();
    }

    public boolean releaseProduct(int productId, int quantity) {
        return productStorage.releaseProduct(productId, quantity);
    }
}
