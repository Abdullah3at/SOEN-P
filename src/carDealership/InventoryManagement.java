package carDealership;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InventoryManagement extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton addVehicleButton, deleteVehicleButton, editVehicleButton, viewVehicleButton;

    public InventoryManagement() {
        setTitle("Inventory Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        addVehicleButton = new JButton("Add Vehicle");
        deleteVehicleButton = new JButton("Delete Vehicle");
        editVehicleButton = new JButton("Edit Vehicle");
        viewVehicleButton = new JButton("View Vehicle");

        addVehicleButton.addActionListener(this);
        deleteVehicleButton.addActionListener(this);
        editVehicleButton.addActionListener(this);
        viewVehicleButton.addActionListener(this);

        add(addVehicleButton);
        add(deleteVehicleButton);
        add(editVehicleButton);
        add(viewVehicleButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addVehicleButton) {
            // Add vehicle functionality
        } else if (e.getSource() == deleteVehicleButton) {
            // Delete vehicle functionality
        } else if (e.getSource() == editVehicleButton) {
            // Edit vehicle functionality
        } else if (e.getSource() == viewVehicleButton) {
            // View vehicle functionality
        }
    }
}