package com.audgh3260.Ui;

import com.audgh3260.Domain.Product;
import com.audgh3260.service.ProductService;
import com.audgh3260.Persistence.FileProductStorage;

import java.util.Map;
import java.util.Scanner;

public class Application {
    private final ProductService productService;
    private final Scanner scanner;

    public Application(String filename) {
        FileProductStorage fileProductStorage = new FileProductStorage(filename);
        this.productService = new ProductService(fileProductStorage);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n===== 상품 관리 프로그램 =====");
            System.out.println("1. 상품 입고");
            System.out.println("2. 상품 출고");
            System.out.println("3. 상품 찾기");
            System.out.println("4. 상품 삭제");
            System.out.println("5. 모든 상품 보기");
            System.out.println("6. 상품 구매");
            System.out.println("0. 프로그램 종료");
            System.out.print("메뉴 선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerProduct();
                    break;
                case 2:
                    releaseProduct();
                    break;
                case 3:
                    findProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    viewAllProducts();
                    break;
                case 6:
                    purchaseProduct();
                    break;
                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    private void registerProduct() {
        System.out.print("상품 ID 입력: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        System.out.print("상품 명 입력: ");
        String name = scanner.nextLine();

        System.out.print("상품 가격 입력: ");
        int price = scanner.nextInt();

        System.out.print("재고 수량 입력: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        productService.insertOrUpdateProduct(id, name, price, quantity);
        System.out.println("상품 입고 완료: " + name);
    }

    private void findProduct() {
        System.out.print("상품 ID 입력: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        Product product = productService.getProduct(id);
        if (product != null) {
            System.out.println("상품 정보: ID=" + product.getId() + ", 이름=" + product.getName() +
                    ", 가격=" + product.getPrice() + ", 재고=" + product.getQuantity());
        } else {
            System.out.println("해당 ID의 상품이 존재하지 않습니다.");
        }
    }

    private void deleteProduct() {
        System.out.print("삭제할 상품 ID 입력: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        if (productService.removeProduct(id)) {
            System.out.println("상품이 삭제되었습니다.");
        } else {
            System.out.println("해당 ID의 상품이 존재하지 않아 삭제할 수 없습니다.");
        }
    }

    private void viewAllProducts() {
        Map<Integer, Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("등록된 상품이 없습니다.");
        } else {
            System.out.println("등록된 모든 상품:");
            for (Product product : products.values()) {
                System.out.println("ID=" + product.getId() + ", 이름=" + product.getName() +
                        ", 가격=" + product.getPrice() + ", 재고=" + product.getQuantity());
            }
        }
    }

    private void releaseProduct() {
        System.out.print("출고 상품 ID 입력: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        Product product = productService.getProduct(id);
        System.out.println("남은 재고 : " + product.getQuantity());

        System.out.print("출고할 수량 입력: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        if (productService.releaseProduct(id, quantity)) {
            product = productService.getProduct(id);
            System.out.println(product.getName() + " 상품 출고 완료. ");
        } else {
            System.out.println("구매 실패: 재고가 부족하거나 해당 상품이 존재하지 않습니다.");
        }
    }
    private void purchaseProduct() {
        System.out.print("구매할 상품 ID 입력: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        // 입력한 ID의 상품이 존재하는지 확인
        Product product = productService.getProduct(id);
        if (product == null) {
            System.out.println("해당 ID의 상품이 존재하지 않습니다.");
            return; // 상품이 없으므로 메서드 종료
        }

        System.out.print("구매할 수량 입력: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        if (productService.releaseProduct(id, quantity)) {
            System.out.println("상품 구매 완료하였습니다.");
        } else {
            System.out.println("구매 실패: 재고가 부족합니다.");
        }
    }

    public static void main(String[] args) {
        String filename = "src/main/java/com/audgh3260/DB/Product.txt"; // 저장할 파일 이름
        new Application(filename).run();
    }
}