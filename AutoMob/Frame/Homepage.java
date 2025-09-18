package Frame;

import Entities.Account;
import Entities.PendingPurchase;
import Components.RoundedButton;
import Components.RoundedTextField;
import java.awt.*;
//import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
//import javax.swing.border.*;
import javax.swing.table.*;

public class Homepage extends JFrame {
    private JLabel welcomeLabel;
    private RoundedButton logoutButton;
    private String currentUser;
    private JFrame loginFrame;
    ImageIcon img;
    
    private static Map<String, Integer> carTypes = new HashMap<>();
    private static Map<String, Double> carPrices = new HashMap<>();
    private JLabel quantityLabel;
    private RoundedTextField quantityField;
    //private RoundedButton setQuantityButton;
    private JComboBox<String> carTypeCombo;
    private RoundedButton addCarTypeButton;
    private RoundedButton deleteCarTypeButton;
    
    private JTable carTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> customerCarCombo;
    private JSpinner buySpinner;
    private RoundedButton buyButton;
    
    // Admin panel components for pending purchases
    private JTable pendingPurchasesTable;
    private DefaultTableModel pendingPurchasesModel;
    private RoundedButton approveButton, rejectButton;
    private JScrollPane pendingPurchasesScrollPane;
    
    // Sales report components
    //private RoundedButton generateReportButton;
    private JTable salesReportTable;
    private DefaultTableModel salesReportModel;
    private RoundedButton dailyReportButton, rangeReportButton;
    private RoundedTextField dailyReportDateField;
    
    private static final String DATA_FILE = "car_data.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    private static final String PENDING_PURCHASES_FILE = "pending_purchases.txt";

    public Homepage(String username, JFrame loginFrame) {
        super("AutoMob - Homepage");
        this.currentUser = username;
        this.loginFrame = loginFrame;
        
        loadCarData();
        
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(204, 229, 255));
        
