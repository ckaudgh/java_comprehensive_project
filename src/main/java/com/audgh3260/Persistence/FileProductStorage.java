package com.audgh3260.Persistence;

import com.audgh3260.Domain.Product;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileProductStorage implements ProductStorage {
    private Map<Integer, Product> products = new HashMap<>();
    private final String filename;

    public FileProductStorage(String filename) {
        this.filename = "src/main/java/com/audgh3260/DB/Product.txt";
        loadFromFile();
    }

    @Override
    public void addOrUpdateProduct(Product product) {
        if (products.containsKey(product.getId())) {
            Product existingProduct = products.get(product.getId());
            existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
        } else {
            products.put(product.getId(), product);
        }
        saveToFile(); // 상품 등록 시 자동으로 저장
    }

    @Override
    public Product getProduct(int productId) {
        return products.get(productId);
    }

    @Override
    public boolean removeProduct(int productId) {
        boolean removed = products.remove(productId) != null;
        if (removed) {
            saveToFile(); // 상품 삭제 시 자동으로 저장
        }
        return removed;
    }

    @Override
    public Map<Integer, Product> getAllProducts() {
        return products;
    }

    @Override
    public boolean releaseProduct(int productId, int quantity) {
        Product product = products.get(productId);
        if (product != null && product.getQuantity() >= quantity) {
            product.setQuantity(product.getQuantity() - quantity);
            saveToFile(); // 구매 후 자동으로 저장
            return true; // 구매 성공
        }
        return false; // 구매 실패
    }

    @Override
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Product product : products.values()) {
                writer.write(product.getId() + "," + product.getName() + "," +
                        product.getPrice() + "," + product.getQuantity());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Override
    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                int price = Integer.parseInt(parts[2]);
                int quantity = Integer.parseInt(parts[3]);
                Product product = new Product(id, name, price, quantity);
                addOrUpdateProduct(product);
            }
            System.out.println("상품 정보가 " + filename + "에서 불러와졌습니다.");
        } catch (IOException e) {
            System.out.println("파일 불러오기 중 오류가 발생했습니다: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("상품 정보 형식이 잘못되었습니다: " + e.getMessage());
        }
    }
}
