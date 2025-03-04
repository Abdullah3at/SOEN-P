package carDealership;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserManagement extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton addUserButton, deleteUserButton, editUserButton, viewUserButton;

    public UserManagement() {
        setTitle("User Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        addUserButton = new JButton("Add User");
        deleteUserButton = new JButton("Delete User");
        editUserButton = new JButton("Edit User");
        viewUserButton = new JButton("View User");

        addUserButton.addActionListener(this);
        deleteUserButton.addActionListener(this);
        editUserButton.addActionListener(this);
        viewUserButton.addActionListener(this);

        add(addUserButton);
        add(deleteUserButton);
        add(editUserButton);
        add(viewUserButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addUserButton) {
            // Add user functionality
        } else if (e.getSource() == deleteUserButton) {
            // Delete user functionality
        } else if (e.getSource() == editUserButton) {
            // Edit user functionality
        } else if (e.getSource() == viewUserButton) {
            // View user functionality
        }
    }
}