        // Header section with improved styling
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBounds(0, 0, 900, 100);
        headerPanel.setBackground(new Color(153, 204, 255));
        headerPanel.setBorder(BorderFactory.createLineBorder(new Color(153, 204, 255), 2));
        
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setBounds(50, 20, 600, 40);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(0, 51, 51)); 
        headerPanel.add(welcomeLabel);
        
        quantityLabel = new JLabel("Total Car Types: " + carTypes.size());
        quantityLabel.setBounds(50, 60, 300, 25);
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quantityLabel.setForeground(new Color(0, 51, 51));
        headerPanel.add(quantityLabel);
        
        panel.add(headerPanel);

        JLabel contentLabel = new JLabel();
        contentLabel.setBounds(50, 120, 700, 30);
        contentLabel.setFont(new Font("Arial", Font.BOLD, 20));
        contentLabel.setForeground(new Color(0, 51, 51));
        
        Account account = new Account();
        String userType = account.getUserType(username);
        
        if(userType.equals("admin")) {
            contentLabel.setText("Dashboard");
            setupAdminComponents(panel);
        } else if(userType.equals("customer")) {
            contentLabel.setText("Dashboard - Buy car");
            setupCustomerComponents(panel);
        } else {
            contentLabel.setText("Dashboard");
        }
        panel.add(contentLabel);
        
        logoutButton = new RoundedButton("Logout");
        logoutButton.setBounds(400, 550, 100, 40);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(153, 204, 255));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        logoutButton.addActionListener(e -> logout());
        logoutButton.setFocusPainted(false);
        panel.add(logoutButton);

        add(panel);
    }
    
    private void setupAdminComponents(JPanel panel) {
        // Create a tabbed pane for admin features
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(40, 170, 825, 375);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Inventory Management Tab
        JPanel inventoryPanel = new JPanel(null);
        inventoryPanel.setBackground(Color.WHITE);
        setupInventoryManagement(inventoryPanel);
        
        // Pending Purchases Tab
        JPanel pendingPanel = new JPanel(null);
        pendingPanel.setBackground(Color.WHITE);
        setupPendingPurchasesPanel(pendingPanel);
        
        // Sales Report Tab
        JPanel reportPanel = new JPanel(null);
        reportPanel.setBackground(Color.WHITE);
        setupSalesReportPanel(reportPanel);
        
        tabbedPane.addTab("Inventory Management", inventoryPanel);
        tabbedPane.addTab("Pending Purchases", pendingPanel);
        tabbedPane.addTab("Sales Reports", reportPanel);
        
        panel.add(tabbedPane);
    }
    
    private void setupInventoryManagement(JPanel panel) {
        // Car Type Selection Section
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(null);
        selectionPanel.setBounds(220, 20, 350, 80);
        selectionPanel.setBackground(new Color(248, 248, 255));
        selectionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Select Car Type",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        JLabel carTypeLabel = new JLabel("Car Type:");
        carTypeLabel.setBounds(15, 25, 100, 25);
        carTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        selectionPanel.add(carTypeLabel);
        
        carTypeCombo = new JComboBox<>();
        updateCarTypeCombo();
        carTypeCombo.setBounds(15, 50, 200, 25);
        carTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        carTypeCombo.setBackground(Color.WHITE);
        selectionPanel.add(carTypeCombo);
        
        panel.add(selectionPanel);
        
        // Quantity Management Section
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(null);
        quantityPanel.setBounds(220, 100, 350, 80);
        quantityPanel.setBackground(new Color(248, 248, 255));
        quantityPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Manage Quantity",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        JLabel setQuantityLabel = new JLabel("Quantity:");
        setQuantityLabel.setBounds(15, 25, 80, 25);
        setQuantityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityPanel.add(setQuantityLabel);
        
        quantityField = new RoundedTextField();
        quantityField.setBounds(100, 25, 80, 25);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityField.setBorder(BorderFactory.createLoweredBevelBorder());
        quantityPanel.add(quantityField);
        
        JLabel unitsLabel = new JLabel("units");
        unitsLabel.setBounds(190, 25, 50, 25);
        unitsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityPanel.add(unitsLabel);
        
        panel.add(quantityPanel);
        
        // Action Buttons Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(220, 200, 350, 120);
        buttonPanel.setBackground(new Color(248, 248, 255));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Actions",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        // First row of buttons
        RoundedButton setQuantityButton = new RoundedButton("Set Quantity");
        setQuantityButton.setBounds(20, 30, 140, 35);
        setQuantityButton.setFont(new Font("Arial", Font.BOLD, 12));
        setQuantityButton.setBackground(new Color(153, 204, 255));
        setQuantityButton.setForeground(Color.BLACK);
        setQuantityButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        setQuantityButton.setFocusPainted(false);
        setQuantityButton.addActionListener(e -> updateCarQuantity());
        buttonPanel.add(setQuantityButton);
        
        RoundedButton addQuantityButton = new RoundedButton("Add to Stock");
        addQuantityButton.setBounds(180, 30, 140, 35);
        addQuantityButton.setFont(new Font("Arial", Font.BOLD, 12));
        addQuantityButton.setBackground(new Color(153, 204, 255));
        addQuantityButton.setForeground(Color.BLACK);
        addQuantityButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        addQuantityButton.setFocusPainted(false);
        addQuantityButton.addActionListener(e -> addToCarQuantity());
        buttonPanel.add(addQuantityButton);
        
        // Second row of buttons
        addCarTypeButton = new RoundedButton("Add New Type");
        addCarTypeButton.setBounds(20, 75, 140, 35);
        addCarTypeButton.setFont(new Font("Arial", Font.BOLD, 12));
        addCarTypeButton.setBackground(new Color(153, 204, 255));
        addCarTypeButton.setForeground(Color.BLACK);
        addCarTypeButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        addCarTypeButton.setFocusPainted(false);
        addCarTypeButton.addActionListener(e -> addNewCarType());
        buttonPanel.add(addCarTypeButton);
        
        deleteCarTypeButton = new RoundedButton("Delete Type");
        deleteCarTypeButton.setBounds(180, 75, 140, 35);
        deleteCarTypeButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteCarTypeButton.setBackground(new Color(153, 204, 255));
        deleteCarTypeButton.setForeground(Color.BLACK);
        deleteCarTypeButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        deleteCarTypeButton.setFocusPainted(false);
        deleteCarTypeButton.addActionListener(e -> deleteCarType());
        buttonPanel.add(deleteCarTypeButton);
        
        panel.add(buttonPanel);
    }
    
    private void setupPendingPurchasesPanel(JPanel panel) {
        JLabel titleLabel = new JLabel("Pending Purchases");
        titleLabel.setBounds(20, 20, 200, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 51, 51));
        panel.add(titleLabel);
        
        // Create table for pending purchases
        String[] columnNames = {"Customer", "Car Type", "Quantity", "Price", "Date", "Actions"};
        pendingPurchasesModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        pendingPurchasesTable = new JTable(pendingPurchasesModel);
        pendingPurchasesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        pendingPurchasesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        pendingPurchasesTable.setRowHeight(25);
        
        pendingPurchasesScrollPane = new JScrollPane(pendingPurchasesTable);
        pendingPurchasesScrollPane.setBounds(20, 60, 760, 200);
        panel.add(pendingPurchasesScrollPane);
        
        // Action buttons
        approveButton = new RoundedButton("Approve");
        approveButton.setBounds(290, 270, 100, 35);
        approveButton.setFont(new Font("Arial", Font.BOLD, 12));
        approveButton.setBackground(new Color(153, 204, 255));
        approveButton.setForeground(Color.BLACK);
        approveButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        approveButton.setFocusPainted(false);
        approveButton.addActionListener(e -> approveSelectedPurchase());
        panel.add(approveButton);

        rejectButton = new RoundedButton("Reject");
        rejectButton.setBounds(410, 270, 100, 35);
        rejectButton.setFont(new Font("Arial", Font.BOLD, 12));
        rejectButton.setBackground(new Color(153, 204, 255));
        rejectButton.setForeground(Color.BLACK);
        rejectButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        rejectButton.setFocusPainted(false);
        rejectButton.addActionListener(e -> rejectSelectedPurchase());
        panel.add(rejectButton);
        
        // Load pending purchases
        loadPendingPurchases();
    }
    
    private void setupSalesReportPanel(JPanel panel) {
        JLabel titleLabel = new JLabel("Sales Reports");
        titleLabel.setBounds(20, 20, 200, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 51, 51));
        panel.add(titleLabel);
        
        // Daily report section
        JPanel dailyPanel = new JPanel(null);
        dailyPanel.setBounds(20, 60, 350, 100);
        dailyPanel.setBackground(new Color(248, 248, 255));
        dailyPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Daily Sales Report",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setBounds(15, 25, 100, 25);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dailyPanel.add(dateLabel);
        
        dailyReportDateField = new RoundedTextField();
        dailyReportDateField.setBounds(120, 25, 100, 25);
        dailyReportDateField.setFont(new Font("Arial", Font.PLAIN, 14));
        // Set default to today
        dailyReportDateField.setText(LocalDate.now().toString());
        dailyPanel.add(dailyReportDateField);
        
        dailyReportButton = new RoundedButton("Generate");
        dailyReportButton.setBounds(230, 25, 100, 25);
        dailyReportButton.setFont(new Font("Arial", Font.BOLD, 12));
        dailyReportButton.setBackground(new Color(153, 204, 255));
        dailyReportButton.setForeground(Color.BLACK);
        dailyReportButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        dailyReportButton.setFocusPainted(false);
        dailyReportButton.addActionListener(e -> generateDailyReport());
        dailyPanel.add(dailyReportButton);
        
        panel.add(dailyPanel);
        
        // Date range report section
        JPanel rangePanel = new JPanel(null);
        rangePanel.setBounds(20, 170, 350, 100);
        rangePanel.setBackground(new Color(248, 248, 255));
        rangePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Date Range Sales Report",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        JLabel startDateLabel = new JLabel("From:");
        startDateLabel.setBounds(15, 25, 50, 25);
        startDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangePanel.add(startDateLabel);
        
        RoundedTextField startDateField = new RoundedTextField();
        startDateField.setBounds(70, 25, 100, 25);
        startDateField.setFont(new Font("Arial", Font.PLAIN, 14));
        // Set default to 7 days ago
        startDateField.setText(LocalDate.now().minusDays(7).toString());
        rangePanel.add(startDateField);
        
        JLabel endDateLabel = new JLabel("To:");
        endDateLabel.setBounds(180, 25, 30, 25);
        endDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangePanel.add(endDateLabel);
        
        RoundedTextField endDateField = new RoundedTextField();
        endDateField.setBounds(215, 25, 100, 25);
        endDateField.setFont(new Font("Arial", Font.PLAIN, 14));
        // Set default to today
        endDateField.setText(LocalDate.now().toString());
        rangePanel.add(endDateField);
        
        rangeReportButton = new RoundedButton("Generate");
        rangeReportButton.setBounds(15, 60, 300, 25);
        rangeReportButton.setFont(new Font("Arial", Font.BOLD, 12));
        rangeReportButton.setBackground(new Color(153, 204, 255));
        rangeReportButton.setForeground(Color.BLACK);
        rangeReportButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        rangeReportButton.setFocusPainted(false);
        rangeReportButton.addActionListener(e -> generateRangeReport(startDateField.getText(), endDateField.getText()));
        rangePanel.add(rangeReportButton);
        
        panel.add(rangePanel);
        
        // Sales report table
        String[] columnNames = {"Date", "Customer", "Car Type", "Quantity", "Total Price"};
        salesReportModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        salesReportTable = new JTable(salesReportModel);
        salesReportTable.setFont(new Font("Arial", Font.PLAIN, 12));
        salesReportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        salesReportTable.setRowHeight(25);
        
        JScrollPane reportScrollPane = new JScrollPane(salesReportTable);
        reportScrollPane.setBounds(400, 60, 380, 210);
        panel.add(reportScrollPane);
    }
    
    private void setupCustomerComponents(JPanel panel) {
        JLabel customerTitle = new JLabel("Available Cars");
        customerTitle.setBounds(50, 170, 300, 25);
        customerTitle.setFont(new Font("Arial", Font.BOLD, 16));
        customerTitle.setForeground(new Color(0, 51, 51));
        panel.add(customerTitle);
        
        createCustomerTable(panel);
        
        JLabel purchaseTitle = new JLabel("Make a Purchase");
        purchaseTitle.setBounds(50, 420, 200, 25);
        purchaseTitle.setFont(new Font("Arial", Font.BOLD, 16));
        purchaseTitle.setForeground(new Color(0, 51, 51));
        panel.add(purchaseTitle);
        
        JLabel selectCarLabel = new JLabel("Select Car Type:");
        selectCarLabel.setBounds(50, 460, 150, 25);
        selectCarLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(selectCarLabel);
        
        customerCarCombo = new JComboBox<>();
        updateCustomerCarCombo();
        customerCarCombo.setBounds(160, 460, 200, 25);
        customerCarCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        customerCarCombo.addActionListener(e -> updateBuySpinner());
        panel.add(customerCarCombo);
        
        JLabel buyLabel = new JLabel("Quantity:");
        buyLabel.setBounds(390, 460, 80, 25);
        buyLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(buyLabel);
        
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, getMaxQuantityForSelectedCar(), 1);
        buySpinner = new JSpinner(spinnerModel);
        buySpinner.setBounds(450, 460, 80, 25);
        buySpinner.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(buySpinner);
        
        JLabel unitsLabel = new JLabel("units");
        unitsLabel.setBounds(540, 460, 50, 25);
        unitsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(unitsLabel);
        
        buyButton = new RoundedButton("Buy Car");
        buyButton.setBounds(600, 455, 120, 35);
        buyButton.setFont(new Font("Arial", Font.BOLD, 12));
        buyButton.setBackground(new Color(153, 204, 255)); 
        buyButton.setForeground(Color.BLACK);
        buyButton.setFocusPainted(false);
        buyButton.addActionListener(e -> buyCar());
        panel.add(buyButton);
    }
    
    private void createCustomerTable(JPanel panel) {
        String[] columnNames = {"Car Type", "Available Quantity", "Price per Unit", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        carTable = new JTable(tableModel);
        carTable.setFont(new Font("Arial", Font.PLAIN, 12));
        carTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        carTable.setRowHeight(25);
        
        updateCustomerTable();
        
        JScrollPane scrollPane = new JScrollPane(carTable);
        scrollPane.setBounds(50, 200, 600, 200);
        panel.add(scrollPane);
    }
    
    private void updateCustomerTable() {
        tableModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : carTypes.entrySet()) {
            String carType = entry.getKey();
            int quantity = entry.getValue();
            double price = carPrices.getOrDefault(carType, 5.00);
            String status = quantity > 0 ? "Available" : "Out of Stock";
            
            tableModel.addRow(new Object[]{
                carType, 
                quantity + " units", 
                "$" + String.format("%.2f", price),
                status
            });
        }
    }
    
    private void updateCarTypeCombo() {
        carTypeCombo.removeAllItems();
        for (String carType : carTypes.keySet()) {
            carTypeCombo.addItem(carType);
        }
    }
    
    private void updateCustomerCarCombo() {
        customerCarCombo.removeAllItems();
        for (Map.Entry<String, Integer> entry : carTypes.entrySet()) {
            if (entry.getValue() > 0) { // Only show available items
                customerCarCombo.addItem(entry.getKey());
            }
        }
    }
    
    private void updateBuySpinner() {
        String selectedCar = (String) customerCarCombo.getSelectedItem();
        if (selectedCar != null) {
            int maxQuantity = carTypes.getOrDefault(selectedCar, 0);
            SpinnerNumberModel model = (SpinnerNumberModel) buySpinner.getModel();
            model.setMaximum(Math.max(1, maxQuantity));
            buyButton.setEnabled(maxQuantity > 0);
        }
    }
    
    private int getMaxQuantityForSelectedCar() {
        String selectedCar = (String) customerCarCombo.getSelectedItem();
        if (selectedCar != null) {
            return Math.max(1, carTypes.getOrDefault(selectedCar, 0));
        }
        return 1;
    }
    
    private void addNewCarType() {
        String newType = JOptionPane.showInputDialog(this, "Enter new Car type name:", "Add Car Type", JOptionPane.QUESTION_MESSAGE);
        if (newType != null && !newType.trim().isEmpty()) {
            newType = newType.trim();
            if (carTypes.containsKey(newType)) {
                JOptionPane.showMessageDialog(this, "This Car type already exists!", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String priceStr = JOptionPane.showInputDialog(this, "Enter price per unit for " + newType + ":", "Set Price", JOptionPane.QUESTION_MESSAGE);
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                try {
                    double price = Double.parseDouble(priceStr.trim());
                    if (price < 0) {
                        JOptionPane.showMessageDialog(this, "Price cannot be negative!", "Invalid Price", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    carTypes.put(newType, 0);
                    carPrices.put(newType, price);
                    updateCarTypeCombo();
                    quantityLabel.setText("Total Car Types: " + carTypes.size());
                    saveCarData();
                    
                    String transaction = getCurrentTimestamp() + " - ADMIN (" + currentUser + "): Added new Car type '" + newType + "' with price $" + String.format("%.2f", price);
                    saveTransaction(transaction);
                    
                    JOptionPane.showMessageDialog(this, "New Car type '" + newType + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid price!", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void deleteCarType() {
        String selectedType = (String) carTypeCombo.getSelectedItem();
        if (selectedType == null) {
            JOptionPane.showMessageDialog(this, "Please select a Car type to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete '" + selectedType + "'?\nThis action cannot be undone.", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            carTypes.remove(selectedType);
            carPrices.remove(selectedType);
            updateCarTypeCombo();
            quantityLabel.setText("Total Car Types: " + carTypes.size());
            saveCarData();
            
            String transaction = getCurrentTimestamp() + " - ADMIN (" + currentUser + "): Deleted Car type '" + selectedType + "'";
            saveTransaction(transaction);
            
            JOptionPane.showMessageDialog(this, "Car type '" + selectedType + "' deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateCarQuantity() {
        String selectedType = (String) carTypeCombo.getSelectedItem();
        if (selectedType == null) {
            JOptionPane.showMessageDialog(this, "Please select a Car type.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String input = quantityField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a quantity.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int newQuantity = Integer.parseInt(input);
            if (newQuantity < 0) {
                JOptionPane.showMessageDialog(this, "Quantity cannot be negative.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            carTypes.put(selectedType, newQuantity);
            quantityField.setText("");
            
            saveCarData();
            String transaction = getCurrentTimestamp() + " - ADMIN (" + currentUser + "): Set " + selectedType + " quantity to " + newQuantity + " units";
            saveTransaction(transaction);
            
            JOptionPane.showMessageDialog(this, 
                selectedType + " quantity updated successfully!\nNew quantity: " + newQuantity + " units", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addToCarQuantity() {
        String selectedType = (String) carTypeCombo.getSelectedItem();
        if (selectedType == null) {
            JOptionPane.showMessageDialog(this, "Please select a Car type.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String input = quantityField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a quantity to add.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int addQuantity = Integer.parseInt(input);
            if (addQuantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity to add must be positive.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int currentQuantity = carTypes.get(selectedType);
            int newQuantity = currentQuantity + addQuantity;
            carTypes.put(selectedType, newQuantity);
            quantityField.setText("");
            
            saveCarData();
            String transaction = getCurrentTimestamp() + " - ADMIN (" + currentUser + "): Added " + addQuantity + " units to " + selectedType + " (Total: " + newQuantity + ")";
            saveTransaction(transaction);
            
            JOptionPane.showMessageDialog(this, 
                addQuantity + " units added to " + selectedType + "!\nTotal quantity: " + newQuantity + " units", 
                "Stock Added", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buyCar() {
        String selectedCar = (String) customerCarCombo.getSelectedItem();
        if (selectedCar == null) {
            JOptionPane.showMessageDialog(this, "Please select a Car type.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int availableQuantity = carTypes.getOrDefault(selectedCar, 0);
        if (availableQuantity <= 0) {
            JOptionPane.showMessageDialog(this, "Sorry, " + selectedCar + " is out of stock!", "Out of Stock", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int quantityToBuy = (Integer) buySpinner.getValue();
        
        if (quantityToBuy > availableQuantity) {
            JOptionPane.showMessageDialog(this, 
                "Insufficient stock! Available: " + availableQuantity + " units", 
                "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double pricePerUnit = carPrices.getOrDefault(selectedCar, 5.00);
        double totalPrice = quantityToBuy * pricePerUnit;
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Purchase Summary:\n" +
            "Product: " + selectedCar + "\n" +
            "Quantity: " + quantityToBuy + " units\n" +
            "Price per unit: $" + String.format("%.2f", pricePerUnit) + "\n" +
            "Total Price: $" + String.format("%.2f", totalPrice) + "\n\n" +
            "Your purchase request will be sent to admin for approval.\n" +
            "Confirm purchase request?", 
            "Confirm Purchase Request", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            // Create a pending purchase
            PendingPurchase purchase = new PendingPurchase(
                currentUser, 
                selectedCar, 
                quantityToBuy, 
                totalPrice, 
                LocalDateTime.now()
            );
            
            // Save the pending purchase
            savePendingPurchase(purchase);
            
            JOptionPane.showMessageDialog(this, 
                "Your purchase request has been submitted and is pending admin approval.\n" +
                "You will be notified once approved.", 
                "Purchase Pending", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void savePendingPurchase(PendingPurchase purchase) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PENDING_PURCHASES_FILE, true))) {
            writer.println(purchase.toFileString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving purchase request: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadPendingPurchases() {
        pendingPurchasesModel.setRowCount(0);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(PENDING_PURCHASES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PendingPurchase purchase = PendingPurchase.fromFileString(line);
                if (purchase != null) {
                    pendingPurchasesModel.addRow(new Object[]{
                        purchase.getCustomer(),
                        purchase.getCarType(),
                        purchase.getQuantity(),
                        "$" + String.format("%.2f", purchase.getTotalPrice()),
                        purchase.getFormattedTimestamp(),
                        "Actions"
                    });
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, no pending purchases
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading pending purchases: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void approveSelectedPurchase() {
        int selectedRow = pendingPurchasesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a purchase to approve.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the details of the selected purchase
        String customer = (String) pendingPurchasesModel.getValueAt(selectedRow, 0);
        String carType = (String) pendingPurchasesModel.getValueAt(selectedRow, 1);
        int quantity = Integer.parseInt(pendingPurchasesModel.getValueAt(selectedRow, 2).toString());
        double totalPrice = Double.parseDouble(pendingPurchasesModel.getValueAt(selectedRow, 3).toString().replace("$", ""));
        
        // Check if there's enough stock
        int availableQuantity = carTypes.getOrDefault(carType, 0);
        if (quantity > availableQuantity) {
            JOptionPane.showMessageDialog(this, 
                "Insufficient stock for " + carType + "! Available: " + availableQuantity + " units", 
                "Insufficient Stock", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update car inventory
        int newQuantity = availableQuantity - quantity;
        carTypes.put(carType, newQuantity);
        saveCarData(); // Save the updated inventory
        
        // Record the transaction in a consistent format
        String transaction = getCurrentTimestamp() + " - CUSTOMER (" + customer + "): Purchased " + 
                            quantity + " units of " + carType + " for $" + String.format("%.2f", totalPrice) + 
                            " (Approved)";
        saveTransaction(transaction);
        
        // Remove the pending purchase from the file and the table
        removePendingPurchase(selectedRow);
        
        // Refresh the pending purchases table
        loadPendingPurchases();
        
        // Show success message
        JOptionPane.showMessageDialog(this, 
            "Purchase approved successfully!\n" +
            "Customer: " + customer + "\n" +
            "Car Type: " + carType + "\n" +
            "Quantity: " + quantity + " units\n" +
            "Total Price: $" + String.format("%.2f", totalPrice), 
            "Purchase Approved", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void rejectSelectedPurchase() {
        int selectedRow = pendingPurchasesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a purchase to reject.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String customer = (String) pendingPurchasesModel.getValueAt(selectedRow, 0);
        String carType = (String) pendingPurchasesModel.getValueAt(selectedRow, 1);
        int quantity = Integer.parseInt(pendingPurchasesModel.getValueAt(selectedRow, 2).toString());
        double totalPrice = Double.parseDouble(pendingPurchasesModel.getValueAt(selectedRow, 3).toString().replace("$", ""));
        
        // Record the rejection in transactions
        String transaction = getCurrentTimestamp() + " - ADMIN (" + currentUser + "): Rejected purchase request from " + 
                            customer + " for " + quantity + " units of " + carType + " ($" + String.format("%.2f", totalPrice) + ")";
        saveTransaction(transaction);
        
        // Remove the pending purchase
        removePendingPurchase(selectedRow);
        
        // Refresh table
        loadPendingPurchases();
        
        JOptionPane.showMessageDialog(this, 
            "Purchase rejected successfully!\n" +
            "Customer: " + customer + "\n" +
            "Car Type: " + carType + "\n" +
            "Quantity: " + quantity + " units\n" +
            "Total Price: $" + String.format("%.2f", totalPrice), 
            "Purchase Rejected", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void removePendingPurchase(int rowIndex) {
        List<String> pendingLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PENDING_PURCHASES_FILE))) {
            String line;
            int currentRow = 0;
            while ((line = reader.readLine()) != null) {
                if (currentRow != rowIndex) {
                    pendingLines.add(line);
                }
                currentRow++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error removing pending purchase: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Rewrite the pending file without the removed purchase
        try (PrintWriter writer = new PrintWriter(new FileWriter(PENDING_PURCHASES_FILE))) {
            for (String pendingLine : pendingLines) {
                writer.println(pendingLine);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating pending purchases: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateDailyReport() {
        String dateStr = dailyReportDateField.getText().trim();
        if (dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a date.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            LocalDate date = LocalDate.parse(dateStr);
            generateSalesReport(date, date);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD", "Date Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateRangeReport(String startDateStr, String endDateStr) {
        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both start and end dates.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            
            if (startDate.isAfter(endDate)) {
                JOptionPane.showMessageDialog(this, "Start date cannot be after end date.", "Date Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            generateSalesReport(startDate, endDate);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD", "Date Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateSalesReport(LocalDate startDate, LocalDate endDate) {
        salesReportModel.setRowCount(0);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            double totalSales = 0;
            int totalTransactions = 0;
            
            while ((line = reader.readLine()) != null) {
                try {
                    // Check if this is an approved purchase transaction
                    if (!line.contains("Purchased") || !line.contains("Approved")) {
                        continue; // Skip non-purchase transactions
                    }
                    
                    // Extract date part (first 10 characters)
                    String datePart = line.substring(0, 10);
                    LocalDate transactionDate = LocalDate.parse(datePart);
                    
                    // Check if the transaction date is within the range
                    if ((transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) && 
                        (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate))) {
                        
                        // Extract customer name - look for pattern "CUSTOMER (name):"
                        int customerStart = line.indexOf("CUSTOMER (") + 10;
                        if (customerStart < 10) continue; // Not found
                        int customerEnd = line.indexOf(")", customerStart);
                        if (customerEnd <= customerStart) continue;
                        String customer = line.substring(customerStart, customerEnd);
                        
                        // Extract car type - look for pattern "of [Car Type] for"
                        int carTypeStart = line.indexOf("of ") + 3;
                        if (carTypeStart < 3) continue;
                        int carTypeEnd = line.indexOf(" for", carTypeStart);
                        if (carTypeEnd <= carTypeStart) continue;
                        String carType = line.substring(carTypeStart, carTypeEnd);
                        
                        // Extract quantity - look for pattern "Purchased [quantity] units"
                        int quantityStart = line.indexOf("Purchased ") + 10;
                        if (quantityStart < 10) continue;
                        int quantityEnd = line.indexOf(" units", quantityStart);
                        if (quantityEnd <= quantityStart) continue;
                        int quantity = Integer.parseInt(line.substring(quantityStart, quantityEnd).trim());
                        
                        // Extract total price - look for pattern "for $[price] ("
                        int priceStart = line.indexOf("for $") + 5;
                        if (priceStart < 5) continue;
                        int priceEnd = line.indexOf(" (", priceStart);
                        if (priceEnd <= priceStart) continue;
                        double totalPrice = Double.parseDouble(line.substring(priceStart, priceEnd));
                        
                        // Add row to table
                        salesReportModel.addRow(new Object[]{
                            datePart,
                            customer,
                            carType,
                            quantity,
                            "$" + String.format("%.2f", totalPrice)
                        });
                        
                        totalSales += totalPrice;
                        totalTransactions++;
                    }
                } catch (Exception e) {
                    // Skip malformed lines but print error for debugging
                    System.err.println("Error parsing transaction: " + line);
                    e.printStackTrace();
                }
            }
            
            // Show summary
            JOptionPane.showMessageDialog(this,
                "Sales Report Summary\n" +
                "Period: " + startDate + " to " + endDate + "\n" +
                "Total Transactions: " + totalTransactions + "\n" +
                "Total Sales: $" + String.format("%.2f", totalSales),
                "Sales Report",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No transactions found", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading transactions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logout() {
        this.dispose(); 
        loginFrame.setVisible(true); 
        
        if (loginFrame instanceof Frame) {
            Frame frame = (Frame) loginFrame;
            frame.textField.setText("");
            frame.passwordField.setText("");
            frame.textField.requestFocus();
        }
    }
    
    private void saveCarData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, Integer> entry : carTypes.entrySet()) {
                String carType = entry.getKey();
                int quantity = entry.getValue();
                double price = carPrices.getOrDefault(carType, 5.00);
                writer.println(carType + "," + quantity + "," + price);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadCarData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String carType = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    double price = Double.parseDouble(parts[2]);
                    carTypes.put(carType, quantity);
                    carPrices.put(carType, price);
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, will be created on first save
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveTransaction(String transaction) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.println(transaction);
            // Debug output
            System.out.println("Recording transaction: " + transaction);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving transaction: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}