package carDealership;

import persistance.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class ChangePasswordDialog extends JDialog {
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton changeButton;
    private JLabel messageLabel;
    private String username;

    public ChangePasswordDialog(String username) {
        this.username = username;
        setTitle("Change Temporary Password");
        setModal(true);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        newPasswordField = new JPasswordField(15);
        formPanel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        formPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        changeButton = new JButton("Change Password");
        formPanel.add(changeButton, gbc);

        gbc.gridy = 3;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        formPanel.add(messageLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String newPass = newPasswordField.getText().trim();
        String confirmPass = new String(confirmPasswordField.getPassword()).trim();
        if (!newPass.equals(confirmPass)) {
            messageLabel.setText("Passwords do not match!");
            return;
        }
        if (newPass.isEmpty()) {
            messageLabel.setText("Password cannot be empty!");
            return;
        }

        try {
            DatabaseManager dbManager = new DatabaseManager();
            // Update the users table with the new password and mark isTemp as false
            String updateQuery = "UPDATE users SET password = '" + newPass + "', isTemp = false WHERE name = '" + username + "'";
            dbManager.runInsert(updateQuery);
            dbManager.close();
            JOptionPane.showMessageDialog(this, "Password changed successfully. Please login again.");
            dispose();
            LoginPage.displayLogin();
        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("Error updating password!");
        }
    }
}