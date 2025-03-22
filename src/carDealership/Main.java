package carDealership;

import persistance.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static Scanner input = new Scanner(System.in);
    public static Dealership m_dealership;

    public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException {
        // Pre-login: Ensure dealership info exists
        ensureDealershipExists();
        // Pre-login: Ensure at least one admin user exists
        ensureAdminExists();
        // Once checks pass, show the login page.
        LoginPage.displayLogin();
    }

    private static void ensureDealershipExists() throws SQLException {
        DatabaseManager dbManager = new DatabaseManager();
        String query = "SELECT name, location, capacity FROM dealerships LIMIT 1";
        PreparedStatement stmt = dbManager.getConnection().prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String name = rs.getString("name");
            String location = rs.getString("location");
            int capacity = rs.getInt("capacity");
            m_dealership = new Dealership(name, location, capacity);
        } else {
            showDealershipDialog(); // this dialog is modal
            // Re-check after dialog closes
            stmt = dbManager.getConnection().prepareStatement(query);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String location = rs.getString("location");
                int capacity = rs.getInt("capacity");
                m_dealership = new Dealership(name, location, capacity);
            } else {
                JOptionPane.showMessageDialog(null, "Dealership creation is required. Exiting application.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        dbManager.close();
    }

    private static void showDealershipDialog() throws SQLException {
        JDialog dlg = new JDialog((JFrame) null, "Enter Dealership Information", true);
        dlg.setLayout(new GridLayout(0, 2, 10, 10));
        dlg.setSize(400, 200);
        dlg.setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 25));
        dlg.add(nameLabel);
        dlg.add(nameField);

        JLabel locLabel = new JLabel("Location:");
        JTextField locField = new JTextField();
        locField.setPreferredSize(new Dimension(200, 25));
        dlg.add(locLabel);
        dlg.add(locField);

        JLabel capLabel = new JLabel("Capacity:");
        JTextField capField = new JTextField();
        capField.setPreferredSize(new Dimension(200, 25));
        dlg.add(capLabel);
        dlg.add(capField);

        JButton saveBtn = new JButton("Save");
        dlg.add(new JLabel("")); // placeholder cell
        dlg.add(saveBtn);

        // If the user closes the window, exit the application.
        dlg.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        saveBtn.addActionListener(e -> {
            String dname = nameField.getText().trim();
            String dloc = locField.getText().trim();
            String dcap = capField.getText().trim();
            if (dname.isEmpty() || dloc.isEmpty() || dcap.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int capacity;
            try {
                capacity = Integer.parseInt(dcap);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Capacity must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                DatabaseManager dbManager = new DatabaseManager();
                String insertQuery = "INSERT INTO dealerships (name, location, capacity) VALUES (?, ?, ?)";
                PreparedStatement ps = dbManager.getConnection().prepareStatement(insertQuery);
                ps.setString(1, dname);
                ps.setString(2, dloc);
                ps.setInt(3, capacity);
                ps.executeUpdate();
                dbManager.close();
                JOptionPane.showMessageDialog(dlg, "Dealership information saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dlg, "Error saving dealership information.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dlg.setVisible(true);
    }

    private static void ensureAdminExists() throws SQLException {
        if (!adminExists()) {
            showAdminDialog();
            if (!adminExists()) {
                JOptionPane.showMessageDialog(null, "At least one admin user is required. Exiting application.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    private static boolean adminExists() throws SQLException {
        DatabaseManager dbManager = new DatabaseManager();
        String query = "SELECT COUNT(*) AS count FROM users JOIN roles ON users.roleId = roles.id WHERE roles.role = 'Admin'";
        PreparedStatement stmt = dbManager.getConnection().prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt("count");
        }
        dbManager.close();
        return count > 0;
    }

    private static void showAdminDialog() throws SQLException {
        JDialog dlg = new JDialog((JFrame) null, "Create Admin Account", true);
        
        // Use a panel with BoxLayout for compact layout.
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Username row
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(20);
        userField.setMaximumSize(new Dimension(200, 25));
        userPanel.add(userLabel);
        userPanel.add(userField);
        
        // Password row
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);
        passField.setMaximumSize(new Dimension(200, 25));
        passPanel.add(passLabel);
        passPanel.add(passField);
        
        // Button row
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton saveBtn = new JButton("Save Admin");
        btnPanel.add(saveBtn);
        
        panel.add(userPanel);
        panel.add(passPanel);
        panel.add(btnPanel);
        
        dlg.getContentPane().add(panel);
        dlg.pack();
        dlg.setLocationRelativeTo(null);
        dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlg.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        saveBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Username and password are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                DatabaseManager dbManager = new DatabaseManager();
                String insertQuery = "INSERT INTO users (name, password, roleId) VALUES (?, ?, (SELECT id FROM roles WHERE role = 'Admin'))";
                PreparedStatement ps = dbManager.getConnection().prepareStatement(insertQuery);
                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
                dbManager.close();
                JOptionPane.showMessageDialog(dlg, "Admin user created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dlg, "Error creating admin user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dlg.setVisible(true);
    }
    public static void initializeApplication(String role) {
                // Initialize admin dashboard
                AdminDashboard adminDashboard = new AdminDashboard(role);
                adminDashboard.setVisible(true);
   
        }
    }
