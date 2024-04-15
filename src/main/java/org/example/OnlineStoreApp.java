package org.example;

import lombok.Data;
import org.example.entity.CartItem;
import org.example.entity.Customer;
import org.example.entity.Manager;
import org.example.entity.Order;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.utils.InputUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class OnlineStoreApp {
    private List<Product> products;
    private Set<Customer> customerSet;
    private Map<String, Set<CartItem>> shoppingCart;
    private Map<String, Set<Order>> orderHistory;

    public OnlineStoreApp() {
        products = new ArrayList<>();
        customerSet = new HashSet<>();
        shoppingCart = new TreeMap<>();
        orderHistory = new TreeMap<>();
    }

    public Product getProduct(String productId) {
        for (Product p : products) {
            if (p.getProductId().equals(productId)) {
                return p;
            }
        }
        RuntimeException e = new RuntimeException("Product not found");
        throw e;
    }

    // Method to add a new product to the store
    public String addProduct(Product product) throws Exception {
        Set<String> productIdSet = products.stream().map(Product::getProductId).collect(Collectors.toSet());
        if (!productIdSet.contains(product.getProductId())) {
            products.add(product);
            return "Product Added Successfully";
        } else {
            Exception e = new RuntimeException("Product with same product id already exists");
            throw e;
        }
    }

    public void removeProduct(String productId) throws Exception {
        Set<String> allProductIds = products.stream().map(Product::getProductId)
                .collect(Collectors.toSet());
        if (allProductIds.contains(productId)) {
            Iterator<Product> it = products.iterator();
            while (it.hasNext()) {
                Product storeProduct = it.next();
                if (storeProduct.getProductId().equals(productId)) {
                    it.remove();
                    break;
                }
            }
        } else {
            Exception e = new RuntimeException("Product does not exists");
            throw e;
        }
    }

    public void updateProduct(Product product) {
        Iterator<Product> it = products.iterator();
        boolean productFound = false;
        while (it.hasNext()) {
            Product storeProduct = it.next();
            if (storeProduct.getProductId().equals(product.getProductId())) {
                it.remove();
                productFound = true;
                break;
            }
        }
        if (productFound) {
            products.add(product);
        } else {
            RuntimeException e = new RuntimeException("Product not available in store");
            throw e;
        }
    }

    // Method to add a new customer to the system
    public void addCustomer(Customer customer) {
        Set<String> allEmails = customerSet.stream().map(Customer::getEmail).collect(Collectors.toSet());
        if (allEmails.contains(customer.getEmail())) {
            throw new RuntimeException("Email Already In Use. Please register with another email.");
        }
        customerSet.add(customer);
    }

    public void removeCustomer(String customerId) throws Exception {
        Set<String> allCustomerIds = customerSet.stream().map(Customer::getId).collect(Collectors.toSet());
        if (allCustomerIds.contains(customerId)) {
            Iterator<Customer> it = customerSet.iterator();
            while (it.hasNext()) {
                Customer storeCustomer = it.next();
                if (storeCustomer.getId().equals(customerId)) {
                    it.remove();
                    break;
                }
            }
        } else {
            Exception e = new RuntimeException("Customer Id does not exist");
            throw e;
        }
    }

    // Method to create a new order for a customer
    public void placeOrder(String orderId, String customerId) {
        Set<CartItem> cartItems = shoppingCart.get(customerId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is Empty");
        }
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));
        for (CartItem item : cartItems) {
            if (productMap.get(item.getProductId()).getStockQuantity() < item.getQuantity()) {
                RuntimeException e = new RuntimeException("Failed to place Order, Stock not sufficient.");
                throw e;
            }
        }
        for (CartItem item : cartItems) {
            Product p = getProduct(item.getProductId());
            updateProduct(new Product(item.getProductId(), p.getName(),
                    p.getDescription(), p.getPrice(), p.getStockQuantity() - item.getQuantity()));
        }
        Order order = new Order(orderId, customerId, cartItems);
        if (orderHistory.containsKey(customerId)) {
            Set<Order> customerOrders = orderHistory.get(customerId);
            customerOrders.add(order);
            orderHistory.put(customerId, customerOrders);
        } else {
            Set<Order> s = new HashSet<>();
            s.add(order);
            orderHistory.put(customerId, s);
        }
        shoppingCart.remove(customerId);
    }

    // Method to add items to the shopping cart
    public void addToCart(String productId, int quantity, String customerId) {
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));
        if (!productMap.containsKey(productId)) {
            throw new RuntimeException("Product Id is not available in store");
        }
        Product p = productMap.get(productId);
        if (p.getStockQuantity() < quantity) {
            throw new RuntimeException("Quantity available is " + p.getStockQuantity());
        }
        if (shoppingCart.containsKey(customerId)) {
            Set<CartItem> currentCartItems = shoppingCart.get(customerId);
            Iterator<CartItem> it = currentCartItems.iterator();
            while (it.hasNext()) {
                CartItem item = it.next();
                if (item.getProductId().equals(productId)) {
                    quantity = quantity + item.getQuantity();
                    it.remove();
                    break;
                }
            }
            CartItem c = new CartItem(productId, quantity);
            currentCartItems.add(c);
            shoppingCart.put(customerId, currentCartItems);
        } else {
            CartItem cartItem = new CartItem(productId, quantity);
            Set<CartItem> cartItemSet = new TreeSet<>();
            cartItemSet.add(cartItem);
            shoppingCart.put(customerId, cartItemSet);
        }
    }

    // Method to display a list of all products in the store
    public void displayProducts() {
        System.out.println("Product List:");
        for (Product product : products) {
            System.out.println("Product Id: " + product.getProductId() + " " + product.getName() + " "
                    + product.getDescription() + " - Price: ₹" + product.getPrice() + " - Stock Quantity: "
                    + product.getStockQuantity());
        }
    }

    // Method to display a list of all customers in the system
    public void displayCustomers() {
        System.out.println("Customer List:");
        for (Customer customer : customerSet) {
            System.out.println("CustomerId: " + customer.getId() + " "
                    + customer.getName() + " - Email: " + customer.getEmail());
        }
    }

    // Method to display a list of all orders placed by a customer
    public void displayOrders(String customerId) {
        System.out.println("Order History:");
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));
        Set<Order> customerOrders = orderHistory.get(customerId);
        if (customerOrders == null || customerOrders.isEmpty()) {
            System.out.println("No Orders Placed");
            return;
        }
        for (Order order : customerOrders) {
            System.out.println("Order ID: " + order.getOrderId()
                    + " Total Cost: ₹" + order.calculateTotalCost(productMap));
        }
    }

    // Method to display a list of all orders placed in store
    public void displayOrders() {
        System.out.println("All Order History:");
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));
        for (Set<Order> orders : orderHistory.values()) {
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId()
                        + " Total Cost: ₹" + order.calculateTotalCost(productMap));
            }
        }
    }

    // Get Next CustomerId for Customer Sgn Up
    public String getNextCustomerId() {
        List<Integer> allCustomerId = new ArrayList<>(customerSet.stream()
                .map(Customer::getId)
                .map(id -> id.split("#")[1])
                .map(Integer::parseInt)
                .toList());
        allCustomerId.sort(null);
        int lastId = allCustomerId.isEmpty() ? 0 : allCustomerId.get(allCustomerId.size() - 1);
        String id = String.format("%0" + 5 + "d", lastId + 1);
        return "C#" + id;
    }

    public Customer getCustomerByEmailId(String email) {
        for (Customer c : customerSet) {
            if (c.getEmail().equals(email)) {
                return c;
            }
        }
        throw new RuntimeException("Email Id is not Registered");
    }

    public String getNextOrderId() {
        Set<Order> allOrders = new HashSet<>();
        for (Set<Order> orderSet : orderHistory.values()) {
            allOrders.addAll(orderSet);
        }
        List<Integer> allOrderIds = new ArrayList<>(allOrders.stream()
                .map(Order::getOrderId)
                .map(id -> id.split("#")[1])
                .map(Integer::parseInt)
                .toList());
        allOrderIds.sort(null);
        int lastId = allOrderIds.isEmpty() ? 0 : allOrderIds.get(allOrderIds.size() - 1);
        String id = String.format("%0" + 3 + "d", lastId + 1);
        return "O#" + id;
    }

    public void viewCart(String customerId) {
        Set<CartItem> cartItems = shoppingCart.get(customerId);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));
        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("Cart is Empty");
            return;
        }
        for (CartItem item : cartItems) {
            System.out.println(productMap.get(item.getProductId()).getName() + " Quantity:  " + item.getQuantity());
        }
    }

    public void clearCart(String customerId) {
        shoppingCart.remove(customerId);
    }

    public User landingPage() {
        System.out.println("Welcome ----------------------------");
        System.out.println("Are you a customer or a manager ? ----------------------");
        Scanner sc = new Scanner(System.in);
        String ans = sc.nextLine();
        while (true) {
            if ("Manager".equalsIgnoreCase(ans)) {
                while (true) {
                    System.out.println("Please enter your store credentials:");
                    String email = InputUtils.inputStringWithRegexCheck("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", "Please Enter Email:", "Email Id is Incorrect");
                    String password = InputUtils.inputString("Please Enter Your Password", "Please Enter a Valid Password");
                    User user = Manager.login(email, password);
                    if (user != null) {
                        return user;
                    } else {
                        System.out.println("Invalid Credentials were Received, Login Failed");
                    }
                }
            } else if ("Customer".equalsIgnoreCase(ans)) {
                while (true) {
                    System.out.println("Please enter your store credentials:");
                    String email = InputUtils.inputStringWithRegexCheck("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", "Please Enter Email:", "Email Id is Incorrect");
                    String password = InputUtils.inputString("Please Enter Your Password", "Please Enter a Valid Password");
                    User user = Customer.login(email, password, customerSet);
                    if (user != null) {
                        return user;
                    } else {
                        System.out.println("Invalid Credentials were Received, Login Failed");
                    }
                }
            } else {
                System.out.println("Enter either manager or customer");
                return null;
            }
        }
    }
}