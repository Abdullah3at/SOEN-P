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

        // --- HEADER ---
        ModernPanel headerPanel = new ModernPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("Sell Vehicle");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerPanel.add(headerLabel);

        // --- FORM PANEL ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fields
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Vehicle ID:"), gbc);
        gbc.gridx = 1;
        JTextField vehicleIdField = new JTextField(15);
        formPanel.add(vehicleIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Buyer Name:"), gbc);
        gbc.gridx = 1;
        JTextField buyerNameField = new JTextField(15);
        formPanel.add(buyerNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        formPanel.add(dateChooser, gbc);

        // Button
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton sellButton = new JButton("Sell Vehicle");
        StyleHelper.styleButton(sellButton);
        formPanel.add(sellButton, gbc);

        // --- ACTION ---
        sellButton.addActionListener(e -> {
            String vehIdStr = vehicleIdField.getText().trim();
            String buyerName = buyerNameField.getText().trim();

            if (vehIdStr.isEmpty() || buyerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int vehicleIdInt;
            try {
                vehicleIdInt = Integer.parseInt(vehIdStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Vehicle ID.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date selectedDate = dateChooser.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            String sqlDate = dbFormat.format(selectedDate);

            try {
                DatabaseManager dbManager = new DatabaseManager();
                String query1 = "SELECT id, make, model, price FROM vehicles WHERE id = ?";
                PreparedStatement stmt1 = dbManager.getConnection().prepareStatement(query1);
                stmt1.setInt(1, vehicleIdInt);
                ResultSet rs1 = stmt1.executeQuery();

                if (!rs1.next()) {
                    JOptionPane.showMessageDialog(this, "Vehicle not found.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String make = rs1.getString("make");
                String model = rs1.getString("model");
                double price = rs1.getDouble("price");

                String query2 = "INSERT INTO sales (vehicleId, customerName, date, price) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt2 = dbManager.getConnection().prepareStatement(query2);
                stmt2.setInt(1, vehicleIdInt);
                stmt2.setString(2, buyerName);
                stmt2.setString(3, sqlDate);
                stmt2.setDouble(4, price);

                int rowsInserted = stmt2.executeUpdate();

                if (rowsInserted > 0) {
                    String query3 = "UPDATE vehicles SET sold = '1' WHERE id = ?";
                    PreparedStatement stmt3 = dbManager.getConnection().prepareStatement(query3);
                    stmt3.setInt(1, vehicleIdInt);
                    stmt3.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Vehicle sold successfully!\n" +
                            "Make: " + make + "\nModel: " + model + "\nPrice: $" + price,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to process sale.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

                rs1.close(); stmt1.close(); stmt2.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- WATERMARK BACKGROUND ---
        JPanel watermarkPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                ImageIcon icon = new ImageIcon("src/images/car.png");
                int fixedWidth = 1100;
                int fixedHeight = 1100;
                int x = (getWidth() - fixedWidth) / 2;
                int y = (getHeight() - fixedHeight) / 2;

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
                g2d.drawImage(icon.getImage(), x, y, fixedWidth, fixedHeight, this);
                g2d.dispose();
            }
        };
        watermarkPanel.setOpaque(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.add(watermarkPanel, Integer.valueOf(1));
        layeredPane.add(formPanel, Integer.valueOf(0));
        layeredPane.setPreferredSize(new Dimension(700, 350));

        // --- FINAL LAYOUT ---
        add(Box.createVerticalStrut(10), BorderLayout.NORTH);
        add(headerPanel, BorderLayout.NORTH);
        add(layeredPane, BorderLayout.CENTER);
    }
}
