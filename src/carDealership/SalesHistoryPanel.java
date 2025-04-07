package carDealership;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import persistance.DatabaseManager;

public class SalesHistoryPanel extends JPanel {
    private AdminDashboard parent;
    private JTable salesTable;

    public SalesHistoryPanel(AdminDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // --- HEADER ---
        ModernPanel headerPanel = new ModernPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("Sales History");
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerPanel.add(label);

        // --- BUTTON PANEL ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton filterButton = new JButton("Filter Sales History");
        JButton clearFiltersButton = new JButton("Clear Filters");

        JButton[] buttons = {filterButton, clearFiltersButton};
        for (JButton button : buttons) {
            StyleHelper.styleButton(button);
            button.setPreferredSize(new Dimension(160, 40));
            buttonPanel.add(button);
        }

        filterButton.addActionListener(e -> parent.showSalesHistoryFilterDialog(salesTable));
        clearFiltersButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
            model.setRowCount(0);
            fetchSalesData(model);
        });

        // Combine header and buttons
        JPanel combinedHeader = new JPanel();
        combinedHeader.setLayout(new BoxLayout(combinedHeader, BoxLayout.Y_AXIS));
        combinedHeader.setBackground(Color.LIGHT_GRAY);
        combinedHeader.add(Box.createVerticalStrut(10));
        combinedHeader.add(headerPanel);
        combinedHeader.add(Box.createVerticalStrut(5));
        combinedHeader.add(buttonPanel);
        combinedHeader.add(Box.createVerticalStrut(10));

        // --- TABLE SETUP ---
        String[] columnNames = {"Sale ID", "Vehicle ID", "Make", "Model", "Type", "Color", "Customer Name", "Date", "Price", "Year"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        salesTable = new JTable(model);
        salesTable.setRowHeight(25);
        StyleHelper.styleTable(salesTable);

        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        salesTable.setOpaque(false);

        // --- WATERMARK PANEL ---
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

        // --- LAYERED PANE ---
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.setPreferredSize(new Dimension(1000, 400));
        layeredPane.add(watermarkPanel, Integer.valueOf(1));
        layeredPane.add(scrollPane, Integer.valueOf(0));

        // --- FINAL LAYOUT ---
        add(combinedHeader, BorderLayout.NORTH);
        add(layeredPane, BorderLayout.CENTER);

        // Load data
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
                model.addRow(new Object[]{
                        resultSet.getInt("sale_id"),
                        resultSet.getInt("vehicle_id"),
                        resultSet.getString("make"),
                        resultSet.getString("model"),
                        resultSet.getString("type"),
                        resultSet.getString("color"),
                        resultSet.getString("customerName"),
                        resultSet.getString("date"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("vehicleYear")
                });
            }
            dbManager.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching sales history.");
        }
    }
}
