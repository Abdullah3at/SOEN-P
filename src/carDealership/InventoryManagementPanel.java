package carDealership;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InventoryManagementPanel extends JPanel {
    private AdminDashboard parent;
    private JTable vehicleTable;


    public InventoryManagementPanel(AdminDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("Inventory Management");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(label);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton addVehicleButton = new JButton("Add Vehicle");
        JButton editVehicleButton = new JButton("Edit Vehicle");
        JButton deleteVehicleButton = new JButton("Delete Vehicle");
        JButton viewVehiclesButton = new JButton("View Vehicles");
        JButton filterButton = new JButton("Filter");
        buttonPanel.add(addVehicleButton);
        buttonPanel.add(editVehicleButton);
        buttonPanel.add(deleteVehicleButton);
        buttonPanel.add(viewVehiclesButton);
        buttonPanel.add(filterButton);

        // Combine header and button panels vertically
        JPanel combinedHeader = new JPanel();
        combinedHeader.setLayout(new BoxLayout(combinedHeader, BoxLayout.Y_AXIS));
        combinedHeader.setBackground(Color.LIGHT_GRAY);
        combinedHeader.add(headerPanel);
        combinedHeader.add(buttonPanel);

        // Table for vehicles
        String[] columnNames = {"Vehicle ID", "Make", "Model", "Year", "Price", "Type", "Color"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        vehicleTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        vehicleTable.setRowHeight(25);


        add(combinedHeader, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    
        
        // Button Actions: delegate to parent's methods
        addVehicleButton.addActionListener(e -> parent.showAddVehicleDialog());
        editVehicleButton.addActionListener(e -> parent.showEditVehicleDialog());
        deleteVehicleButton.addActionListener(e -> parent.showDeleteVehicleDialog());
        viewVehiclesButton.addActionListener(e -> parent.showViewVehiclesDialog(model));
        filterButton.addActionListener(e -> parent.showFilterDialog(vehicleTable));

        // Automatically display the vehicle table upon panel load.
        parent.showViewVehiclesDialog(model);
    }
    
}