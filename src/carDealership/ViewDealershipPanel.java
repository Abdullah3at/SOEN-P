package carDealership;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import persistance.DatabaseManager;

public class ViewDealershipPanel extends JPanel {
    private AdminDashboard parent;
    private JPanel cardsPanel;

    public ViewDealershipPanel(AdminDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // --- Modern Styled Header ---
        ModernPanel headerPanel = new ModernPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("Dealership Overview");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Cards Panel ---
        cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Watermark Background Panel ---
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

        // --- Overlay Both Panels ---
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.setPreferredSize(new Dimension(1000, 700));
        layeredPane.add(watermarkPanel, Integer.valueOf(1));
        layeredPane.add(cardsPanel, Integer.valueOf(0));

        add(layeredPane, BorderLayout.CENTER);

        // --- Reload Data When Shown ---
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshData();
            }
        });
    }

    private void refreshData() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                SwingUtilities.invokeLater(() -> cardsPanel.removeAll());

                final String[] dealershipname = {""}, dealershiplocation = {""}, totalSalesAmount = {""};
                final String[] totalVehicleSold = {""}, TotalCarsAvailable = {""}, TotalMotorcyclesAvailable = {""};
                final String[] totalCarsSold = {""}, totalMotorcyclesSold = {""}, totalInventoryCost = {""};
                final String[] availableCapacity = {""}, totalUsers = {""}, totalSalesPersons = {""};
                final String[] totalAdmins = {""}, totalManagers = {""};

                try {
                    DatabaseManager dbManager = new DatabaseManager();
                    ResultSet rs;

                    rs = dbManager.runQuery("SELECT name, location, capacity FROM dealerships LIMIT 1");
                    int capacity = 0;
                    if (rs.next()) {
                        dealershipname[0] = rs.getString("name");
                        dealershiplocation[0] = rs.getString("location");
                        capacity = rs.getInt("capacity");
                    }

                    rs = dbManager.runQuery("SELECT COUNT(*) as availableCount FROM vehicles WHERE sold = 0");
                    int availableCount = rs.next() ? rs.getInt("availableCount") : 0;
                    availableCapacity[0] = String.valueOf(capacity - availableCount);

                    rs = dbManager.runQuery("SELECT COUNT(*) as totalVehiclesSold, SUM(price) as totalSalesAmount FROM sales");
                    if (rs.next()) {
                        totalVehicleSold[0] = String.valueOf(rs.getInt("totalVehiclesSold"));
                        totalSalesAmount[0] = String.format("$%.2f", rs.getDouble("totalSalesAmount"));
                    }

                    rs = dbManager.runQuery("SELECT COUNT(*) as totalCarsAvailable FROM vehicles WHERE type='Car' AND sold = 0");
                    TotalCarsAvailable[0] = rs.next() ? rs.getString("totalCarsAvailable") : "0";

                    rs = dbManager.runQuery("SELECT COUNT(*) as totalMotorcyclesAvailable FROM vehicles WHERE type='Motorcycle' AND sold = 0");
                    TotalMotorcyclesAvailable[0] = rs.next() ? rs.getString("totalMotorcyclesAvailable") : "0";

                    rs = dbManager.runQuery("SELECT COUNT(*) as totalCarsSold FROM vehicles WHERE type='Car' AND sold = 1");
                    totalCarsSold[0] = rs.next() ? rs.getString("totalCarsSold") : "0";

                    rs = dbManager.runQuery("SELECT COUNT(*) as totalMotorcyclesSold FROM vehicles WHERE type='Motorcycle' AND sold = 1");
                    totalMotorcyclesSold[0] = rs.next() ? rs.getString("totalMotorcyclesSold") : "0";

                    rs = dbManager.runQuery("SELECT SUM(price) as totalInventoryCost FROM vehicles WHERE sold = 0");
                    totalInventoryCost[0] = rs.next() ? String.format("$%.2f", rs.getDouble("totalInventoryCost")) : "$0.00";

                    rs = dbManager.runQuery("SELECT COUNT(*) as totalUsers FROM users");
                    totalUsers[0] = rs.next() ? rs.getString("totalUsers") : "0";

                    rs = dbManager.runQuery("SELECT COUNT(*) as totalSalesPersons FROM users JOIN roles ON users.roleId = roles.id WHERE roles.role = 'Salesperson'");
                    totalSalesPersons[0] = rs.next() ? rs.getString("totalSalesPersons") : "0";

                    rs = dbManager.runQuery("SELECT COUNT(*) as totalAdmins FROM users JOIN roles ON users.roleId = roles.id WHERE roles.role = 'Admin'");
                    totalAdmins[0] = rs.next() ? rs.getString("totalAdmins") : "0";

                    rs = dbManager.runQuery("SELECT COUNT(*) as totalManagers FROM users JOIN roles ON users.roleId = roles.id WHERE roles.role = 'Manager'");
                    totalManagers[0] = rs.next() ? rs.getString("totalManagers") : "0";

                    dbManager.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(ViewDealershipPanel.this,
                            "Error fetching dealership data."));
                }

                SwingUtilities.invokeLater(() -> {
                    addCard("Dealership Name", dealershipname[0]);
                    addCard("Dealership Location", dealershiplocation[0]);
                    addCard("Total Sales Amount", totalSalesAmount[0]);
                    addCard("Total Vehicles Sold", totalVehicleSold[0]);
                    addCard("Total Cars Available", TotalCarsAvailable[0]);
                    addCard("Total Motorcycles Available", TotalMotorcyclesAvailable[0]);
                    addCard("Total Cars Sold", totalCarsSold[0]);
                    addCard("Total Motorcycles Sold", totalMotorcyclesSold[0]);
                    addCard("Total Inventory Cost", totalInventoryCost[0]);
                    addCard("Available Capacity", availableCapacity[0]);
                    addCard("Total Users", totalUsers[0]);
                    addCard("Total Sales Persons", totalSalesPersons[0]);
                    addCard("Total Admins", totalAdmins[0]);
                    addCard("Total Managers", totalManagers[0]);

                    cardsPanel.revalidate();
                    cardsPanel.repaint();
                });

                return null;
            }
        }.execute();
    }

    private void addCard(String title, String value) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(200, 100));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(valueLabel);

        cardsPanel.add(card);
    }
}