package Frame;

import Entities.Account;
import Login.RegisterFrame;
import Components.RoundedButton;
import Components.RoundedTextField;
import Components.RoundedPasswordField;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame implements ActionListener {
    JLabel nameLabel, passLabel, imgLabel, loginAsLabel;
    RoundedButton login, signUp, exitButton;
    JRadioButton customerRadio, adminRadio;
    ButtonGroup loginGroup;
    RoundedTextField textField;
    RoundedPasswordField passwordField;
    JPanel panel, formPanel;
    Font font;

    public Frame() {
        super("AutoMob - Login");
        this.setSize(900, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        font = new Font("Segoe UI", Font.BOLD, 16);

        panel = new JPanel(null);
        panel.setBackground(new Color(204, 229, 255));

        // Background Bee Image
        ImageIcon car = new ImageIcon("Pics/car.jpg");
        Image scaledImage = car.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
        imgLabel = new JLabel(new ImageIcon(scaledImage));
        imgLabel.setBounds(0, 0, 450, 500);
        panel.add(imgLabel);

        // Form Panel
        formPanel = new JPanel(null);
        formPanel.setBounds(450, 0, 450, 500);
        formPanel.setBackground(new Color(128, 131, 255));
        
        ImageIcon icon = new ImageIcon("Pics/automob.png");
        Image scaledAutomobImage = icon.getImage().getScaledInstance(170, 50, Image.SCALE_SMOOTH);
        imgLabel = new JLabel(new ImageIcon(scaledAutomobImage));
        imgLabel.setBounds(250, 30, 170, 50);
        formPanel.add(imgLabel);

        nameLabel = new JLabel("Username:");
        nameLabel.setFont(font);
        nameLabel.setBounds(60, 150, 100, 25);
        nameLabel.setForeground(Color.BLACK);
        formPanel.add(nameLabel);

        textField = new RoundedTextField();
        textField.setFont(font);
        textField.setBounds(180, 150, 200, 30);
        textField.setBackground(new Color(255, 255, 255));
        textField.setForeground(Color.BLACK);
        formPanel.add(textField);

        passLabel = new JLabel("Password:");
        passLabel.setFont(font);
        passLabel.setBounds(60, 200, 100, 25);
        passLabel.setForeground(Color.BLACK);
        formPanel.add(passLabel);

        passwordField = new RoundedPasswordField();
        passwordField.setFont(font);
        passwordField.setBounds(180, 200, 200, 30);
        passwordField.setBackground(new Color(255, 255, 255));
        passwordField.setForeground(Color.BLACK);
        formPanel.add(passwordField);

        loginAsLabel = new JLabel("Login As:");
        loginAsLabel.setFont(font);
        loginAsLabel.setBounds(60, 250, 100, 25);
        loginAsLabel.setForeground(Color.BLACK);
        formPanel.add(loginAsLabel);

        customerRadio = new JRadioButton("Customer", true);
        customerRadio.setFont(font);
        customerRadio.setBounds(180, 250, 100, 25);
        customerRadio.setBackground(formPanel.getBackground());
        customerRadio.setForeground(Color.BLACK);
        customerRadio.setFocusPainted(false);
        formPanel.add(customerRadio);

        adminRadio = new JRadioButton("Admin");
        adminRadio.setFont(font);
        adminRadio.setBounds(280, 250, 100, 25);
        adminRadio.setBackground(formPanel.getBackground());
        adminRadio.setForeground(Color.BLACK);
        adminRadio.setFocusPainted(false);
        formPanel.add(adminRadio);

        loginGroup = new ButtonGroup();
        loginGroup.add(customerRadio);
        loginGroup.add(adminRadio);

        login = new RoundedButton("Login");
        login.setFont(font);
        login.setBounds(60, 300, 100, 35);
        login.setBackground(new Color(153, 153, 255));  // Light blue
        login.setForeground(Color.BLACK);
        login.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        login.addActionListener(this);
        formPanel.add(login);

        signUp = new RoundedButton("Sign Up");
        signUp.setFont(font);
        signUp.setBounds(180, 300, 100, 35);
        signUp.setBackground(new Color(153, 153, 255)); // Light blue
        signUp.setForeground(Color.BLACK);
        signUp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        signUp.addActionListener(this);
        formPanel.add(signUp);

        exitButton = new RoundedButton("Exit");
        exitButton.setFont(font);
        exitButton.setBounds(300, 300, 80, 35);
        exitButton.setBackground(new Color(153, 153, 255)); // Light blue
        exitButton.setForeground(Color.BLACK);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        exitButton.addActionListener(this);
        formPanel.add(exitButton);

        panel.add(formPanel);
        this.add(panel);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == signUp) {
            this.setVisible(false);
            new RegisterFrame().setVisible(true);
            return;
        }

        if (ae.getSource() == login) {
            String username = textField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both username and password");
                return;
            }

            Account account = new Account();
            if (account.validateLogin(username, password)) {
                String accountType = account.getUserType(username);
                String selectedType = customerRadio.isSelected() ? "customer" : "admin";

                if (!accountType.equals(selectedType)) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid user type selected. Please select the correct user type.");
                    return;
                }

                JOptionPane.showMessageDialog(null, "Login Successful!");
                this.setVisible(false);
                new Homepage(username, this).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                textField.setText("");
                passwordField.setText("");
                textField.requestFocus();
            }
        } else if (ae.getSource() == exitButton) {
            System.exit(0);
        }
    }
}