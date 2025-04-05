package carDealership;

import persistance.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginPage() {
        setTitle("Car Dealership System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a panel for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(59, 89, 182));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this);
        formPanel.add(loginButton, gbc);

        // Message label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        formPanel.add(messageLabel, gbc);

        // Add form panel to the frame
        add(formPanel, BorderLayout.CENTER);

        // Add a title label at the top
        JLabel titleLabel = new JLabel("Car Dealership System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Set background color for the frame
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
     // Allow pressing Enter to trigger login
        getRootPane().setDefaultButton(loginButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (authenticate(username, password)) {
                messageLabel.setText("Login successful!");
                // If the user uses a temporary password, force a password change.
                if (isTemporaryPassword(username)) {
                    ChangePasswordDialog changeDialog = new ChangePasswordDialog(username);
                    changeDialog.setVisible(true);
                } else {
                    // Otherwise, fetch the role and continue as normal.
                    String role = getRole(username);
                    System.out.println("Role: " + role); // Debugging statement
                    AdminDashboard dashboard = createDashboardInstance(role);
                    dashboard.setVisible(true);
                    dispose(); // Close the login window
                }
            } else {
                messageLabel.setText("Invalid username or password. Please try again.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("Database error. Please try again later.");
        }
    }

    private boolean authenticate(String username, String password) throws SQLException {
        DatabaseManager dbManager = new DatabaseManager();
        String query = "SELECT * FROM users WHERE name = '" + username + "' AND password = '" + password + "'";
        System.out.println("Executing query: " + query); // Debugging statement
        ResultSet resultSet = dbManager.runQuery(query);
        boolean authenticated = resultSet.next();
        dbManager.close();
        return authenticated;
    }

    private String getRole(String username) throws SQLException {
        DatabaseManager dbManager = new DatabaseManager();
        String query = "SELECT roles.role FROM users JOIN roles ON users.roleId = roles.id WHERE users.name = '" + username + "'";
        System.out.println("Executing query: " + query); // Debugging statement
        ResultSet resultSet = dbManager.runQuery(query);
        String role = null;
        if (resultSet.next()) {
            role = resultSet.getString("role");
        }
        dbManager.close();
        return role;
    }

    private boolean isTemporaryPassword(String username) throws SQLException {
        DatabaseManager dbManager = new DatabaseManager();
        String query = "SELECT isTemp FROM users WHERE name = '" + username + "'";
        System.out.println("Executing temp query: " + query); // Debug
        ResultSet resultSet = dbManager.runQuery(query);
        boolean isTemp = false;
        if (resultSet.next()) {
            isTemp = resultSet.getBoolean("isTemp");
        }
        dbManager.close();
        return isTemp;
    }

    public static void displayLogin() {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }

    private AdminDashboard createDashboardInstance(String role) {
        if (role.equals("Admin")) {
            return new AdminDashboard("Admin");
        } else if (role.equals("Manager")) {
            return new AdminDashboard("Manager");
        } else if (role.equals("Salesperson")) {
            return new AdminDashboard("Salesperson");
        }
        return null;
    }
    
}