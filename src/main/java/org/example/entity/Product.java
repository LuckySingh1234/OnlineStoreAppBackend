package org.example.entity;

import org.example.OnlineStoreApp;
import org.example.utils.InputUtils;

import java.util.Scanner;

public class Product {
    private String productId;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;

    public Product (String productId, String name, String description, double price, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public static void addProductToStore(OnlineStoreApp store) {
        String id = InputUtils.inputStringWithRegexCheck("^P#[0-9A-Z]{5}$", "Please Enter Product Id", "Product Id should be of seven characters and must start with P#");
        String name = InputUtils.inputStringWithRegexCheck("^[A-Za-z0-9][A-Za-z 0-9 ]{1,20}$", "Please Enter Product Name", "Please Enter a Valid Product Name");
        String desc = InputUtils.inputStringWithRegexCheck("^[A-Za-z0-9][A-Za-z 0-9 ]{1,50}$", "Please Enter Product Description", "Please Enter a Valid Product Description");
        Double price = InputUtils.inputDouble("Please Enter Product Price", "Please Enter a Valid Price");
        Integer quantity = InputUtils.inputInteger("Please Enter Product Quantity", "PLease Enter a Valid Quantity");
        try {
            Product product = new Product(id, name, desc, price, quantity);
            store.addProduct(product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Product Added Successfully");
    }

    public static void removeProductFromStore(OnlineStoreApp store) {
        store.displayProducts();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Product Id to Remove:");
        String productId = sc.nextLine();
        try {
            store.removeProduct(productId);
            System.out.println("Product Removed Successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
