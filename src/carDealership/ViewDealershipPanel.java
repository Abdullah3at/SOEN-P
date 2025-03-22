package carDealership;

import persistance.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewDealershipPanel extends JPanel {
    private AdminDashboard parent;
    // Assign to the field, not a new local variable in the constructor.
    private JPanel cardsPanel;
    
    public ViewDealershipPanel(AdminDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        
        JLabel headerLabel = new JLabel("Dealership Overview", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(headerLabel, BorderLayout.NORTH);
        
        // Initialize cardsPanel as a field so that refreshData() uses the same instance.
        cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(cardsPanel, BorderLayout.CENTER);

        // Refresh data each time this panel is shown.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshData();
            }
        });
    }
    
    private void refreshData() {
        // Use SwingWorker to run DB queries off the EDT.
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Clear cardsPanel on EDT later.
                SwingUtilities.invokeLater(() -> cardsPanel.removeAll());
                
                // Variables to hold data
                final String[] dealershipname = {""}, dealershiplocation = {""}, totalSalesAmount = {""};
                final String[] totalVehicleSold = {""}, TotalCarsAvailable = {""}, TotalMotorcyclesAvailable = {""};
                final String[] totalCarsSold = {""}, totalMotorcyclesSold = {""}, totalInventoryCost = {""};
                final String[] availableCapacity = {""}, totalUsers = {""}, totalSalesPersons = {""};
                final String[] totalAdmins = {""}, totalManagers = {""};
                
                try {
                    DatabaseManager dbManager = new DatabaseManager();
                    String query1 = "SELECT name, location, capacity FROM dealerships LIMIT 1";
                    ResultSet rs1 = dbManager.runQuery(query1);
                    int capacity = 0;
                    if (rs1.next()) {
                        dealershipname[0] = rs1.getString("name");
                        dealershiplocation[0] = rs1.getString("location");
                        capacity = rs1.getInt("capacity");
                    }
                    String queryInv = "SELECT COUNT(*) as availableCount FROM vehicles WHERE sold = 0";
                    ResultSet rsInv = dbManager.runQuery(queryInv);
                    int availableCount = 0;
                    if (rsInv.next()) {
                        availableCount = rsInv.getInt("availableCount");
                    }
                    availableCapacity[0] = String.valueOf(capacity - availableCount);
                    
                    String query2 = "SELECT COUNT(*) as totalVehiclesSold, SUM(price) as totalSalesAmount FROM sales";
                    ResultSet rs2 = dbManager.runQuery(query2);
                    if (rs2.next()) {
                        totalVehicleSold[0] = String.valueOf(rs2.getInt("totalVehiclesSold"));
                        totalSalesAmount[0] = String.valueOf(rs2.getDouble("totalSalesAmount"));
                    }
                    String query3 = "SELECT COUNT(*) as totalCarsAvailable FROM vehicles WHERE type='Car' AND sold = 0";
                    ResultSet rs3 = dbManager.runQuery(query3);
                    if (rs3.next()) {
                        TotalCarsAvailable[0] = String.valueOf(rs3.getInt("totalCarsAvailable"));
                    }
                    String query4 = "SELECT COUNT(*) as totalMotorcyclesAvailable FROM vehicles WHERE type='Motorcycle' AND sold = 0";
                    ResultSet rs4 = dbManager.runQuery(query4);
                    if (rs4.next()) {
                        TotalMotorcyclesAvailable[0] = String.valueOf(rs4.getInt("totalMotorcyclesAvailable"));
                    }
                    String query5 = "SELECT COUNT(*) as totalCarsSold FROM vehicles WHERE type='Car' AND sold = 1";
                    ResultSet rs5 = dbManager.runQuery(query5);
                    if (rs5.next()) {
                        totalCarsSold[0] = String.valueOf(rs5.getInt("totalCarsSold"));
                    }
                    String query6 = "SELECT COUNT(*) as totalMotorcyclesSold FROM vehicles WHERE type='Motorcycle' AND sold = 1";
                    ResultSet rs6 = dbManager.runQuery(query6);
                    if (rs6.next()) {
                        totalMotorcyclesSold[0] = String.valueOf(rs6.getInt("totalMotorcyclesSold"));
                    }
                    String query7 = "SELECT SUM(price) as totalInventoryCost FROM vehicles WHERE sold = 0";
                    ResultSet rs7 = dbManager.runQuery(query7);
                    if (rs7.next()) {
                        totalInventoryCost[0] = String.valueOf(rs7.getDouble("totalInventoryCost"));
                    }
                    String query8 = "SELECT COUNT(*) as totalUsers FROM users";
                    ResultSet rs8 = dbManager.runQuery(query8);
                    if (rs8.next()) {
                        totalUsers[0] = String.valueOf(rs8.getInt("totalUsers"));
                    }
                    String query9 = "SELECT COUNT(*) as totalSalesPersons FROM users JOIN roles ON users.roleId = roles.id WHERE roles.role = 'Salesperson'";
                    ResultSet rs9 = dbManager.runQuery(query9);
                    if (rs9.next()) {
                        totalSalesPersons[0] = String.valueOf(rs9.getInt("totalSalesPersons"));
                    }
                    String query10 = "SELECT COUNT(*) as totalAdmins FROM users JOIN roles ON users.roleId = roles.id WHERE roles.role = 'Admin'";
                    ResultSet rs10 = dbManager.runQuery(query10);
                    if (rs10.next()) {
                        totalAdmins[0] = String.valueOf(rs10.getInt("totalAdmins"));
                    }
                    String query11 = "SELECT COUNT(*) as totalManagers FROM users JOIN roles ON users.roleId = roles.id WHERE roles.role = 'Manager'";
                    ResultSet rs11 = dbManager.runQuery(query11);
                    if (rs11.next()) {
                        totalManagers[0] = String.valueOf(rs11.getInt("totalManagers"));
                    }
                    dbManager.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(ViewDealershipPanel.this,
                        "Error fetching dealership data."));
                }
                
                // Update the cardsPanel on the EDT.
                SwingUtilities.invokeLater(() -> {
                    cardsPanel.add(parent.createInfoCard("Dealership Name", dealershipname[0]));
                    cardsPanel.add(parent.createInfoCard("Dealership Location", dealershiplocation[0]));
                    cardsPanel.add(parent.createInfoCard("Total Sales Amount", totalSalesAmount[0]));
                    cardsPanel.add(parent.createInfoCard("Total Vehicles Sold", totalVehicleSold[0]));
                    cardsPanel.add(parent.createInfoCard("Total Cars Available", TotalCarsAvailable[0]));
                    cardsPanel.add(parent.createInfoCard("Total Motorcycles Available", TotalMotorcyclesAvailable[0]));
                    cardsPanel.add(parent.createInfoCard("Total Cars Sold", totalCarsSold[0]));
                    cardsPanel.add(parent.createInfoCard("Total Motorcycles Sold", totalMotorcyclesSold[0]));
                    cardsPanel.add(parent.createInfoCard("Total Inventory Cost", totalInventoryCost[0]));
                    cardsPanel.add(parent.createInfoCard("Available Capacity", availableCapacity[0]));
                    cardsPanel.add(parent.createInfoCard("Total Users", totalUsers[0]));
                    cardsPanel.add(parent.createInfoCard("Total Sales Persons", totalSalesPersons[0]));
                    cardsPanel.add(parent.createInfoCard("Total Admins", totalAdmins[0]));
                    cardsPanel.add(parent.createInfoCard("Total Managers", totalManagers[0]));
                    
                    cardsPanel.revalidate();
                    cardsPanel.repaint();
                });
                return null;
            }
        }.execute();
    }
}