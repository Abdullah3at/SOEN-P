package carDealership;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserManagementPanel extends JPanel {
    private AdminDashboard parent;
    private JTable userTable;
    
    public UserManagementPanel(AdminDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        
        // Header panel for the label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel label = new JLabel("User Management");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(label);
        
        // Button panel for user management actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");
        JButton viewUsersButton = new JButton("View Users");
        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(viewUsersButton);
        
        // Combine header and button panels
        JPanel combinedHeader = new JPanel();
        combinedHeader.setBackground(Color.LIGHT_GRAY);
        combinedHeader.setLayout(new BoxLayout(combinedHeader, BoxLayout.Y_AXIS));
        combinedHeader.add(headerPanel);
        combinedHeader.add(buttonPanel);
        
        // Create user table
        String[] columnNames = {"User ID", "Username", "Role", "Is_Temp_password"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(userTable);
        userTable.setRowHeight(25);

        
        add(combinedHeader, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Connect buttons to parent methods
        addUserButton.addActionListener(e -> parent.showAddUserDialog());
        editUserButton.addActionListener(e -> parent.showEditUserDialog());
        deleteUserButton.addActionListener(e -> parent.showDeleteUserDialog());
        viewUsersButton.addActionListener(e -> parent.showViewUsersDialog(model));
        
        // Automatically display user table when the panel is loaded.
        parent.showViewUsersDialog(model);
    }
    
    // Getter for JTable object
    public JTable getUserTable() {
        return userTable;
    }
}