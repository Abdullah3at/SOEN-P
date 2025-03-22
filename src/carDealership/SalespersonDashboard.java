package carDealership;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.SQLException;

public class SalespersonDashboard extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton inventoryManagementButton, sellVehicleButton, logoutButton, quitButton;
    private JLabel roleLabel;
    private String role;
    private JPanel mainPanel, sidebar;
    private CardLayout cardLayout;
    private Dealership dealership; // Reference to the Dealership class
    private JButton selectedButton; // Track the selected button

    public SalespersonDashboard(String role) {
        this.role = role;
        setTitle("Salesperson Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Initialize the Dealership instance
        try {
            dealership = new Dealership("My Dealership", "123 Main St", 100);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Sidebar Panel
        sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1, 10, 10)); // One button per row
        sidebar.setBackground(new Color(35, 45, 65));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        roleLabel = new JLabel("Role: " + role, SwingConstants.CENTER);
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        sidebar.add(roleLabel);

        inventoryManagementButton = createSidebarButton("Inventory Management", new ImageIcon("icons/inventory.png"));
        sellVehicleButton = createSidebarButton("Sell Vehicle", new ImageIcon("icons/sell.png"));
        logoutButton = createSidebarButton("Logout", new ImageIcon("icons/logout.png"));
        quitButton = createSidebarButton("Quit", new ImageIcon("icons/quit.png"));

        sidebar.add(inventoryManagementButton);
        sidebar.add(sellVehicleButton);
        sidebar.add(logoutButton);
        sidebar.add(quitButton);

        // Main Content Panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.DARK_GRAY);

        // Add different panels for each management section
        mainPanel.add(createInventoryManagementPanel(), "Inventory Management");
        mainPanel.add(createSellVehiclePanel(), "Sell Vehicle");

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text, ImageIcon icon) {
        JButton button = new JButton(text, icon);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.addActionListener(this);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != selectedButton) {
                    button.setBackground(new Color(50, 60, 80));
                    button.setOpaque(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != selectedButton) {
                    button.setBackground(new Color(35, 45, 65));
                    button.setOpaque(false);
                }
            }
        });
        return button;
    }

    private void setSelectedButton(JButton selectedButton) {
        this.selectedButton = selectedButton;
        for (Component component : sidebar.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button == selectedButton) {
                    button.setBackground(new Color(50, 60, 80));
                    button.setOpaque(true);
                } else {
                    button.setBackground(new Color(35, 45, 65));
                    button.setOpaque(false);
                }
            }
        }
    }

    private JPanel createInventoryManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Label for inventory management
        JLabel label = new JLabel("Inventory Management");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        // Top panel with buttons
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton viewVehiclesButton = new JButton("View Vehicles");
        JButton filterButton = new JButton("Filter"); // Add Filter button
        topPanel.add(viewVehiclesButton);
        topPanel.add(filterButton); // Add Filter button to the panel

        // Table for vehicles
        String[] columnNames = {"Vehicle ID", "Make", "Model", "Year", "Price"};
        Object[][] data = {}; // Empty data for now
        JTable vehicleTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(vehicleTable);

        panel.add(topPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        // Add action listener to the filter button
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFilterDialog(vehicleTable);
            }
        });

        return panel;
    }

    private void showFilterDialog(JTable vehicleTable) {
        JDialog filterDialog = new JDialog(this, "Filter Vehicles", true);
        filterDialog.setSize(400, 300);
        filterDialog.setLayout(new GridLayout(0, 1, 10, 10));
        filterDialog.setLocationRelativeTo(this);

        JCheckBox budgetCheckBox = new JCheckBox("Budget");
        JCheckBox typeCheckBox = new JCheckBox("Type");
        JCheckBox colorCheckBox = new JCheckBox("Color");
        JCheckBox brandCheckBox = new JCheckBox("Brand");

        filterDialog.add(budgetCheckBox);
        filterDialog.add(typeCheckBox);
        filterDialog.add(colorCheckBox);
        filterDialog.add(brandCheckBox);

        JButton applyButton = new JButton("Apply Filters");
        filterDialog.add(applyButton);

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Apply filters based on selected checkboxes
                applyFilters(vehicleTable, budgetCheckBox.isSelected(), typeCheckBox.isSelected(), colorCheckBox.isSelected(), brandCheckBox.isSelected());
                filterDialog.dispose();
            }
        });

        filterDialog.setVisible(true);
    }

    private void applyFilters(JTable vehicleTable, boolean budget, boolean type, boolean color, boolean brand) {
        // Implement the logic to filter the vehicle data based on the selected filters
        // For demonstration purposes, we'll just print the selected filters
        System.out.println("Filters applied:");
        if (budget) System.out.println(" - Budget");
        if (type) System.out.println(" - Type");
        if (color) System.out.println(" - Color");
        if (brand) System.out.println(" - Brand");

        // Update the vehicleTable with the filtered data
        // This is where you would query the database with the selected filters and update the table model
    }

    private JPanel createSellVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Label for selling vehicle
        JLabel label = new JLabel("Sell Vehicle");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        // Form for selling vehicle
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Vehicle ID:"), gbc);
        gbc.gridx = 1;
        JTextField vehicleIdField = new JTextField(15);
        formPanel.add(vehicleIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        JTextField customerNameField = new JTextField(15);
        formPanel.add(customerNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        JTextField dateField = new JTextField(15);
        formPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        JTextField priceField = new JTextField(15);
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton sellButton = new JButton("Sell Vehicle");
        formPanel.add(sellButton, gbc);

        // Add action listener to the sell button
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String vehicleId = vehicleIdField.getText();
                String customerName = customerNameField.getText();
                String date = dateField.getText();
                String price = priceField.getText();

                // Validate input
                if (vehicleId.isEmpty() || customerName.isEmpty() || date.isEmpty() || price.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Convert vehicleId and price to integers
                int vehicleIdInt;
                double priceDouble;
                try {
                    vehicleIdInt = Integer.parseInt(vehicleId);
                    priceDouble = Double.parseDouble(price);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input for Vehicle ID or Price.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get the vehicle from the dealership
                Vehicle vehicle = dealership.getVehicleFromId(vehicleIdInt);
                if (vehicle == null) {
                    JOptionPane.showMessageDialog(panel, "Vehicle not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Sell the vehicle
                boolean success = dealership.sellVehicle(vehicle, customerName, date);
                if (success) {
                    JOptionPane.showMessageDialog(panel, "Vehicle sold successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to sell vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == inventoryManagementButton) {
            setSelectedButton(inventoryManagementButton);
            cardLayout.show(mainPanel, "Inventory Management");
        } else if (e.getSource() == sellVehicleButton) {
            setSelectedButton(sellVehicleButton);
            cardLayout.show(mainPanel, "Sell Vehicle");
        } else if (e.getSource() == logoutButton) {
            dispose();
            LoginPage.displayLogin();
        } else if (e.getSource() == quitButton) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SalespersonDashboard("Salesperson").setVisible(true);
        });
    }
}