package carDealership;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.SQLException;

public class AdminDashboard extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton userManagementButton, inventoryManagementButton, salesHistoryButton, viewDealershipButton, sellVehicleButton, logoutButton, quitButton;
    private JLabel roleLabel;
    private String role; //not yet defined
    private JPanel mainPanel, sidebar;
    private CardLayout cardLayout;
    private Dealership dealership; 
    private JButton selectedButton;

    public AdminDashboard(String role) {
        this.role = role;
        setTitle("Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
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

        userManagementButton = createSidebarButton("User Management");
        inventoryManagementButton = createSidebarButton("Inventory Management");
        salesHistoryButton = createSidebarButton("Sales History");
        viewDealershipButton = createSidebarButton("View Dealership");
        sellVehicleButton = createSidebarButton("Sell Vehicle");
        logoutButton = createSidebarButton("Logout");
        quitButton = createSidebarButton("Quit");

        sidebar.add(userManagementButton);
        sidebar.add(inventoryManagementButton);
        sidebar.add(salesHistoryButton);
        sidebar.add(viewDealershipButton);
        sidebar.add(sellVehicleButton);
        sidebar.add(logoutButton);
        sidebar.add(quitButton);

        // Main Content Panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.DARK_GRAY);

        // Add different panels for each management section
        mainPanel.add(createUserManagementPanel(), "User Management");
        mainPanel.add(createInventoryManagementPanel(), "Inventory Management");
        mainPanel.add(createSalesHistoryPanel(), "Sales History");
        mainPanel.add(createViewDealershipPanel(), "View Dealership");
        mainPanel.add(createSellVehiclePanel(), "Sell Vehicle");

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
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

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Label for user management
        JLabel label = new JLabel("User Management");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        // Top panel with buttons
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");
        JButton viewUsersButton = new JButton("View Users");
        topPanel.add(addUserButton);
        topPanel.add(editUserButton);
        topPanel.add(deleteUserButton);
        topPanel.add(viewUsersButton);

        // Table for users
        String[] columnNames = {"User ID", "Username", "Password", "Role"};
        Object[][] data = {}; // Empty data for now
        JTable userTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(userTable);

        panel.add(topPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        return panel;
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
        JButton addVehicleButton = new JButton("Add Vehicle");
        JButton editVehicleButton = new JButton("Edit Vehicle");
        JButton deleteVehicleButton = new JButton("Delete Vehicle");
        JButton viewVehiclesButton = new JButton("View Vehicles");
        topPanel.add(addVehicleButton);
        topPanel.add(editVehicleButton);
        topPanel.add(deleteVehicleButton);
        topPanel.add(viewVehiclesButton);

        // Table for vehicles
        String[] columnNames = {"Vehicle ID", "Make", "Model", "Year", "Price"};
        Object[][] data = {}; // Empty data for now
        JTable vehicleTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(vehicleTable);

        panel.add(topPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSalesHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Label for sales history
        JLabel label = new JLabel("Sales History");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        // Table for sales history
        String[] columnNames = {"Sale ID", "Vehicle ID", "Customer Name", "Date", "Price"};
        Object[][] data = {}; // Empty data for now
        JTable salesTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(salesTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createViewDealershipPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);
    
        // Label for dealership info
        JLabel label = new JLabel("Dealership Information", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);
    
        // Use FlowLayout so that fixed-size cards don't stretch to fill the area
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Get dealership information
        String info = dealership.getInfoGUI();
        String[] infoLines = info.split("\n");
    
        // Create cards with dealership information
        for (String line : infoLines) {
            if (line.contains(":")) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String title = parts[0].trim();
                    String value = parts[1].trim();
                    cardsPanel.add(createInfoCard(title, value));
                }
            }
        }
    
        panel.add(cardsPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInfoCard(String title, String value) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                // Use light purple gradient colors (Lavender to Thistle)
                Color color1 = new Color(230, 230, 250); // Lavender
                Color color2 = new Color(216, 191, 216); // Thistle
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        // Increase card size for better adjustment
        card.setPreferredSize(new Dimension(200, 150));
        card.setMaximumSize(new Dimension(200, 150));
        card.setMinimumSize(new Dimension(200, 150));
        // Set thinner border
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        card.setLayout(new BorderLayout());
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
        // Create a content panel with vertical/horizontal center alignment
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        JLabel imageLabel = new JLabel(new ImageIcon("icons/info.png"));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        // Center content using vertical glue
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(3));
        contentPanel.add(valueLabel);
        contentPanel.add(Box.createVerticalStrut(3));
        contentPanel.add(imageLabel);
        contentPanel.add(Box.createVerticalGlue());
    
        card.add(contentPanel, BorderLayout.CENTER);
    
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
        });
    
        return card;
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
        if (e.getSource() == userManagementButton) {
            setSelectedButton(userManagementButton);
            cardLayout.show(mainPanel, "User Management");
        } else if (e.getSource() == inventoryManagementButton) {
            setSelectedButton(inventoryManagementButton);
            cardLayout.show(mainPanel, "Inventory Management");
        } else if (e.getSource() == salesHistoryButton) {
            setSelectedButton(salesHistoryButton);
            cardLayout.show(mainPanel, "Sales History");
        } else if (e.getSource() == viewDealershipButton) {
            setSelectedButton(viewDealershipButton);
            cardLayout.show(mainPanel, "View Dealership");
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
            new AdminDashboard("Admin").setVisible(true);
        });
    }
}