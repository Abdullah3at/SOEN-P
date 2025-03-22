package carDealership;

import persistance.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesHistoryPanel extends JPanel {
    private AdminDashboard parent;
    private JTable salesTable;
    
    public SalesHistoryPanel(AdminDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        
        // Header panel with centered title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Sales History");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(label);
        
        // Button panel for filter button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton filterButton = new JButton("Filter Sales History");
        filterButton.addActionListener(e -> parent.showSalesHistoryFilterDialog(salesTable));
        buttonPanel.add(filterButton);
        
        // Combine header and button panels vertically
        JPanel combinedHeader = new JPanel();
        combinedHeader.setLayout(new BoxLayout(combinedHeader, BoxLayout.Y_AXIS));
        combinedHeader.setBackground(Color.LIGHT_GRAY);
        combinedHeader.add(headerPanel);
        combinedHeader.add(buttonPanel);
        
        // Table for sales history
        String[] columnNames = {"Sale ID", "Vehicle ID", "Make", "Model", "Type", "Color", "Customer Name", "Date", "Price", "Year"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        salesTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(salesTable);
        salesTable.setRowHeight(25); // Set the row height to 25 pixels

        
        add(combinedHeader, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Fetch and populate sales data
        fetchSalesData(model);
    }
    
    private void fetchSalesData(DefaultTableModel model) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            String query = "SELECT s.id AS sale_id, v.id AS vehicle_id, v.make, v.model, v.type, v.color, " +
               "s.customerName, s.date, s.price, v.year AS vehicleYear " + 
               "FROM sales s JOIN vehicles v ON s.vehicleId = v.id ORDER BY s.id";

            ResultSet resultSet = dbManager.runQuery(query);
            while (resultSet.next()) {
                int saleId = resultSet.getInt("sale_id");
                int vehicleId = resultSet.getInt("vehicle_id");
                String make = resultSet.getString("make");
                String modelStr = resultSet.getString("model");
                String type = resultSet.getString("type");
                String color = resultSet.getString("color");
                String customerName = resultSet.getString("customerName");
                String date = resultSet.getString("date");
                double price = resultSet.getDouble("price");
                int year = resultSet.getInt("vehicleYear");
                model.addRow(new Object[]{saleId, vehicleId, make, modelStr, type, color, customerName, date, price, year});
            }
            dbManager.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching sales history.");
        }
    }
    
}