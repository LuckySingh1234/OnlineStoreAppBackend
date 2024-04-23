package org.example.entity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.OnlineStoreApp;
import org.example.utils.InputUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;

public class Customer extends User {
    public Customer(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public static Customer login(String email, String password, Set<Customer> customerSet) {
        for(Customer c : customerSet) {
            if (email.equals(c.getEmail()) && password.equals(c.getPassword())) {
                System.out.println("Logged In Successfully");
                System.out.println("Welcome " + c.getName());
                return new Customer(c.getId(), c.getName(), c.getEmail(), c.getPassword());
            }
        }
        return null;
    }

    public static void addCustomerToStore(OnlineStoreApp store) {
        String id = store.getNextCustomerId();
        String name = InputUtils.inputStringWithRegexCheck("^[A-Za-z]{1,20}$", "Please Enter Customer's Name", "Please Enter a Valid Name");
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

    public static void generateExcelSheet(OnlineStoreApp store) {
        String[] headers = {"CustomerId", "Name", "Email", "Password"};
        String filePath = "data/all_store_data.xlsx";
        File directory = new File(filePath.substring(0, filePath.lastIndexOf("/")));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String sheetName = "Customers";
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                // If the file exists, open it
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
            } else {
                // If the file doesn't exist, create a new workbook
                workbook = new XSSFWorkbook();
            }
            int indexToDelete = workbook.getSheetIndex(sheetName);
            if (indexToDelete != -1) {
                workbook.removeSheetAt(indexToDelete);
            }
            Sheet sheet = workbook.createSheet("Customers");
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            // Create data rows
            int rowNum = 0;
            for (Customer customer : store.getCustomerSet()) {
                int colNum = 0;
                Row row = sheet.createRow(rowNum + 1);
                Cell cell = row.createCell(colNum);
                cell.setCellValue(customer.getId());
                colNum++;
                cell = row.createCell(colNum);
                cell.setCellValue(customer.getName());
                colNum++;
                cell = row.createCell(colNum);
                cell.setCellValue(customer.getEmail());
                colNum++;
                cell = row.createCell(colNum);
                cell.setCellValue(customer.getPassword());
                rowNum++;
            }
            // Write the Excel file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                System.out.println("Excel file created successfully at the below location");
                System.out.println(Paths.get(filePath).toAbsolutePath());
            } catch (IOException e) {
                System.out.println("Error creating Excel file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error creating Excel file: " + e.getMessage());
        }
    }

    public static void importCustomersFromExcel(OnlineStoreApp store) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the file path");
        String filePath = sc.nextLine();
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Customers");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        Cell cell = row.getCell(0);
                        String customerId = cell.getStringCellValue();
                        if (!customerId.matches("^C#[0-9A-Z]{5}$")) {
                            excelErrors.append("Customer Id does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        cell = row.getCell(1);
                        String name = cell.getStringCellValue();
                        if (!name.matches("^[A-Za-z][A-Za-z ]{0,20}$")) {
                            excelErrors.append("Customer Name does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        cell = row.getCell(2);
                        String email = cell.getStringCellValue();
                        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                            excelErrors.append("Customer email does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        cell = row.getCell(3);
                        String password = cell.getStringCellValue();
                        if (password == null || password.isEmpty()) {
                            excelErrors.append("Customer password is empty at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        Customer c = new Customer(customerId, name, email, password);
                        try {
                            store.addCustomer(c);
                        } catch (Exception e) {
                            excelErrors.append(e.getMessage()).append(" for Row: ").append(i + 1).append("\n");
                        }
                    }
                    System.out.println("Customers Loaded Successfully.");
                    System.out.println(excelErrors);
                } else {
                    System.out.println("Customers sheet is not available in the excel file");
                }
            } else {
                System.out.println("File does not exist at the specified path");
            }
        } catch (IOException e) {
            System.out.println("Error opening Excel file: " + e.getMessage());
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
