package carDealership;

import persistance.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Year;

public class AdminDashboard extends JFrame implements ActionListener {
    private JButton userManagementButton, inventoryManagementButton, salesHistoryButton, viewDealershipButton, sellVehicleButton, logoutButton, quitButton;
    private JLabel roleLabel;
    private String role;
    private JPanel mainPanel, sidebar;
    private CardLayout cardLayout;
    Dealership dealership;
    private JButton selectedButton; // currently selected sidebar button
    public JTable userTable, vehicleTable, salesTable;
    

    public AdminDashboard() {
        this("Admin");
    }
    
    public AdminDashboard(String role) {
        this.role = role;
        setTitle(role + " Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout());
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width, screenSize.height);  // Set the size based on screen size
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);

        
        // Build the sidebar based on role
        sidebar = new JPanel();
        
sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.X_AXIS));
sidebar.setBackground(new Color(20, 10, 20));

// Left: Role label
roleLabel = new JLabel("  Role: " + role + "  ");
roleLabel.setForeground(Color.WHITE);
roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
sidebar.add(roleLabel);

// Buttons panel (center section)
JPanel navButtonsPanel = new JPanel(new GridBagLayout());
navButtonsPanel.setOpaque(false); // transparent to inherit top bar color

if (role.equalsIgnoreCase("Admin")) {
    navButtonsPanel.add(userManagementButton = createSidebarButton("User Management", new ImageIcon("images/user.png")));
    navButtonsPanel.add(inventoryManagementButton = createSidebarButton("Inventory Management", new ImageIcon("images/icon.jpg")));
    navButtonsPanel.add(salesHistoryButton = createSidebarButton("Sales History", new ImageIcon("images/sales.png")));
    navButtonsPanel.add(viewDealershipButton = createSidebarButton("View Dealership", new ImageIcon("images/dealership.png")));
    navButtonsPanel.add(sellVehicleButton = createSidebarButton("Sell Vehicle", new ImageIcon("images/sell.png")));
} else if (role.equalsIgnoreCase("Manager")) {
    navButtonsPanel.add(inventoryManagementButton = createSidebarButton("Inventory Management", new ImageIcon("images/inventory.png")));
    navButtonsPanel.add(salesHistoryButton = createSidebarButton("Sales History", new ImageIcon("images/sales.png")));
    navButtonsPanel.add(viewDealershipButton = createSidebarButton("View Dealership", new ImageIcon("images/dealership.png")));
    navButtonsPanel.add(sellVehicleButton = createSidebarButton("Sell Vehicle", new ImageIcon("images/sell.png")));
} else if (role.equalsIgnoreCase("Salesperson")) {
    navButtonsPanel.add(inventoryManagementButton = createSidebarButton("Inventory Management", new ImageIcon("images/inventory.png")));
    navButtonsPanel.add(salesHistoryButton = createSidebarButton("Sales History", new ImageIcon("images/sales.png")));
    navButtonsPanel.add(sellVehicleButton = createSidebarButton("Sell Vehicle", new ImageIcon("images/sell.png")));
}

sidebar.add(navButtonsPanel);

// Filler to push the rest to the right
// Common options for all roles
sidebar.add(Box.createHorizontalGlue());

// Right: Logout and Quit buttons in their own right-aligned panel
JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
rightButtonsPanel.setOpaque(false); // Keep background consistent

logoutButton = createSidebarButton("Logout", new ImageIcon("images/logout.png"));
quitButton = createSidebarButton("Quit", new ImageIcon("images/quit.png"));

rightButtonsPanel.add(logoutButton);
rightButtonsPanel.add(quitButton);

