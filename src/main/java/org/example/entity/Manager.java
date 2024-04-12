package org.example.entity;

import org.example.OnlineStoreApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        }
        if (actions.contains("view")) {
            System.out.println("Enter 3 to View All Products");
            System.out.println("Enter 4 to View All Orders");
            System.out.println("Enter 5 to View All Customers");
        }
        if (actions.contains("remove")) {
            System.out.println("Enter 6 to Remove Product");
            System.out.println("Enter 7 to Remove Customer");
        }
        System.out.println("Enter 8 to Sign Out");
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
                        System.out.println("Please Enter a Valid Choice");
                    }
                    break;
                }
                case 2: {
                    if (actions.contains("add")) {
                        Customer.addCustomerToStore(store);
                    } else {
                        System.out.println("Please Enter a Valid Choice");
                    }
                    break;
                }
                case 3: {
                    if (actions.contains("view")) {
                        store.displayProducts();
                    } else {
                        System.out.println("Please Enter a Valid Choice");
                    }
                    break;
                }
                case 4: {
                    if (actions.contains("view")) {
                        store.displayOrders();
                    } else {
                        System.out.println("Please Enter a Valid Choice");
                    }
                    break;
                }
                case 5: {
                    if (actions.contains("view")) {
                        store.displayCustomers();
                    } else {
                        System.out.println("Please Enter a Valid Choice");
                    }
                    break;
                }
                case 6: {
                    if (actions.contains("remove")) {
                        Product.removeProductFromStore(store);
                    } else {
                        System.out.println("Please Enter a Valid Choice");
                    }
                    break;
                } case 7: {
                    if (actions.contains("remove")) {
                        Customer.removeCustomerFromStore(store);
                    } else {
                        System.out.println("Please Enter a Valid Choice");
                    }
                    break;
                }
                case 8: {
                    break;
                }
                default: {
                    System.out.println("Please Enter Valid Choice");
                }
            }
        } while (choice != 8);
    }
}
