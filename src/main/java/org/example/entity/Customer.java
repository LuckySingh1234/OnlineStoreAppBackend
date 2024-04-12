package org.example.entity;

import org.example.OnlineStoreApp;
import org.example.utils.InputUtils;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Customer extends User {
    public Customer(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public static Customer login(String email, String password) {
        Path path = Paths.get("data/CustomerCredentials.csv");
        try {
            BufferedReader reader = Files.newBufferedReader(path);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] storedCredentials = line.split(",");
                String storedEmail = storedCredentials[2];
                String storedPassword = storedCredentials[3];
                if (email.equals(storedEmail) && password.equals(storedPassword)) {
                    System.out.println("Logged In Successfully");
                    System.out.println("Welcome " + storedCredentials[1]);
                    return new Customer(storedCredentials[0], storedCredentials[1], storedCredentials[2], storedCredentials[3]);
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public static void addCustomerToStore(OnlineStoreApp store) {
        String id = store.getNextCustomerId();
        String name = InputUtils.inputStringWithRegexCheck("^[A-Za-z][A-Za-z ]{0,20}$", "Please Enter Customer's Name", "Please Enter a Valid Name");
        String email = InputUtils.inputStringWithRegexCheck("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", "Please Enter Email:", "Email Id is Incorrect");
        String password = InputUtils.inputString("Please Enter Customer's Password", "Please Enter a Valid Password");
        Customer currentCustomer = new Customer(id, name, email, password);
        try {
            store.addCustomer(currentCustomer);
            System.out.println("Customer added successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeCustomerFromStore(OnlineStoreApp store) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Customer Id to Remove:");
        String customerId = sc.nextLine();
        try {
            store.removeCustomer(customerId);
            System.out.println("Customer Removed Successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void printChoices() {
        System.out.println("Enter 1 to View All Products In Store");
        System.out.println("Enter 2 to Add to Cart");
        System.out.println("Enter 3 to View Cart Items");
        System.out.println("Enter 4 to Clear Cart Items");
        System.out.println("Enter 5 to Place Order");
        System.out.println("Enter 6 to View All Order Details");
        System.out.println("Enter 7 to Sign Out");
    }

    @Override
    public void performActions(OnlineStoreApp store) {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            printChoices();
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1: {
                    store.displayProducts();
                    break;
                }
                case 2: {
                    System.out.println("Enter Product Id");
                    String productId = sc.nextLine();
                    System.out.println("Enter Quantity");
                    String qty = sc.nextLine();
                    Integer quantity = null;
                    try {
                        quantity = Integer.parseInt(qty);
                    } catch (Exception e) {
                        System.out.println("Invalid Quantity");
                        break;
                    }
                    try {
                        store.addToCart(productId, quantity, this.getId());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    System.out.println("Product added to Cart Successfully");
                    break;
                }
                case 3: {
                    store.viewCart(this.getId());
                    break;
                } case 4: {
                    store.clearCart(this.getId());
                    break;
                }
                case 5: {
                    try {
                        store.placeOrder(store.getNextOrderId(), this.getId());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    System.out.println("Order Placed Successfully");
                    break;
                }
                case 6: {
                    store.displayOrders(this.getId());
                    break;
                }
                case 7: {
                    break;
                }
                default: {
                    System.out.println("Please Enter Valid Choice");
                }
            }
        } while (choice != 7);
    }
}
