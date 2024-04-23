package org.example.entity;

import org.example.OnlineStoreApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Manager extends User {
    private Set<String> actions = new HashSet<>();
    public Manager(String name, String email, String password, String allActions) {
        super(name, email, password);
        String[] actionsArr = allActions.split("_");
        this.actions.addAll(Arrays.asList(actionsArr));
    }
    public static Manager login(String email, String password) {
        String path = "data/ManagerCredentials.csv";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] storedCredentials = line.split(",");
                String storedEmail = storedCredentials[1];
                String storedPassword = storedCredentials[2];
                String allActions = storedCredentials[3];
                if (email.equals(storedEmail) && password.equals(storedPassword)) {
                    System.out.println("Logged In Successfully");
                    System.out.println("Welcome " + storedCredentials[0]);
                    return new Manager(storedCredentials[0], storedCredentials[1], storedCredentials[2], allActions);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void printChoices() {
        if (actions.contains("add")) {
            System.out.println("Enter 1 to Add Product");
            System.out.println("Enter 2 to Add Customer");
            System.out.println("Enter 3 to Edit Product");
            System.out.println("Enter 4 Import Products from File");
            System.out.println("Enter 5 Import Customers from File");
        }
        if (actions.contains("view")) {
            System.out.println("Enter 6 to View All Products");
            System.out.println("Enter 7 to View All Orders");
            System.out.println("Enter 8 to View All Customers");
            System.out.println("Enter 9 to Generate Excel file for customers data");
            System.out.println("Enter 10 to Generate Excel file for Products data");
        }
        if (actions.contains("remove")) {
            System.out.println("Enter 11 to Remove Product");
            System.out.println("Enter 12 to Remove Customer");
        }
        System.out.println("Enter 13 to Sign Out");
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
                    if (actions.contains("add")) {
                        Product.addProductToStore(store);
                    } else {
                        System.out.println("You don't have permission to add product");
                    }
                    break;
                }
                case 2: {
                    if (actions.contains("add")) {
                        Customer.addCustomerToStore(store);
                    } else {
                        System.out.println("You don't have permission to add customer");
                    }
                    break;
                }
                case 3: {
                    if(actions.contains("add")) {
                        Product.editProduct(store);
                    } else {
                        System.out.println("You don't have permission to edit product");
                    }
                    break;
                }
                case 4: {
                    if (actions.contains("add")) {
                        Product.importProductsFromExcel(store);
                    } else {
                        System.out.println("You don't have permission to import products from excel");
                    }
                    break;
                }
                case 5: {
                    if (actions.contains("add")) {
                        Customer.importCustomersFromExcel(store);
                    } else {
                        System.out.println("You don't have permission to import customers from excel");
                    }
                    break;
                }
                case 6: {
                    if (actions.contains("view")) {
                        store.displayProducts();
                    } else {
                        System.out.println("You don't have permission to view products");
                    }
                    break;
                }
                case 7: {
                    if (actions.contains("view")) {
                        store.displayOrders();
                    } else {
                        System.out.println("You don't have permission to view orders");
                    }
                    break;
                }
                case 8: {
                    if (actions.contains("view")) {
                        store.displayCustomers();
                    } else {
                        System.out.println("You don't have permission to view customers");
                    }
                    break;
                }
                case 9: {
                    if(actions.contains("view")) {
                        Customer.generateExcelSheet(store);
                    } else {
                        System.out.println("You don't have permission to Generate Excel Sheet");
                    }
                    break;
                }
                case 10: {
                    if(actions.contains("view")) {
                        Product.generateExcelSheet(store);
                    } else {
                        System.out.println("You don't have permission to Generate Excel Sheet");
                    }
                    break;
                }
                case 11: {
                    if (actions.contains("remove")) {
                        Product.removeProductFromStore(store);
                    } else {
                        System.out.println("You don't have permission to remove products");
                    }
                    break;
                }
                case 12: {
                    if (actions.contains("remove")) {
                        Customer.removeCustomerFromStore(store);
                    } else {
                        System.out.println("You don't have permission to remove customers ");
                    }
                    break;
                }
                case 13: {
                    break;
                }
                default: {
                    System.out.println("Please Enter Valid Choice");
                }
            }
        } while (choice != 13);
    }
}
