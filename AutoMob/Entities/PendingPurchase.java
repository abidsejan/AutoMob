package Entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PendingPurchase {
    private String customer;
    private String carType;
    private int quantity;
    private double totalPrice;
    private LocalDateTime timestamp;
    
    public PendingPurchase(String customer, String carType, int quantity, double totalPrice, LocalDateTime timestamp) {
        this.customer = customer;
        this.carType = carType;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }
    
    // Getters
    public String getCustomer() { return customer; }
    public String getCarType() { return carType; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    // Format timestamp for display
    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    // Format for file storage
    public String toFileString() {
        return customer + "," + carType + "," + quantity + "," + totalPrice + "," + getFormattedTimestamp();
    }
    
    // Create from file string
    public static PendingPurchase fromFileString(String fileString) {
        String[] parts = fileString.split(",");
        if (parts.length >= 5) {
            String customer = parts[0];
            String carType = parts[1];
            int quantity = Integer.parseInt(parts[2]);
            double totalPrice = Double.parseDouble(parts[3]);
            LocalDateTime timestamp = LocalDateTime.parse(parts[4], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return new PendingPurchase(customer, carType, quantity, totalPrice, timestamp);
        }
        return null;
    }
}