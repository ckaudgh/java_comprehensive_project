package com.audgh3260.Persistence;

import com.audgh3260.Domain.Product;

import java.util.Map;

public interface ProductStorage {
    void addOrUpdateProduct(Product product);
    Product getProduct(int productId);
    boolean removeProduct(int productId);
    Map<Integer, Product> getAllProducts();
    boolean releaseProduct(int productId, int quantity);
    void saveToFile();
    void loadFromFile();
}