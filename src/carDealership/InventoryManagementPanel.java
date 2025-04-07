package carDealership;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InventoryManagementPanel extends JPanel {
    private AdminDashboard parent;
    private JTable vehicleTable;

    public InventoryManagementPanel(AdminDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // --- HEADER ---
        ModernPanel headerPanel = new ModernPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("Inventory Management");
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerPanel.add(label);

        // --- BUTTON PANEL ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton addVehicleButton = new JButton("Add Vehicle");
        JButton editVehicleButton = new JButton("Edit Vehicle");
        JButton deleteVehicleButton = new JButton("Delete Vehicle");
        JButton viewVehiclesButton = new JButton("View Vehicles");
        JButton filterButton = new JButton("Filter");

        JButton[] buttons = {addVehicleButton, editVehicleButton, deleteVehicleButton, viewVehiclesButton, filterButton};
        for (JButton button : buttons) {
            buttonPanel.add(button);
            StyleHelper.styleButton(button);
            button.setPreferredSize(new Dimension(140, 40));
        }

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
        String[] columnNames = {"Vehicle ID", "Make", "Model", "Year", "Price", "Type", "Color"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        vehicleTable = new JTable(model);
        vehicleTable.setRowHeight(25);
        StyleHelper.styleTable(vehicleTable); // Your custom styling

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        vehicleTable.setOpaque(false);

        // --- WATERMARK PANEL ---
        JPanel watermarkPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                ImageIcon icon = new ImageIcon("src/images/car.png"); // 
                int fixedWidth = 1100;
                int fixedHeight = 1100;
                int x = (getWidth() - fixedWidth) / 2;
                int y = (getHeight() - fixedHeight) / 2;

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f)); // Adjust opacity
                g2d.drawImage(icon.getImage(), x, y, fixedWidth, fixedHeight, this);
                g2d.dispose();
            }
        };
        watermarkPanel.setOpaque(false);

        // --- LAYERED PANE TO STACK IMAGE + TABLE ---
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.setPreferredSize(new Dimension(1000, 400));

        layeredPane.add(watermarkPanel, Integer.valueOf(1));  // Top layer
        layeredPane.add(scrollPane, Integer.valueOf(0));      // Table layer

        // --- FINAL LAYOUT ---
        add(combinedHeader, BorderLayout.NORTH);
        add(layeredPane, BorderLayout.CENTER);

        // --- BUTTON ACTIONS ---
        addVehicleButton.addActionListener(e -> parent.showAddVehicleDialog());
        editVehicleButton.addActionListener(e -> parent.showEditVehicleDialog());
        deleteVehicleButton.addActionListener(e -> parent.showDeleteVehicleDialog());
        viewVehiclesButton.addActionListener(e -> parent.showViewVehiclesDialog(model));
        filterButton.addActionListener(e -> parent.showFilterDialog(vehicleTable));

        // Load data automatically
        parent.showViewVehiclesDialog(model);
    }
}
