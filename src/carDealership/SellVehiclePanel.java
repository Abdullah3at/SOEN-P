package carDealership;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.sql.*;
import com.toedter.calendar.JDateChooser;

import persistance.DatabaseManager;

import java.util.Date;

public class SellVehiclePanel extends JPanel {
    
    public SellVehiclePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        
        JLabel headerLabel = new JLabel("Sell Vehicle", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(headerLabel, BorderLayout.NORTH);
        
        add(createSellVehicleForm(), BorderLayout.CENTER);
    }
    
    private JPanel createSellVehicleForm() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Vehicle ID Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Vehicle ID:"), gbc);
        gbc.gridx = 1;
        JTextField vehicleIdField = new JTextField(15);
        formPanel.add(vehicleIdField, gbc);
    
        // Buyer Name Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Buyer Name:"), gbc);
        gbc.gridx = 1;
        JTextField buyerNameField = new JTextField(15);
        formPanel.add(buyerNameField, gbc);
    
        // Date Field using JDateChooser
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        formPanel.add(dateChooser, gbc);
    
        // Sell Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton sellButton = new JButton("Sell Vehicle");
        formPanel.add(sellButton, gbc);
    
        sellButton.addActionListener(e -> {
            String vehIdStr = vehicleIdField.getText().trim();
            String buyerName = buyerNameField.getText().trim();
    
            if (vehIdStr.isEmpty() || buyerName.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all required fields.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int vehicleIdInt;
            try {
                vehicleIdInt = Integer.parseInt(vehIdStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid Vehicle ID.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Validate date selection and convert to SQL format
            Date selectedDate = dateChooser.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(panel, "Please select a date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            String sqlDate = dbFormat.format(selectedDate);
    
            try {
                DatabaseManager dbManager = new DatabaseManager();
                
                // Fetch vehicle details from the database
                String query1 = "SELECT id, make, model, price FROM vehicles WHERE id = ?";
                PreparedStatement stmt1 = dbManager.getConnection().prepareStatement(query1);
                stmt1.setInt(1, vehicleIdInt);
                ResultSet rs1 = stmt1.executeQuery();
    
                if (!rs1.next()) {
                    JOptionPane.showMessageDialog(panel, "Vehicle not found.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                String make = rs1.getString("make");
                String model = rs1.getString("model");
                double price = rs1.getDouble("price");
    
                // Insert sale record
                String query2 = "INSERT INTO sales (vehicleId, customerName, date, price) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt2 = dbManager.getConnection().prepareStatement(query2);
                stmt2.setInt(1, vehicleIdInt);
                stmt2.setString(2, buyerName);
                stmt2.setString(3, sqlDate);
                stmt2.setDouble(4, price);
    
                int rowsInserted = stmt2.executeUpdate();
    
                if (rowsInserted > 0) {
                    // Mark the vehicle as sold
                    String query3 = "UPDATE vehicles SET sold = '1' WHERE id = ?";
                    PreparedStatement stmt3 = dbManager.getConnection().prepareStatement(query3);
                    stmt3.setInt(1, vehicleIdInt);
                    stmt3.executeUpdate();
                    
                    JOptionPane.showMessageDialog(panel, "Vehicle sold successfully!\n" +
                            "Make: " + make + "\nModel: " + model + "\nPrice: $" + price,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to process sale.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                // Close resources
                rs1.close();
                stmt1.close();
                stmt2.close();
    
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }
}