sidebar.add(rightButtonsPanel);


        
        // Create the main panel with CardLayout to load separate panels.
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.DARK_GRAY);
        
        // Add panels – you might want to adjust the panels themselves based on role.
        // For example, InventoryManagementPanel for Salesperson can be modified to display only cars.
        if(role.equalsIgnoreCase("Admin")) {
            mainPanel.add(new UserManagementPanel(this), "User Management");
        }
        mainPanel.add(new InventoryManagementPanel(this), "Inventory Management");
        mainPanel.add(new SalesHistoryPanel(this), "Sales History");
        mainPanel.add(new ViewDealershipPanel(this), "View Dealership");
        mainPanel.add(new SellVehiclePanel(), "Sell Vehicle");
        
        add(sidebar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        
    // set default selection for the panel that the user sees after logs in
    JButton defaultButton = null;
    if(role.equalsIgnoreCase("Admin")) {
        //for Admin, default to User Management
        defaultButton = userManagementButton;
    } else {
        //for both Manager and Salesperson, default to Inventory Management.
        defaultButton = inventoryManagementButton;
    }
    if(defaultButton != null) {
        setSelectedButton(defaultButton);
        cardLayout.show(mainPanel, defaultButton.getActionCommand());
    }
    }
    
    //method to create UI for the sidebar with action listener
    private JButton createSidebarButton(String text, ImageIcon icon) {
        JButton button = new JButton(text, icon);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBackground(new Color(67, 70, 75));
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setMinimumSize(new Dimension(180, 80)); // 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
        StyleHelper.styleSidebarButton(button);  // apply custom styles
    
        button.addActionListener(this);
        //hover effects for the buttons
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != selectedButton) {
                    button.setBackground(new Color(67, 70, 75));
                    button.setOpaque(true);
                }
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                if (button != selectedButton) {
                    button.setBackground(new Color(67, 70, 75));
                    button.setOpaque(false);
                }
            }
        });
    
        return button;
    }
    
    
    
    public void setSelectedButton(JButton selectedButton) {
        this.selectedButton = selectedButton;
        for (Component component : sidebar.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button == selectedButton) {
                    button.setBackground(Color.decode("#2F80ED"));
                    button.setOpaque(true);
                } else {
                    button.setBackground(new Color(35, 45, 65));
                    button.setOpaque(false);
                }
                StyleHelper.highlightSidebarButton(button, button == selectedButton);  // 
            }
        }
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton)e.getSource();
        setSelectedButton(source); //update styling of selected button
        String command = e.getActionCommand();
        cardLayout.show(mainPanel, command);
        //if the selected button is Logout - prompt the user to confirm        
        if (e.getSource() == logoutButton) {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
    
            if (choice == JOptionPane.YES_OPTION) {
                dispose(); //close current window
                LoginPage.displayLogin(); //show login page
            }
            //if NO_OPTION is selected, do nothing - simply close the dialog and return 
    
        } else if (e.getSource() == quitButton) {
            System.exit(0);
        }
    }
    
    // The following methods are kept in AdminDashboard to be called by panel components.
    public void showAddVehicleDialog() {
        JDialog addVehicleDialog = new JDialog(this, "Add Vehicle", true);
        addVehicleDialog.setSize(400, 400);
        addVehicleDialog.setLocationRelativeTo(this);
    
        JPanel contentPanel = new JPanel(new GridLayout(0, 2, 20, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JTextField makeField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Car", "Motorcycle"});
        JTextField colorField = new JTextField();
    
        contentPanel.add(new JLabel("Make:"));
        contentPanel.add(makeField);
    
        contentPanel.add(new JLabel("Model:"));
        contentPanel.add(modelField);
    
        contentPanel.add(new JLabel("Year:"));
        contentPanel.add(yearField);
    
        contentPanel.add(new JLabel("Price:"));
        contentPanel.add(priceField);
    
        contentPanel.add(new JLabel("Type:"));
        contentPanel.add(typeComboBox);
    
        contentPanel.add(new JLabel("Color:"));
        contentPanel.add(colorField);
    
        JButton addButton = new JButton("Add");
        contentPanel.add(new JLabel()); // filler
        contentPanel.add(addButton);
    
        addVehicleDialog.setContentPane(contentPanel);
    
        addButton.addActionListener(e -> {
            String make = makeField.getText().trim();
            String model = modelField.getText().trim();
            String yearText = yearField.getText().trim();
            String priceText = priceField.getText().trim();
            String type = (String) typeComboBox.getSelectedItem();
            String color = colorField.getText().trim();
    
            // Check all required fields are filled
            if (make.isEmpty() || model.isEmpty() || yearText.isEmpty() || priceText.isEmpty() || color.isEmpty()) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (!make.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Make must contain only letters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            int year;
            double price;
            // Validate Year
            try {
                year = Integer.parseInt(yearText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Year must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            if (year < 1960 || year > currentYear) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Year must be between 1960 and " + currentYear + ".", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Validate Price
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Price must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (price < 1) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Price must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add vehicle to the database
            try {
                DatabaseManager dbManager = new DatabaseManager();
                String query = "INSERT INTO vehicles (make, model, year, price, type, color) VALUES ('" 
                            + make + "', '" + model + "', " + year + ", " + price + ", '" + type + "', '" + color + "')";
                dbManager.runInsert(query);
                dbManager.close();
                JOptionPane.showMessageDialog(this, "Vehicle added successfully.");
                addVehicleDialog.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding vehicle.");
            }
        });
    
        addVehicleDialog.setVisible(true);
    }

   public void showEditVehicleDialog() {
        JDialog editVehicleDialog = new JDialog(this, "Edit Vehicle", true);
        editVehicleDialog.setSize(400, 400);
        editVehicleDialog.setLocationRelativeTo(this);
    
        JPanel contentPanel = new JPanel(new GridLayout(0, 2, 20, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JTextField vehicleIdField = new JTextField();
        JTextField makeField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Car", "Motorcycle"});
        JTextField colorField = new JTextField();
    
        contentPanel.add(new JLabel("Vehicle ID:"));
        contentPanel.add(vehicleIdField);
    
        contentPanel.add(new JLabel("New Make:"));
        contentPanel.add(makeField);
    
        contentPanel.add(new JLabel("New Model:"));
        contentPanel.add(modelField);
    
        contentPanel.add(new JLabel("New Year:"));
        contentPanel.add(yearField);
    
        contentPanel.add(new JLabel("New Price:"));
        contentPanel.add(priceField);
    
        contentPanel.add(new JLabel("New Type:"));
        contentPanel.add(typeComboBox);
    
        contentPanel.add(new JLabel("New Color:"));
        contentPanel.add(colorField);
    
        JButton loadButton = new JButton("Load Data");
        contentPanel.add(new JLabel()); // filler
        contentPanel.add(loadButton);
    
        JButton editButton = new JButton("Submit");
        contentPanel.add(new JLabel()); // filler
        contentPanel.add(editButton);
    
        editVehicleDialog.setContentPane(contentPanel);
    
        // Load data
        loadButton.addActionListener(e -> {
            try {
                int vehicleId = Integer.parseInt(vehicleIdField.getText().trim());
                DatabaseManager dbManager = new DatabaseManager();
                String query = "SELECT * FROM vehicles WHERE id = " + vehicleId;
                ResultSet rs = dbManager.runQuery(query);
    
                if (rs.next()) {
                    makeField.setText(rs.getString("make"));
                    modelField.setText(rs.getString("model"));
                    yearField.setText(String.valueOf(rs.getInt("year")));
                    priceField.setText(String.valueOf(rs.getDouble("price")));
                    typeComboBox.setSelectedItem(rs.getString("type"));
                    colorField.setText(rs.getString("color"));
                } else {
                    JOptionPane.showMessageDialog(editVehicleDialog, "Vehicle not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
    
                rs.close();
                dbManager.close();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editVehicleDialog, "Please enter a valid numeric Vehicle ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(editVehicleDialog, "Error loading vehicle data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    editButton.addActionListener(e -> {
        try {
            int vehicleId = Integer.parseInt(vehicleIdField.getText().trim());
            String make = makeField.getText().trim();
            String model = modelField.getText().trim();
            String yearText = yearField.getText().trim();
            String priceText = priceField.getText().trim();
            String type = (String) typeComboBox.getSelectedItem();
            String color = colorField.getText().trim();

            // Empty check
            if (make.isEmpty() || model.isEmpty() || yearText.isEmpty() || priceText.isEmpty() || color.isEmpty()) {
                JOptionPane.showMessageDialog(editVehicleDialog, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Make validation
            if (!make.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(editVehicleDialog, "Make must contain only letters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Year validation
            int year = Integer.parseInt(yearText);
            int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            if (year < 1960 || year > currentYear + 1) {
                JOptionPane.showMessageDialog(editVehicleDialog, "Year must be between 1960 and " + (currentYear + 1) + ".", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Price validation
            double price = Double.parseDouble(priceText);
            if (price < 0) {
                JOptionPane.showMessageDialog(editVehicleDialog, "Price cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update vehicle
            DatabaseManager dbManager = new DatabaseManager();
            String updateQuery = "UPDATE vehicles SET " +
                                 "make = '" + make + "', " +
                                 "model = '" + model + "', " +
                                 "year = " + year + ", " +
                                 "price = " + price + ", " +
                                 "type = '" + type + "', " +
                                 "color = '" + color + "' " +
                                 "WHERE id = " + vehicleId;
            dbManager.runInsert(updateQuery);
            dbManager.close();

            JOptionPane.showMessageDialog(editVehicleDialog, "Vehicle updated successfully.");
            editVehicleDialog.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(editVehicleDialog, "Please enter valid numeric values for Vehicle ID, Year, and Price.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(editVehicleDialog, "Error updating vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    editVehicleDialog.setVisible(true);
}
    public void showDeleteVehicleDialog() {
        JDialog deleteVehicleDialog = new JDialog(this, "Delete Vehicle", true);
        deleteVehicleDialog.setSize(400, 250);
        deleteVehicleDialog.setLocationRelativeTo(this);
        //spacing
        ((JComponent) deleteVehicleDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    
        JPanel contentPanel = new JPanel(new GridLayout(0, 2, 20, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JTextField vehicleIdField = new JTextField();
        contentPanel.add(new JLabel("Vehicle ID:"));
        contentPanel.add(vehicleIdField);
    
        JButton deleteButton = new JButton("Delete");
        contentPanel.add(new JLabel()); // filler
        contentPanel.add(deleteButton);
    
        deleteVehicleDialog.setContentPane(contentPanel);
    
        // Delete vehicle action
        deleteButton.addActionListener(e -> {
            try {
                int vehicleId = Integer.parseInt(vehicleIdField.getText().trim());
    
                DatabaseManager dbManager = new DatabaseManager();
    
                // Fetch vehicle details
                String fetchQuery = "SELECT make, model, year, price, type, color FROM vehicles WHERE id = " + vehicleId;
                ResultSet rs = dbManager.runQuery(fetchQuery);
    
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(deleteVehicleDialog, "Vehicle not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    dbManager.close();
                    return;
                }
    
                // Build confirmation message
                String vehicleDetails = "Make: " + rs.getString("make") +
                                        "\nModel: " + rs.getString("model") +
                                        "\nYear: " + rs.getInt("year") +
                                        "\nPrice: $" + rs.getDouble("price") +
                                        "\nType: " + rs.getString("type") +
                                        "\nColor: " + rs.getString("color");
    
                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(deleteVehicleDialog,
                        "Are you sure you want to delete this vehicle?\n\n" + vehicleDetails,
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    
                if (confirm != JOptionPane.YES_OPTION) {
                    dbManager.close();
                    return;
                }
    
                // Delete vehicle from database
                String deleteQuery = "DELETE FROM vehicles WHERE id = " + vehicleId;
                int rowsAffected = dbManager.runInsert(deleteQuery);
                dbManager.close();
    
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(deleteVehicleDialog, "Vehicle deleted successfully.");
                    deleteVehicleDialog.dispose();
                    showViewVehiclesDialog((DefaultTableModel) vehicleTable.getModel());
                } else {
                    JOptionPane.showMessageDialog(deleteVehicleDialog, "Error deleting vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(deleteVehicleDialog, "Error deleting vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    
        deleteVehicleDialog.setVisible(true);
    }

    public void showViewVehiclesDialog(DefaultTableModel model) {
    // Clear existing rows
    model.setRowCount(0);

    // Fetch vehicles from the database excluding sold ones
    try {
        DatabaseManager dbManager = new DatabaseManager();
        String query = "SELECT id, make, model, year, price, type, color FROM vehicles WHERE sold = 0";
        ResultSet resultSet = dbManager.runQuery(query);

        // Populate the table with data
        while (resultSet.next()) {
            int vehicleId = resultSet.getInt("id");
            String make = resultSet.getString("make");
            String modelStr = resultSet.getString("model");
            int year = resultSet.getInt("year");
            double price = resultSet.getDouble("price");
            String type = resultSet.getString("type");
            String color = resultSet.getString("color");
            model.addRow(new Object[]{vehicleId, make, modelStr, year, price, type, color});   
        }

        dbManager.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching vehicles.");
    }
}

public void showFilterDialog(JTable vehicleTable) {
    JDialog filterDialog = new JDialog(this, "Filter Vehicles", true);
    filterDialog.setSize(600, 500); // Increased height to accommodate date filters
    filterDialog.setLayout(new GridBagLayout());
    filterDialog.setLocationRelativeTo(this);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Budget (price) filter
    JCheckBox budgetCheckBox = new JCheckBox("Budget");
    JTextField minBudgetField = new JTextField();
    JTextField maxBudgetField = new JTextField();
    minBudgetField.setPreferredSize(new Dimension(100, 25));
    maxBudgetField.setPreferredSize(new Dimension(100, 25));

    gbc.gridx = 0;
    gbc.gridy = 0;
    filterDialog.add(budgetCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Min Budget:"), gbc);
    gbc.gridx = 2;
    filterDialog.add(minBudgetField, gbc);
    gbc.gridx = 3;
    filterDialog.add(new JLabel("Max Budget:"), gbc);
    gbc.gridx = 4;
    filterDialog.add(maxBudgetField, gbc);

    // Type filter using JComboBox
    JCheckBox typeCheckBox = new JCheckBox("Type");
    String[] typeOptions = {"All", "Car", "Motorcycle"};
    JComboBox<String> typeComboBox = new JComboBox<>(typeOptions);
    typeComboBox.setPreferredSize(new Dimension(200, 25));

    gbc.gridx = 0;
    gbc.gridy = 1;
    filterDialog.add(typeCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Type:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(typeComboBox, gbc);
    gbc.gridwidth = 1;

    // Color filter remains unchanged
    JCheckBox colorCheckBox = new JCheckBox("Color");
    JTextField colorField = new JTextField();
    colorField.setPreferredSize(new Dimension(200, 25));

    gbc.gridx = 0;
    gbc.gridy = 2;
    filterDialog.add(colorCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Color:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(colorField, gbc);
    gbc.gridwidth = 1;

    // Make filter remains unchanged
    JCheckBox brandCheckBox = new JCheckBox("Make");
    JTextField brandField = new JTextField();
    brandField.setPreferredSize(new Dimension(200, 25));

    gbc.gridx = 0;
    gbc.gridy = 3;
    filterDialog.add(brandCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Make:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(brandField, gbc);
    gbc.gridwidth = 1;

    // Year filter using JSpinners
    JCheckBox dateCheckBox = new JCheckBox("Model Year");
    int currentYear = Year.now().getValue();
    
    SpinnerNumberModel fromYearModel = new SpinnerNumberModel(currentYear - 10, 1900, currentYear, 1);
    SpinnerNumberModel toYearModel = new SpinnerNumberModel(currentYear, 1900, currentYear + 1, 1);

    JSpinner fromYearSpinner = new JSpinner(fromYearModel);
    JSpinner.NumberEditor fromEditor = new JSpinner.NumberEditor(fromYearSpinner, "####");
    fromYearSpinner.setEditor(fromEditor);

    JSpinner toYearSpinner = new JSpinner(toYearModel);
    JSpinner.NumberEditor toEditor = new JSpinner.NumberEditor(toYearSpinner, "####");
    toYearSpinner.setEditor(toEditor);

    // Add to layout
    gbc.gridx = 0;
    gbc.gridy = 4;
    filterDialog.add(dateCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("From:"), gbc);
    gbc.gridx = 2;
    filterDialog.add(fromYearSpinner, gbc);
    gbc.gridx = 3;
    filterDialog.add(new JLabel("To:"), gbc);
    gbc.gridx = 4;
    filterDialog.add(toYearSpinner, gbc);

    // Apply button
    JButton applyButton = new JButton("Apply Filters");
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 5;
    gbc.anchor = GridBagConstraints.CENTER;
    filterDialog.add(applyButton, gbc);

    applyButton.addActionListener(e -> {
             // Apply filters based on selected checkboxes
             double minBudget = minBudgetField.getText().isEmpty() ? 0 : Double.parseDouble(minBudgetField.getText());
             double maxBudget = maxBudgetField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxBudgetField.getText());
             String type = (String) typeComboBox.getSelectedItem();
             String color = colorField.getText();
             String brand = brandField.getText();
             Integer fromYear = dateCheckBox.isSelected() ? (Integer) fromYearSpinner.getValue() : null;
             Integer toYear = dateCheckBox.isSelected() ? (Integer) toYearSpinner.getValue() : null;
             
             applyFilters(vehicleTable, budgetCheckBox.isSelected(), minBudget, maxBudget, typeCheckBox.isSelected(), type, colorCheckBox.isSelected(), color, brandCheckBox.isSelected(), brand, dateCheckBox.isSelected(), fromYear, toYear);
             filterDialog.dispose();
         });

    filterDialog.setVisible(true);
}

private void applyFilters(JTable vehicleTable, boolean budget, double minBudget, double maxBudget, boolean type, String typeValue, boolean color, String colorValue, boolean brand, String brandValue, boolean date, Integer fromYear, Integer toYear) {
    DefaultTableModel model = (DefaultTableModel) vehicleTable.getModel();
    model.setRowCount(0); // Clear existing rows

    // Build the SQL query with a condition to only include vehicles that are not sold
    StringBuilder queryBuilder = new StringBuilder("SELECT id, make, model, year, price, type, color FROM vehicles WHERE sold = 0");

    if (budget) {
        queryBuilder.append(" AND price BETWEEN ").append(minBudget).append(" AND ").append(maxBudget);
    }
    if (type) {
        queryBuilder.append(" AND type = '").append(typeValue).append("'");
    }
    if (color) {
        queryBuilder.append(" AND color = '").append(colorValue).append("'");
    }
    if (brand) {
        queryBuilder.append(" AND make = '").append(brandValue).append("'");
    }
    
    if (date) {
        if (fromYear != null && toYear != null) {
            queryBuilder.append(" AND year BETWEEN ").append(fromYear).append(" AND ").append(toYear);
        } else if (fromYear != null) {
            queryBuilder.append(" AND year >= ").append(fromYear);
        } else if (toYear != null) {
            queryBuilder.append(" AND year <= ").append(toYear);
        }
    }

    String query = queryBuilder.toString();

    // Fetch filtered vehicles from the database
    try {
        DatabaseManager dbManager = new DatabaseManager();
        ResultSet resultSet = dbManager.runQuery(query);

        // Populate the table with data
        while (resultSet.next()) {
            int vehicleId = resultSet.getInt("id");
            String make = resultSet.getString("make");
            String modelStr = resultSet.getString("model");
            int year = resultSet.getInt("year");
            double price = resultSet.getDouble("price");
            String typeStr = resultSet.getString("type");
            String colorStr = resultSet.getString("color");
            model.addRow(new Object[]{vehicleId, make, modelStr, year, price, typeStr, colorStr});
        }

        dbManager.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching vehicles.");
    }

}

public void showAddUserDialog() {
    JDialog addUserDialog = new JDialog(this, "Add User", true);
    addUserDialog.setSize(400, 250);
    addUserDialog.setLocationRelativeTo(this);
    ((JComponent) addUserDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel contentPanel = new JPanel(new GridLayout(0, 2, 20, 15));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    contentPanel.add(new JLabel("Username:"));
    JTextField usernameField = new JTextField();
    contentPanel.add(usernameField);

    contentPanel.add(new JLabel("Password:"));
    JTextField passwordField = new JTextField("TempPass"); // Default visible text
    passwordField.setEditable(false); // Prevent editing
    contentPanel.add(passwordField);

    contentPanel.add(new JLabel("Role:"));
    JComboBox<String> roleComboBox = new JComboBox<>(new String[] {"Admin", "Manager", "Salesperson"});
    contentPanel.add(roleComboBox);

    JButton addButton = new JButton("Add");
    contentPanel.add(new JLabel()); // filler to align button to the right
    contentPanel.add(addButton);

    addUserDialog.setContentPane(contentPanel);

    addButton.addActionListener(e -> {
        String username = usernameField.getText().trim();
        String password = "TempPass"; // Always TempPass
        String role = roleComboBox.getSelectedItem().toString();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(addUserDialog,
                    "Username cannot be empty.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DatabaseManager dbManager = new DatabaseManager();
            
            // Check if username already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE name = '" + username + "'";
            ResultSet rs = dbManager.runQuery(checkQuery);
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                // Username already exists
                JOptionPane.showMessageDialog(addUserDialog,
                        "Username already exists.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add user to the database
            String query = "INSERT INTO users (name, password, roleId, isTemp) VALUES ('" 
                         + username + "', '" + password + "', (SELECT id FROM roles WHERE role = '" + role + "' LIMIT 1), 1)";
            dbManager.runInsert(query);
            dbManager.close();
            JOptionPane.showMessageDialog(this, "User added successfully.");
            addUserDialog.dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding user.");
        }
    });

    addUserDialog.setVisible(true);
}

public void showEditUserDialog() {
    JDialog editUserDialog = new JDialog(this, "Edit User", true);
    editUserDialog.setSize(600, 300);
    editUserDialog.setLocationRelativeTo(this);
    ((JComponent) editUserDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Create a panel with padding and GridLayout
    JPanel contentPanel = new JPanel(new GridLayout(0, 2, 20, 15));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // top, left, bottom, right

    // Create components
    contentPanel.add(new JLabel("User ID:"));
    JTextField userIdField = new JTextField();
    contentPanel.add(userIdField);

    contentPanel.add(new JLabel("New Username:"));
    JTextField usernameField = new JTextField();
    contentPanel.add(usernameField);

    contentPanel.add(new JLabel("New Password:"));
    JPasswordField passwordField = new JPasswordField();
    contentPanel.add(passwordField);

    contentPanel.add(new JLabel("New Role:"));
    JComboBox<String> roleComboBox = new JComboBox<>(new String[] {"Admin", "Manager", "Salesperson"});
    contentPanel.add(roleComboBox);

    JButton loadButton = new JButton("Load Data");
    contentPanel.add(loadButton);

    JButton editButton = new JButton("Submit");
    contentPanel.add(editButton);

    // Add content panel to dialog
    editUserDialog.setContentPane(contentPanel);
    
    loadButton.addActionListener(e -> {
        try {
            int userId = Integer.parseInt(userIdField.getText().trim());
            DatabaseManager dbManager = new DatabaseManager();
            String query = "SELECT u.name, r.role FROM users u " +
                           "JOIN roles r ON u.roleId = r.id WHERE u.id = " + userId;
            ResultSet rs = dbManager.runQuery(query);

            if (rs.next()) {
                usernameField.setText(rs.getString("name"));
                passwordField.setText("TempPass"); // Pre-set to TempPass
                roleComboBox.setSelectedItem(rs.getString("role"));
                userIdField.setEditable(false);
            } else {
                usernameField.setText("");
                roleComboBox.setSelectedIndex(0);
                JOptionPane.showMessageDialog(this, "User ID not found.");
            }
            rs.close();
            dbManager.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching user data.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid User ID format.");
        }
    });

    // Handle the edit action when the "Edit" button is clicked
    editButton.addActionListener(e -> {
        String username = usernameField.getText().trim();
        String password = "TempPass"; // Always set password to TempPass
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            DatabaseManager dbManager = new DatabaseManager();
            int userId = Integer.parseInt(userIdField.getText().trim());
            
            // Check if username already exists, excluding the current user
            String checkQuery = "SELECT COUNT(*) FROM users WHERE name = '" + username + "' AND id != " + userId;
            ResultSet rs = dbManager.runQuery(checkQuery);
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                // Username already exists
                JOptionPane.showMessageDialog(editUserDialog,
                        "Username already exists.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Proceed with updating the user information
            String query = "UPDATE users SET name = '" + username + "', password = '" + password +
                           "', roleId = (SELECT id FROM roles WHERE role = '" + role + "'), isTemp = 1 WHERE id = " + userId;
            dbManager.runInsert(query);
            dbManager.close();

            // Show a dialog notifying that the password has been reset
            JOptionPane.showMessageDialog(this, "User edited successfully.\nPassword has been reset to TempPass.", 
                                          "Password Reset", JOptionPane.INFORMATION_MESSAGE);
            editUserDialog.dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error editing user.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid User ID format.");
        }
    });

    editUserDialog.setVisible(true);
}


public void showDeleteUserDialog() {
    JDialog deleteUserDialog = new JDialog(this, "Delete User", true);
    deleteUserDialog.setSize(400, 200);
    deleteUserDialog.setLocationRelativeTo(this);
    ((JComponent) deleteUserDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


    JPanel panel = new JPanel(new GridLayout(0, 2, 20, 15));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    panel.add(new JLabel("User ID:"));
    JTextField userIdField = new JTextField();
    panel.add(userIdField);

    JButton deleteButton = new JButton("Delete");
    panel.add(new JLabel()); // Filler
    panel.add(deleteButton);

    deleteUserDialog.setContentPane(panel);

    deleteButton.addActionListener(e -> {
        String input = userIdField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(deleteUserDialog, "User ID cannot be empty.");
            return;
        }

        try {
            int userId = Integer.parseInt(input);

            int confirm = JOptionPane.showConfirmDialog(
                deleteUserDialog,
                "Are you sure you want to delete user with ID: " + userId + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                DatabaseManager dbManager = new DatabaseManager();
                String query = "DELETE FROM users WHERE id = " + userId;
                int rowsAffected = dbManager.runInsert(query);
                dbManager.close();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully.");
                    deleteUserDialog.dispose();
                    showViewUsersDialog((DefaultTableModel) userTable.getModel());
                } else {
                    JOptionPane.showMessageDialog(this, "User not found.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(deleteUserDialog, "Invalid User ID. Please enter a valid number.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting user.");
        }
    });

    deleteUserDialog.setVisible(true);
}

public void showViewUsersDialog(DefaultTableModel model) {
    // Clear existing rows
    model.setRowCount(0);

    // Fetch users from the database
    try {
        DatabaseManager dbManager = new DatabaseManager();
        String query = "SELECT users.id, users.name, roles.role, users.isTemp FROM users JOIN roles ON users.roleId = roles.id order by users.id";
        ResultSet resultSet = dbManager.runQuery(query);

        // Populate the table with data
        while (resultSet.next()) {
            int userId = resultSet.getInt("id");
            String username = resultSet.getString("name");
            String role = resultSet.getString("role");
            String isTemp = (1 == resultSet.getInt("isTemp")) ? "Yes" : "No";            
            model.addRow(new Object[]{userId, username, role, isTemp});
        }

        dbManager.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching users.");
    }
}

    // The main method remains unchanged.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard("Admin").setVisible(true);
        });
    }

    // Method to show the Sales History Filter Dialog.
public void showSalesHistoryFilterDialog(JTable salesTable) {
    JDialog filterDialog = new JDialog(this, "Filter Sales History", true);
    filterDialog.setSize(600, 500);
    filterDialog.setLayout(new GridBagLayout());
    filterDialog.setLocationRelativeTo(this);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Sale ID filter
    JCheckBox saleIdCheckBox = new JCheckBox("Sale ID");
    JTextField saleIdField = new JTextField();
    saleIdField.setPreferredSize(new Dimension(200, 25));
    
    gbc.gridx = 0;
    gbc.gridy = 0;
    filterDialog.add(saleIdCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Sale ID:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(saleIdField, gbc);
    gbc.gridwidth = 1;

    // Vehicle ID filter
    JCheckBox vehicleIdCheckBox = new JCheckBox("Vehicle ID");
    JTextField vehicleIdField = new JTextField();
    vehicleIdField.setPreferredSize(new Dimension(200, 25));
    
    gbc.gridx = 0;
    gbc.gridy = 1;
    filterDialog.add(vehicleIdCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Vehicle ID:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(vehicleIdField, gbc);
    gbc.gridwidth = 1;

    // Customer filter
    JCheckBox customerCheckBox = new JCheckBox("Customer");
    JTextField customerField = new JTextField();
    customerField.setPreferredSize(new Dimension(200, 25));
    
    gbc.gridx = 0;
    gbc.gridy = 2;
    filterDialog.add(customerCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Customer:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(customerField, gbc);
    gbc.gridwidth = 1;

    // Price filter
    JCheckBox priceCheckBox = new JCheckBox("Price");
    JTextField minPriceField = new JTextField();
    JTextField maxPriceField = new JTextField();
    minPriceField.setPreferredSize(new Dimension(100, 25));
    maxPriceField.setPreferredSize(new Dimension(100, 25));
    
    gbc.gridx = 0;
    gbc.gridy = 3;
    filterDialog.add(priceCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Min Price:"), gbc);
    gbc.gridx = 2;
    filterDialog.add(minPriceField, gbc);
    gbc.gridx = 3;
    filterDialog.add(new JLabel("Max Price:"), gbc);
    gbc.gridx = 4;
    filterDialog.add(maxPriceField, gbc);

    // Type filter
    JCheckBox typeCheckBox = new JCheckBox("Type");
    String[] typeOptions = {"All", "Car", "Motorcycle"};
    JComboBox<String> typeComboBox = new JComboBox<>(typeOptions);
    typeComboBox.setPreferredSize(new Dimension(200, 25));
    
    gbc.gridx = 0;
    gbc.gridy = 4;
    filterDialog.add(typeCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Type:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(typeComboBox, gbc);
    gbc.gridwidth = 1;

    // Color filter
    JCheckBox colorCheckBox = new JCheckBox("Color");
    JTextField colorField = new JTextField();
    colorField.setPreferredSize(new Dimension(200, 25));
    
    gbc.gridx = 0;
    gbc.gridy = 5;
    filterDialog.add(colorCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Color:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(colorField, gbc);
    gbc.gridwidth = 1;

    // Model filter
    JCheckBox modelCheckBox = new JCheckBox("Model");
    JTextField modelField = new JTextField();
    modelField.setPreferredSize(new Dimension(200, 25));
    
    gbc.gridx = 0;
    gbc.gridy = 6;
    filterDialog.add(modelCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Model:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(modelField, gbc);
    gbc.gridwidth = 1;

    // Make filter
    JCheckBox makeCheckBox = new JCheckBox("Make");
    JTextField makeField = new JTextField();
    makeField.setPreferredSize(new Dimension(200, 25));
    
    gbc.gridx = 0;
    gbc.gridy = 7;
    filterDialog.add(makeCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Make:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(makeField, gbc);
    gbc.gridwidth = 1;

    // Year filter
    JCheckBox yearCheckBox = new JCheckBox("Year");
    JTextField yearField = new JTextField();
    yearField.setPreferredSize(new Dimension(200, 25));
    
    gbc.gridx = 0;
    gbc.gridy = 8;
    filterDialog.add(yearCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("Year:"), gbc);
    gbc.gridx = 2;
    gbc.gridwidth = 3;
    filterDialog.add(yearField, gbc);
    gbc.gridwidth = 1;

    // Date filter
    JCheckBox dateCheckBox = new JCheckBox("Date");
    JDateChooser fromDateChooser = new JDateChooser();
    fromDateChooser.setDateFormatString("dd-MM-yyyy");
    JDateChooser toDateChooser = new JDateChooser();
    toDateChooser.setDateFormatString("dd-MM-yyyy");
    
    gbc.gridx = 0;
    gbc.gridy = 9;
    filterDialog.add(dateCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("From:"), gbc);
    gbc.gridx = 2;
    filterDialog.add(fromDateChooser, gbc);
    gbc.gridx = 3;
    filterDialog.add(new JLabel("To:"), gbc);
    gbc.gridx = 4;
    filterDialog.add(toDateChooser, gbc);

    // Apply button to trigger filtering.
    JButton applyButton = new JButton("Apply Sales History Filters");
    gbc.gridx = 0;
    gbc.gridy = 10;
    gbc.gridwidth = 5;
    gbc.anchor = GridBagConstraints.CENTER;
    filterDialog.add(applyButton, gbc);
    
    applyButton.addActionListener(e -> {
        String saleId = saleIdField.getText().trim();
        String vehicleId = vehicleIdField.getText().trim();
        String customer = customerField.getText().trim();
        double minPrice = minPriceField.getText().isEmpty() ? 0 
                            : Double.parseDouble(minPriceField.getText());
        double maxPrice = maxPriceField.getText().isEmpty() ? Double.MAX_VALUE 
                            : Double.parseDouble(maxPriceField.getText());
        String type = (String) typeComboBox.getSelectedItem();
        String color = colorField.getText().trim();
        String model = modelField.getText().trim();
        String make = makeField.getText().trim();
        String year = yearField.getText().trim();
        
        boolean dateSelected = dateCheckBox.isSelected();
        String fromDateStr = "";
        String toDateStr = "";
        if (dateSelected) {
            java.util.Date fromDate = fromDateChooser.getDate();
            java.util.Date toDate = toDateChooser.getDate();
            if (fromDate != null && toDate != null) {
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                fromDateStr = dbFormat.format(fromDate);
                toDateStr = dbFormat.format(toDate);
            } else {
                JOptionPane.showMessageDialog(filterDialog, "Please select both From and To dates.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // The applySalesHistoryFilters method below will use these values.
        applySalesHistoryFilters(salesTable, saleIdCheckBox.isSelected(), saleId,
                vehicleIdCheckBox.isSelected(), vehicleId,
                customerCheckBox.isSelected(), customer,
                priceCheckBox.isSelected(), minPrice, maxPrice,
                typeCheckBox.isSelected(), type,
                colorCheckBox.isSelected(), color,
                modelCheckBox.isSelected(), model,
                makeCheckBox.isSelected(), make,
                yearCheckBox.isSelected(), year,
                dateSelected, fromDateStr, toDateStr);
        filterDialog.dispose();
    });
    
    filterDialog.setVisible(true);
}

// Method to apply the sales history filters and update the salesTable.
private void applySalesHistoryFilters(JTable salesTable, boolean saleIdFilter, String saleId,
                                      boolean vehicleIdFilter, String vehicleId,
                                      boolean customerFilter, String customerValue,
                                      boolean priceFilter, double minPrice, double maxPrice,
                                      boolean typeFilter, String typeValue,
                                      boolean colorFilter, String colorValue,
                                      boolean modelFilter, String modelValue,
                                      boolean makeFilter, String makeValue,
                                      boolean yearFilter, String yearValue,
                                      boolean dateFilter, String fromDate, String toDate) {
    DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
    model.setRowCount(0); // Clear existing rows

    // Build the SQL query by joining the sales and vehicles tables.
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT s.id AS saleId, s.vehicleId, v.make, v.model, v.type, v.color, ");
    queryBuilder.append("s.customerName, s.price, s.date, v.year AS vehicleYear ");
    queryBuilder.append("FROM sales s JOIN vehicles v ON s.vehicleId = v.id ");
    queryBuilder.append("WHERE 1=1 "); // Base condition

    if (saleIdFilter && saleId != null && !saleId.trim().isEmpty()) {
        queryBuilder.append("AND s.id = ").append(saleId).append(" ");
    }
    if (vehicleIdFilter && vehicleId != null && !vehicleId.trim().isEmpty()) {
        queryBuilder.append("AND s.vehicleId = ").append(vehicleId).append(" ");
    }
    if (customerFilter && customerValue != null && !customerValue.trim().isEmpty()) {
        queryBuilder.append("AND s.customerName LIKE '%").append(customerValue).append("%' ");
    }
    if (priceFilter) {
        queryBuilder.append("AND s.price BETWEEN ").append(minPrice)
                    .append(" AND ").append(maxPrice).append(" ");
    }
    if (typeFilter && typeValue != null && !typeValue.equals("All")) {
        queryBuilder.append("AND v.type = '").append(typeValue).append("' ");
    }
    if (colorFilter && colorValue != null && !colorValue.trim().isEmpty()) {
        queryBuilder.append("AND v.color LIKE '%").append(colorValue).append("%' ");
    }
    if (modelFilter && modelValue != null && !modelValue.trim().isEmpty()) {
        queryBuilder.append("AND v.model LIKE '%").append(modelValue).append("%' ");
    }
    if (makeFilter && makeValue != null && !makeValue.trim().isEmpty()) {
        queryBuilder.append("AND v.make LIKE '%").append(makeValue).append("%' ");
    }
    if (yearFilter && yearValue != null && !yearValue.trim().isEmpty()) {
        queryBuilder.append("AND v.year = ").append(yearValue).append(" ");
    }
    if (dateFilter && !fromDate.isEmpty() && !toDate.isEmpty()) {
        queryBuilder.append("AND s.date BETWEEN '").append(fromDate)
                    .append("' AND '").append(toDate).append("' ");
    }

    String query = queryBuilder.toString();
    System.out.println("Sales History Query: " + query); // Debug print to verify query

    try {
        DatabaseManager dbManager = new DatabaseManager();
        ResultSet rs = dbManager.runQuery(query);

        while (rs.next()) {
            int saleIdResult = rs.getInt("saleId");
            int vehicleIdResult = rs.getInt("vehicleId");
            String make = rs.getString("make");
            String modelStr = rs.getString("model");
            String type = rs.getString("type");
            String color = rs.getString("color");
            String customerName = rs.getString("customerName");
            double salePrice = rs.getDouble("price");
            String saleDate = rs.getString("date");
            int year = rs.getInt("vehicleYear");
            
            model.addRow(new Object[]{saleIdResult, vehicleIdResult, make, modelStr, type, color, customerName, saleDate, salePrice, year});
        }
        dbManager.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching sales history.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

public JPanel createInfoCard(String title, String value) {
    JPanel card = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();
            int arc = 20;
            GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 240, 255),
                    0, height, new Color(220, 220, 245));
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, width - 1, height - 1, arc, arc);
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(new Color(150, 150, 170));
            g2d.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);
        }
    };
    card.setPreferredSize(new Dimension(220, 160));
    card.setLayout(new BorderLayout());
    
    JPanel contentPanel = new JPanel();
    contentPanel.setOpaque(false);
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
    
    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    titleLabel.setForeground(new Color(70, 70, 70));
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
    valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
    valueLabel.setForeground(new Color(80, 80, 80));
    valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    contentPanel.add(Box.createVerticalGlue());
    contentPanel.add(titleLabel);
    contentPanel.add(Box.createVerticalStrut(8));
    contentPanel.add(valueLabel);
    contentPanel.add(Box.createVerticalGlue());
    
    card.add(contentPanel, BorderLayout.CENTER);
    
    card.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            card.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 2));
        }
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            card.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 2));
        }
    });
    
    return card;
}
}
