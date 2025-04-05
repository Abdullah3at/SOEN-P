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

/*
 * This is the main dashboard frame for role-based access to the dealership system application.
 * 
 */

public class AdminDashboard extends JFrame implements ActionListener {
    //user interface components for sidebar actions
    private JButton userManagementButton, inventoryManagementButton, salesHistoryButton, viewDealershipButton, sellVehicleButton, logoutButton, quitButton;
    private JLabel roleLabel; //display current logged in user role
    private String role; //current role
    private JPanel mainPanel, sidebar;
    private CardLayout cardLayout; //card layout to switch between the panels dynamically
    Dealership dealership;
    private JButton selectedButton; // currently selected sidebar button
    public JTable userTable, vehicleTable, salesTable;
    
    //default constructor for the dashboard (admin)
    public AdminDashboard() {
        this("Admin");
    }
    
    //constructor for the dashboard component with role-based parameter
    public AdminDashboard(String role) {
        this.role = role;
        setTitle(role + " Dashboard"); //shows what's the role of the current user logged in
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        

        
        //build the sidebar based on role
        sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1, 10, 10));
        sidebar.setBackground(new Color(35, 45, 65));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        
        //display the role of the user in the top left corner
        roleLabel = new JLabel("Role: " + role, SwingConstants.CENTER);
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        sidebar.add(roleLabel);
        
        //if the logged in user is Admin: show all the features
        if(role.equalsIgnoreCase("Admin")) {
            userManagementButton = createSidebarButton("User Management", new ImageIcon("images/user.png"));
            sidebar.add(userManagementButton);
            
            inventoryManagementButton = createSidebarButton("Inventory Management", new ImageIcon("images/icon.jpg"));
            sidebar.add(inventoryManagementButton);
            
            salesHistoryButton = createSidebarButton("Sales History", new ImageIcon("images/sales.png"));
            sidebar.add(salesHistoryButton);
            
            viewDealershipButton = createSidebarButton("View Dealership", new ImageIcon("images/dealership.png"));
            sidebar.add(viewDealershipButton);
            
            sellVehicleButton = createSidebarButton("Sell Vehicle", new ImageIcon("images/sell.png"));
            sidebar.add(sellVehicleButton);
        }

        //if the logged in user is Manager: show everything except User Management
        else if(role.equalsIgnoreCase("Manager")) {
            inventoryManagementButton = createSidebarButton("Inventory Management", new ImageIcon("images/inventory.png"));
            sidebar.add(inventoryManagementButton);
            
            salesHistoryButton = createSidebarButton("Sales History", new ImageIcon("images/sales.png"));
            sidebar.add(salesHistoryButton);
            
            viewDealershipButton = createSidebarButton("View Dealership", new ImageIcon("images/dealership.png"));
            sidebar.add(viewDealershipButton);
            
            sellVehicleButton = createSidebarButton("Sell Vehicle", new ImageIcon("images/sell.png"));
            sidebar.add(sellVehicleButton);
        }

        //if the logged in user is Salesperson: show cars in Inventory, Sell Vehicle, and Sales History
        else if(role.equalsIgnoreCase("Salesperson")) {
            inventoryManagementButton = createSidebarButton("Inventory Management", new ImageIcon("images/inventory.png"));
            sidebar.add(inventoryManagementButton);
            
            salesHistoryButton = createSidebarButton("Sales History", new ImageIcon("images/sales.png"));
            sidebar.add(salesHistoryButton);
            
            sellVehicleButton = createSidebarButton("Sell Vehicle", new ImageIcon("images/sell.png"));
            sidebar.add(sellVehicleButton);
        }
        
        //common options for all roles
        logoutButton = createSidebarButton("Logout", new ImageIcon("images/logout.png"));
        sidebar.add(logoutButton);
        quitButton = createSidebarButton("Quit", new ImageIcon("images/quit.png"));
        sidebar.add(quitButton);
        
        //create the main panel with CardLayout to load separate panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.DARK_GRAY);
        
        /*adding panels based on roles – can be modified in case there's a need to add or remove functionalities
        for each role, for example, Inventory Management Panel for Salesperson can be modified to display only cars.
        */
        if(role.equalsIgnoreCase("Admin")) {
            mainPanel.add(new UserManagementPanel(this), "User Management");
        }
        mainPanel.add(new InventoryManagementPanel(this), "Inventory Management");
        mainPanel.add(new SalesHistoryPanel(this), "Sales History");
        mainPanel.add(new ViewDealershipPanel(this), "View Dealership");
        mainPanel.add(new SellVehiclePanel(), "Sell Vehicle");
        
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        
    //set default selection for the panel that the user sees after logs in
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
        button.setFont(new Font("Verdana", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.addActionListener(this);
        //hover effects for the buttons
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
    
    //highlight selected button with lighter color
    public void setSelectedButton(JButton selectedButton) {
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
    
    //handle button actions (regular click or logout)
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
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
    
    //the following methods are kept in AdminDashboard to be called by panel components.
    public void showAddVehicleDialog() {
        JDialog addVehicleDialog = new JDialog(this, "Add Vehicle", true);
        addVehicleDialog.setSize(400, 400);
        addVehicleDialog.setLayout(new GridLayout(0, 2, 10, 10));
        addVehicleDialog.setLocationRelativeTo(this);
        //added spacing between the text and the borders
        //((JComponent) addVehicleDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        //adding car details
        addVehicleDialog.add(new JLabel("Make:"));
        JTextField makeField = new JTextField();
        addVehicleDialog.add(makeField);
        
    
        addVehicleDialog.add(new JLabel("Model:"));
        JTextField modelField = new JTextField();
        addVehicleDialog.add(modelField);
    
        addVehicleDialog.add(new JLabel("Year:"));
        JTextField yearField = new JTextField();
        addVehicleDialog.add(yearField);
    
        addVehicleDialog.add(new JLabel("Price:"));
        JTextField priceField = new JTextField();
        addVehicleDialog.add(priceField);
    
        addVehicleDialog.add(new JLabel("Type:"));
        //dropdown - JComboBox for type with options "Car" or "Motorcycle"
        String[] types = {"Car", "Motorcycle"};
        JComboBox<String> typeComboBox = new JComboBox<>(types);
        addVehicleDialog.add(typeComboBox);
    
        addVehicleDialog.add(new JLabel("Color:"));
        JTextField colorField = new JTextField();
        addVehicleDialog.add(colorField);
    
        JButton addButton = new JButton("Add");
        addVehicleDialog.add(addButton);
    
        addButton.addActionListener(e -> {
            String make = makeField.getText().trim();
            //validate data type for make (String)
            if (!make.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Make must contain only letters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String model = modelField.getText().trim();
            String yearText = yearField.getText().trim();
            String priceText = priceField.getText().trim();
            String type = (String) typeComboBox.getSelectedItem();
            String color = colorField.getText().trim();
    
            //verify that all the required fields are filled
            if (make.isEmpty() || model.isEmpty() || yearText.isEmpty() || priceText.isEmpty() || color.isEmpty()) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            int year;
            double price;
            //validate year
            try {
                year = Integer.parseInt(yearText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Year must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            //make sure year cannot be less than 1960
            if (year < 1960) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Year cannot be less than 1960.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //year cannot be in the future, can't exceed current date
            if (year > currentYear) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Year cannot exceed the current year (" + currentYear + ").", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            //validate price - must be above 1
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Price must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (price < 1) {
                JOptionPane.showMessageDialog(addVehicleDialog, "Invalid price.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            //add vehicle to the database
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
    editVehicleDialog.setLayout(new GridLayout(0, 2, 10, 10));
    editVehicleDialog.setLocationRelativeTo(this);
    //added spacing between the text and the borders
    //((JComponent) addVehicleDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


    editVehicleDialog.add(new JLabel("Vehicle ID:"));
    JTextField vehicleIdField = new JTextField();
    editVehicleDialog.add(vehicleIdField);

    editVehicleDialog.add(new JLabel("New Make:"));
    JTextField makeField = new JTextField();
    editVehicleDialog.add(makeField);

    editVehicleDialog.add(new JLabel("New Model:"));
    JTextField modelField = new JTextField();
    editVehicleDialog.add(modelField);

    editVehicleDialog.add(new JLabel("New Year:"));
    JTextField yearField = new JTextField();
    editVehicleDialog.add(yearField);

    editVehicleDialog.add(new JLabel("New Price:"));
    JTextField priceField = new JTextField();
    editVehicleDialog.add(priceField);

    editVehicleDialog.add(new JLabel("New Type:"));
    String[] types = {"Car", "Motorcycle"};
    JComboBox<String> typeComboBox = new JComboBox<>(types);
    editVehicleDialog.add(typeComboBox);

    editVehicleDialog.add(new JLabel("New Color:"));
    JTextField colorField = new JTextField();
    editVehicleDialog.add(colorField);
    
    JButton loadButton = new JButton("Load Data");
    editVehicleDialog.add(loadButton);  
    // editVehicleDialog.add(new JLabel("")); 

    JButton editButton = new JButton("Submit");
    editVehicleDialog.add(editButton);

    loadButton.addActionListener(e -> {
        try {
            int vehicleId = Integer.parseInt(vehicleIdField.getText());
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
        int vehicleId = Integer.parseInt(vehicleIdField.getText());
        String make = makeField.getText();
        String model = modelField.getText();
        int year = Integer.parseInt(yearField.getText());
        double price = Double.parseDouble(priceField.getText());
        String type = (String) typeComboBox.getSelectedItem();
        String color = colorField.getText();

        // Check for empty fields
        if (make.isEmpty() || model.isEmpty() || yearField.getText().isEmpty() ||
            priceField.getText().isEmpty() || color.isEmpty()) {
            JOptionPane.showMessageDialog(editVehicleDialog, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //validate Price
        if (price < 0) {
            JOptionPane.showMessageDialog(editVehicleDialog, "Price cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //validate make
        if (!make.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(editVehicleDialog, "Make must contain only letters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //validate year
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        if (year < 1960 || year > currentYear+1) { // Sometimes the "model year" is one year in the future, with new cars
            JOptionPane.showMessageDialog(editVehicleDialog, "Year must be between 1960 and " + (currentYear+1) + ".", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //edit vehicle in the database - update details
        try {
            DatabaseManager dbManager = new DatabaseManager();
            String query = "UPDATE vehicles SET make = '" + make + "', model = '" + model + 
                           "', year = " + year + ", price = " + price + ", type = '" + type + 
                           "', color = '" + color + "' WHERE id = " + vehicleId;
            dbManager.runInsert(query);
            dbManager.close();
            JOptionPane.showMessageDialog(editVehicleDialog, "Vehicle edited successfully.");
            editVehicleDialog.dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(editVehicleDialog, "Error editing vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(editVehicleDialog, "Please enter valid numeric values for Vehicle ID, Year, and Price.", "Error", JOptionPane.ERROR_MESSAGE);
    }
});

    editVehicleDialog.setVisible(true);
}

    //method to show Deleta Vehicle feauture in Inventory Management
    public void showDeleteVehicleDialog() {
        JDialog deleteVehicleDialog = new JDialog(this, "Delete Vehicle", true);
        deleteVehicleDialog.setSize(400, 200);
        deleteVehicleDialog.setLayout(new GridLayout(0, 2, 10, 10));
        deleteVehicleDialog.setLocationRelativeTo(this);
    
        deleteVehicleDialog.add(new JLabel("Vehicle ID:"));
        JTextField vehicleIdField = new JTextField();
        deleteVehicleDialog.add(vehicleIdField);
    
        JButton deleteButton = new JButton("Delete");
        deleteVehicleDialog.add(deleteButton);
    
        /*
         * after entering vehicle id, car details are fetched from the database
         * so the use can verify the details before deleting a vehicle
         */
        deleteButton.addActionListener(e -> {
            int vehicleId = Integer.parseInt(vehicleIdField.getText());
    
            //delete vehicle from the database
            try {
                DatabaseManager dbManager = new DatabaseManager();
        
                // fetch vehicle details
                String fetchQuery = "SELECT make, model, year, price, type, color FROM vehicles WHERE id = " + vehicleId;
                ResultSet rs = dbManager.runQuery(fetchQuery);
        
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Vehicle not found.");
                    dbManager.close();
                    return;
                }
        
                //build confirmation message with the vehicle details
                String vehicleDetails = "Make: " + rs.getString("make") +
                                        "\nModel: " + rs.getString("model") +
                                        "\nYear: " + rs.getInt("year") +
                                        "\nPrice: $" + rs.getDouble("price") +
                                        "\nType: " + rs.getString("type") +
                                        "\nColor: " + rs.getString("color");
        
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this vehicle?\n\n" + vehicleDetails,
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);
        
                if (confirm != JOptionPane.YES_OPTION) {
                    dbManager.close();
                    return;
                }
        
                String deleteQuery = "DELETE FROM vehicles WHERE id = " + vehicleId;
                int rowsAffected = dbManager.runInsert(deleteQuery);
                dbManager.close();
        
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Vehicle deleted successfully.");
                    deleteVehicleDialog.dispose();
                    showViewVehiclesDialog((DefaultTableModel) vehicleTable.getModel());
                } else {
                    JOptionPane.showMessageDialog(this, "Vehicle not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting vehicle.");
            }
        });
        deleteVehicleDialog.setVisible(true);
    }

    public void showViewVehiclesDialog(DefaultTableModel model) {
    //clear existing rows
    model.setRowCount(0);

    //fetch vehicles from the database excluding sold ones
    try {
        DatabaseManager dbManager = new DatabaseManager();
        String query = "SELECT id, make, model, year, price, type, color FROM vehicles WHERE sold = 0";
        ResultSet resultSet = dbManager.runQuery(query);

        //populate the table with data
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

    //color filter remains unchanged
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

    //Make filter remains unchanged
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

    //date filter: using JDateChoosers for from and to dates
    JCheckBox dateCheckBox = new JCheckBox("Date");
    JDateChooser fromDateChooser = new JDateChooser();
    fromDateChooser.setDateFormatString("dd-MM-yyyy");
    JDateChooser toDateChooser = new JDateChooser();
    toDateChooser.setDateFormatString("dd-MM-yyyy");

    gbc.gridx = 0;
    gbc.gridy = 4;
    filterDialog.add(dateCheckBox, gbc);
    gbc.gridx = 1;
    filterDialog.add(new JLabel("From:"), gbc);
    gbc.gridx = 2;
    filterDialog.add(fromDateChooser, gbc);
    gbc.gridx = 3;
    filterDialog.add(new JLabel("To:"), gbc);
    gbc.gridx = 4;
    filterDialog.add(toDateChooser, gbc);

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
             applyFilters(vehicleTable, budgetCheckBox.isSelected(), minBudget, maxBudget, typeCheckBox.isSelected(), type, colorCheckBox.isSelected(), color, brandCheckBox.isSelected(), brand);
             filterDialog.dispose();
         });

    filterDialog.setVisible(true);
}

private void applyFilters(JTable vehicleTable, boolean budget, double minBudget, double maxBudget, boolean type, String typeValue, boolean color, String colorValue, boolean brand, String brandValue) {
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
    addUserDialog.setSize(400, 300);
    addUserDialog.setLayout(new GridLayout(0, 2, 10, 10));
    addUserDialog.setLocationRelativeTo(this);

    addUserDialog.add(new JLabel("Username:"));
    JTextField usernameField = new JTextField();
    addUserDialog.add(usernameField);

    addUserDialog.add(new JLabel("Password:"));
    JPasswordField passwordField = new JPasswordField();
    addUserDialog.add(passwordField);

    addUserDialog.add(new JLabel("Role:"));
    JTextField roleField = new JTextField();
    addUserDialog.add(roleField);

    JButton addButton = new JButton("Add");
    addUserDialog.add(addButton);

    addButton.addActionListener(e -> {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = roleField.getText().trim();

        // Validate that role is one of the allowed values.
        if (!(role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("Manager") || role.equalsIgnoreCase("Salesperson"))) {
            JOptionPane.showMessageDialog(addUserDialog,
                    "Role must be either Admin, Manager, or Salesperson.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add user to the database
        try {
            DatabaseManager dbManager = new DatabaseManager();
            String query = "INSERT INTO users (name, password, roleId) VALUES ('" 
                         + username + "', '" + password + "', (SELECT id FROM roles WHERE role = '" + role + "' LIMIT 1))";
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
    editUserDialog.setSize(400, 300);
    editUserDialog.setLayout(new GridLayout(0, 2, 10, 10));
    editUserDialog.setLocationRelativeTo(this);

    editUserDialog.add(new JLabel("User ID:"));
    JTextField userIdField = new JTextField();
    editUserDialog.add(userIdField);

    editUserDialog.add(new JLabel("New Username:"));
    JTextField usernameField = new JTextField();
    editUserDialog.add(usernameField);

    editUserDialog.add(new JLabel("New Password:"));
    JPasswordField passwordField = new JPasswordField();
    editUserDialog.add(passwordField);

    // New Role drop-down (JComboBox)
    editUserDialog.add(new JLabel("New Role:"));
    JComboBox<String> roleComboBox = new JComboBox<>(new String[] {"Admin", "Manager", "Salesperson"});
    editUserDialog.add(roleComboBox);

    JButton loadButton = new JButton("Load Data");
    editUserDialog.add(loadButton);

    JButton editButton = new JButton("Submit");
    editUserDialog.add(editButton);

    // Handle load data button click to populate fields
    loadButton.addActionListener(e -> {
        try {
            int userId = Integer.parseInt(userIdField.getText().trim());
            DatabaseManager dbManager = new DatabaseManager();

            // Fetch user data and role name
            String query = "SELECT u.name, u.password, r.role FROM users u " +
                           "JOIN roles r ON u.roleId = r.id WHERE u.id = " + userId;
            System.out.println("Executing query: " + query);

            ResultSet rs = dbManager.runQuery(query);

            if (rs.next()) {
                // Populate the fields with existing data
                usernameField.setText(rs.getString("name"));
                passwordField.setText(rs.getString("password"));
                // Set the role in the combo box
                String role = rs.getString("role");
                roleComboBox.setSelectedItem(role); // Select role from the options
                userIdField.setEditable(false);  // Make User ID field non-editable
            } else {
                // If no user found, clear the fields and show a message
                usernameField.setText("");
                passwordField.setText("");
                roleComboBox.setSelectedIndex(0);  // Reset to the default role (first item)
                JOptionPane.showMessageDialog(this, "User ID not found.");
            }

            rs.close();
            dbManager.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching user data. Please check the console for details.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid User ID format. Please enter a valid number.");
        }
    });

    // Handle the edit action when the "Edit" button is clicked
    editButton.addActionListener(e -> {
        // Validate that none of the fields are empty
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        // Check if any field is empty
        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;  // Prevent proceeding if any field is empty
        }

        try {
            int userId = Integer.parseInt(userIdField.getText().trim());

            // Update the user data in the database
            DatabaseManager dbManager = new DatabaseManager();
            String query = "UPDATE users SET name = '" + username + "', password = '" + password + "', roleId = (SELECT id FROM roles WHERE role = '" + role + "') WHERE id = " + userId;
            dbManager.runInsert(query);
            dbManager.close();
            JOptionPane.showMessageDialog(this, "User edited successfully.");
            editUserDialog.dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error editing user.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid User ID format. Please enter a valid number.");
        }
    });

    editUserDialog.setVisible(true);
}


public void showDeleteUserDialog() {
    JDialog deleteUserDialog = new JDialog(this, "Delete User", true);
    deleteUserDialog.setSize(400, 200);
    deleteUserDialog.setLayout(new GridLayout(0, 2, 10, 10));
    deleteUserDialog.setLocationRelativeTo(this);

    deleteUserDialog.add(new JLabel("User ID:"));
    JTextField userIdField = new JTextField();
    deleteUserDialog.add(userIdField);

    JButton deleteButton = new JButton("Delete");
    deleteUserDialog.add(deleteButton);

    deleteButton.addActionListener(e -> {
        int userId = Integer.parseInt(userIdField.getText());

        // Delete user from the database
        try {
            DatabaseManager dbManager = new DatabaseManager();
            String query = "DELETE FROM users WHERE id = " + userId;
            int rowsAffected = dbManager.runInsert(query);
            dbManager.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "User deleted successfully.");
                deleteUserDialog.dispose();
                // Update the user table
                showViewUsersDialog((DefaultTableModel) userTable.getModel());
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }
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
        String query = "SELECT users.id, users.name, users.password, roles.role FROM users JOIN roles ON users.roleId = roles.id order by users.id";
        ResultSet resultSet = dbManager.runQuery(query);

        // Populate the table with data
        while (resultSet.next()) {
            int userId = resultSet.getInt("id");
            String username = resultSet.getString("name");
            String password = resultSet.getString("password");
            String role = resultSet.getString("role");
            model.addRow(new Object[]{userId, username, password, role});
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
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    
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
            card.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 130), 2));
        }
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            card.setBorder(BorderFactory.createEmptyBorder());
        }
    });
    
    return card;
}
}
