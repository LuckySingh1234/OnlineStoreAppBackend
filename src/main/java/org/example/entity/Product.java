package org.example.entity;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.OnlineStoreApp;
import org.example.utils.InputUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

@Builder
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

    public static void editProduct(OnlineStoreApp store) {
        String id = InputUtils.inputStringWithRegexCheck("^P#[0-9A-Z]{5}$", "Please Enter Product Id", "Product Id should be of seven characters and must start with P#");

        try {
            Product existingProduct = store.getProduct(id);

            Scanner sc = new Scanner(System.in);

            System.out.println("Do you want to edit product name ? Yes or No");
            String ans = sc.nextLine();
            String name = null;
            while (true) {
                if (ans.equalsIgnoreCase("YES")) {
                    name = InputUtils.inputStringWithRegexCheck("^[A-Za-z0-9][A-Za-z 0-9 ]{1,20}$", "Please Enter Product Name", "Please Enter a Valid Product Name");
                    break;
                } else if (ans.equalsIgnoreCase("NO")) {
                    break;
                } else {
                    System.out.println("Please answer in YES or NO.");
                }
            }

            System.out.println("Do you want to edit product description ? Yes or No");
            ans = sc.nextLine();
            String desc = null;
            while (true) {
                if (ans.equalsIgnoreCase("YES")) {
                    desc = InputUtils.inputStringWithRegexCheck("^[A-Za-z0-9][A-Za-z 0-9 ]{1,50}$", "Please Enter Product Description", "Please Enter a Valid Product Description");
                    break;
                } else if (ans.equalsIgnoreCase("NO")) {
                    break;
                } else {
                    System.out.println("Please answer in YES or NO.");
                }
            }

            System.out.println("Do you want to edit product price ? Yes or No");
            ans = sc.nextLine();
            Double price = null;
            while (true) {
                if (ans.equalsIgnoreCase("YES")) {
                    price = InputUtils.inputDouble("Please Enter Product Price", "Please Enter a Valid Price");
                    break;
                } else if (ans.equalsIgnoreCase("NO")) {
                    break;
                } else {
                    System.out.println("Please answer in YES or NO.");
                }
            }

            System.out.println("Do you want to edit product quantity ? Yes or No");
            ans = sc.nextLine();
            Integer quantity = null;
            while (true) {
                if (ans.equalsIgnoreCase("YES")) {
                    quantity = InputUtils.inputInteger("Please Enter Product Quantity", "PLease Enter a Valid Quantity");
                    break;
                } else if (ans.equalsIgnoreCase("NO")) {
                    break;
                } else {
                    System.out.println("Please answer in YES or NO.");
                }
            }

            Product updatedProduct = Product.builder()
                    .productId(id)
                    .name(Optional.ofNullable(name).orElse(existingProduct.getName()))
                    .description(Optional.ofNullable(desc).orElse(existingProduct.getDescription()))
                    .price(Optional.ofNullable(price).orElse(existingProduct.getPrice()))
                    .stockQuantity(Optional.ofNullable(quantity).orElse(existingProduct.getStockQuantity()))
                    .build();
            store.updateProduct(updatedProduct);
            System.out.println("Product Updated Successfully!!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    public static void generateExcelSheet(OnlineStoreApp store) {
        String[] headers = {"ProductId", "Name", "Description", "Price", "Quantity"};
        String filePath = "data/all_store_data.xlsx";
        String sheetName = "Products";
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
            Sheet sheet = workbook.createSheet("Products");
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            // Create data rows
            int rowNum = 0;
            for (Product product : store.getProducts()) {
                int colNum = 0;
                Row row = sheet.createRow(rowNum + 1);
                Cell cell = row.createCell(colNum);
                cell.setCellValue(product.getProductId());
                colNum++;
                cell = row.createCell(colNum);
                cell.setCellValue(product.getName());
                colNum++;
                cell = row.createCell(colNum);
                cell.setCellValue(product.getDescription());
                colNum++;
                cell = row.createCell(colNum);
                cell.setCellValue(product.getPrice());
                colNum++;
                cell = row.createCell(colNum);
                cell.setCellValue(product.getStockQuantity());
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

    public static void importProductsFromExcel(OnlineStoreApp store) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the file path");
        String filePath = sc.nextLine();
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Products");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        Cell cell = row.getCell(0);
                        String productId = cell.getStringCellValue();
                        if (!productId.matches("^P#[0-9A-Z]{5}$")) {
                            excelErrors.append("Product Id does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        cell = row.getCell(1);
                        String name = cell.getStringCellValue();
                        if (!name.matches("^[A-Za-z0-9\s]{1,20}$")) {
                            excelErrors.append("Product Name does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        cell = row.getCell(2);
                        String desc = cell.getStringCellValue();
                        if (!desc.matches("^[A-Za-z0-9][A-Za-z 0-9 ]{1,50}$")) {
                            excelErrors.append("Product Description does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        cell = row.getCell(3);
                        Double price;
                        try {
                            price = cell.getNumericCellValue();
                            if (price <= 0) {
                                excelErrors.append("Product Price is less than or equal to zero at Row: ").append(i + 1).append("\n");
                                continue;
                            }
                        } catch (Exception e) {
                            excelErrors.append("Incorrect Price at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        cell = row.getCell(4);
                        Integer qty;
                        try {
                            Double doubleQty = cell.getNumericCellValue();
                            qty = doubleQty.intValue();
                            if (doubleQty.compareTo(qty.doubleValue()) != 0) {
                                excelErrors.append("Stock Quantity is fractional at Row: ").append(i + 1).append("\n");
                                continue;
                            }
                            if (qty < 0) {
                                excelErrors.append("Stock Quantity is negative at Row: ").append(i + 1).append("\n");
                                continue;
                            }
                        } catch (Exception e) {
                            excelErrors.append("Incorrect Stock Quantity at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        Product p = new Product(productId, name, desc, price, qty);
                        try {
                            store.addProduct(p);
                        } catch (Exception e) {
                            excelErrors.append(e.getMessage()).append(" for Row: ").append(i + 1).append("\n");
                        }
                    }
                    System.out.println("Products Loaded Successfully.");
                    System.out.println(excelErrors);
                } else {
                    System.out.println("Products sheet is not available in the excel file");
                }
            } else {
                System.out.println("File does not exist at the specified path");
            }
        } catch (IOException e) {
            System.out.println("Error opening Excel file: " + e.getMessage());
        }
    }
}
