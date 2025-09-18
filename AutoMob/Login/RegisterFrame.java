package Login;

import Entities.Account;
import Frame.Frame;
import Components.RoundedButton;
import Components.RoundedTextField;
import Components.RoundedPasswordField;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class RegisterFrame extends JFrame {
    private JLabel titleLabel, nameLabel, passLabel, confirmPassLabel;
    private RoundedTextField nameField;
    private RoundedPasswordField passField, confirmPassField;
    private RoundedButton registerButton, backButton;
    private JRadioButton customerRadio, adminRadio;
    private ButtonGroup userTypeGroup;
    private Color bgColor = new Color(204, 229, 255); 
    private Color buttonColor = new Color(70, 70, 70);

    public RegisterFrame() {
        super("Account Registration");
        this.setSize(600, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(bgColor);

        titleLabel = new JLabel("CREATE NEW ACCOUNT");
        titleLabel.setBounds(170, 60, 300, 40);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        panel.add(titleLabel);

        nameLabel = new JLabel("Username:");
        nameLabel.setBounds(150, 130, 100, 25);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(nameLabel);
        
        nameField = new RoundedTextField();
        nameField.setBounds(250, 130, 200, 30);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(nameField);

        passLabel = new JLabel("Password:");
        passLabel.setBounds(150, 180, 100, 25);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(passLabel);
        
        passField = new RoundedPasswordField();
        passField.setBounds(250, 180, 200, 30);
        passField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(passField);

        confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(100, 230, 150, 25);
        confirmPassLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(confirmPassLabel);
        
        confirmPassField = new RoundedPasswordField();
        confirmPassField.setBounds(250, 230, 200, 30);
        confirmPassField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(confirmPassField);

        JLabel userTypeLabel = new JLabel("Register As:");
        userTypeLabel.setBounds(150, 280, 100, 25);
        userTypeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(userTypeLabel);

        customerRadio = new JRadioButton("Customer", true);
        customerRadio.setBounds(250, 280, 100, 25);
        customerRadio.setBackground(bgColor);
        customerRadio.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(customerRadio);

        adminRadio = new JRadioButton("Admin");
        adminRadio.setBounds(360, 280, 100, 25);
        adminRadio.setBackground(bgColor);
        adminRadio.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(adminRadio);

        userTypeGroup = new ButtonGroup();
        userTypeGroup.add(customerRadio);
        userTypeGroup.add(adminRadio);

        registerButton = new RoundedButton("Register");
        registerButton.setBounds(200, 330, 100, 35);
        registerButton.setBackground(new Color(153, 204, 255));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.addActionListener(e -> registerUser());
        panel.add(registerButton);

        backButton = new RoundedButton("Back");
        backButton.setBounds(320, 330, 100, 35);
        backButton.setBackground(new Color(153, 204, 255));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(e -> {
            this.dispose();
            new Frame().setVisible(true);
        });
        panel.add(backButton);

        this.add(panel);
    }

    private void registerUser() {
        String username = nameField.getText().trim();
        String password = new String(passField.getPassword());
        String confirmPassword = new String(confirmPassField.getPassword());
        String userType = customerRadio.isSelected() ? "customer" : "admin";

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Account newAccount = new Account(username, password, userType);
        if (newAccount.addAccount()) {
            JOptionPane.showMessageDialog(this, 
                "Registration Successful!\nUsername: " + username + 
                "\nUser Type: " + userType, "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new Frame().setVisible(true);
        }
    }
}