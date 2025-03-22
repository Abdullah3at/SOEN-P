package carDealership;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class SellVehiclePanel extends JPanel {
    
    private AdminDashboard parent;
    
    public SellVehiclePanel(AdminDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        
        JLabel headerLabel = new JLabel("Sell Vehicle", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(headerLabel, BorderLayout.NORTH);
        
        // Add the custom Sell Vehicle form components
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
        dateChooser.setDateFormatString("dd-MM-yyyy");
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
    
            // Validate date selection and convert to SQL-friendly format (yyyy-MM-dd)
            Date selectedDate = dateChooser.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(panel, "Please select a date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            String sqlDate = dbFormat.format(selectedDate);
    
            // Retrieve the vehicle from the database using the dealership instance
            Vehicle vehicle = parent.dealership.getVehicleFromId(vehicleIdInt);
            if (vehicle == null) {
                JOptionPane.showMessageDialog(panel, "Vehicle not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Sell the vehicle: add sales record and mark the vehicle as sold.
            boolean success = parent.dealership.sellVehicle(vehicle, buyerName, sqlDate);
            if (success) {
                JOptionPane.showMessageDialog(panel, "Vehicle sold successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                // Optionally, perform additional actions (refresh UI, etc.)
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to sell vehicle.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }
}