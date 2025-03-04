package carDealership;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SellVehicle extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JTextField vehicleIdField, buyerNameField, buyerContactField;
    private JButton sellButton;

    public SellVehicle() {
        setTitle("Sell Vehicle");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Vehicle ID:"));
        vehicleIdField = new JTextField();
        add(vehicleIdField);

        add(new JLabel("Buyer Name:"));
        buyerNameField = new JTextField();
        add(buyerNameField);

        add(new JLabel("Buyer Contact:"));
        buyerContactField = new JTextField();
        add(buyerContactField);

        sellButton = new JButton("Sell");
        sellButton.addActionListener(this);
        add(sellButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sellButton) {
            int vehicleId = Integer.parseInt(vehicleIdField.getText());
            String buyerName = buyerNameField.getText();
            String buyerContact = buyerContactField.getText();
            Vehicle vehicle = Main.m_dealership.getVehicleFromId(vehicleId);

            if (vehicle != null && Main.m_dealership.sellVehicle(vehicle, buyerName, buyerContact)) {
                JOptionPane.showMessageDialog(this, "Vehicle sold successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to sell vehicle. Please check the details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}