package carDealership;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import persistance.DatabaseManager;

public class LoginPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginPage() {
        setTitle("Car Dealership System - Login");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient background
        JPanel gradientPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gp = new GradientPaint(
                    getWidth(), 0, new Color(0x434343),
                    0, 0, new Color(0x000000)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(new BoxLayout(gradientPanel, BoxLayout.Y_AXIS));

        // Welcome Text
        JLabel titleLabel = new JLabel("Welcome to Car Dealership System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 0));

        JLabel subtitleLabel = new JLabel("Effortlessly manage vehicles, users, sales, and more.", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Fields Panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setOpaque(false);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(300, 35));
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        usernameField.setBackground(new Color(0, 0, 0, 0));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        usernameField.setOpaque(false);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        passLabel.setForeground(Color.WHITE);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 35));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passwordField.setBackground(new Color(0, 0, 0, 0));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        passwordField.setOpaque(false);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(new Color(20, 10, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setFocusPainted(false);
        loginButton.setMaximumSize(new Dimension(200, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(this);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        fieldsPanel.add(userLabel);
        fieldsPanel.add(usernameField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        fieldsPanel.add(passLabel);
        fieldsPanel.add(passwordField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        fieldsPanel.add(loginButton);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        fieldsPanel.add(messageLabel);

        gradientPanel.add(titleLabel);
        gradientPanel.add(subtitleLabel);
        gradientPanel.add(fieldsPanel);

        // Final Layout
        add(gradientPanel, BorderLayout.CENTER);
        getRootPane().setDefaultButton(loginButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try {
            if (authenticate(username, password)) {
                String role = getRole(username);
                AdminDashboard dashboard = createDashboardInstance(role);
                dashboard.setVisible(true);
                dispose();
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("Database error.");
        }
    }

    private boolean authenticate(String username, String password) throws SQLException {
        DatabaseManager dbManager = new DatabaseManager();
        ResultSet rs = dbManager.runQuery("SELECT * FROM users WHERE name = '" + username + "' AND password = '" + password + "'");
        boolean ok = rs.next();
        dbManager.close();
        return ok;
    }

    private String getRole(String username) throws SQLException {
        DatabaseManager dbManager = new DatabaseManager();
        ResultSet rs = dbManager.runQuery("SELECT roles.role FROM users JOIN roles ON users.roleId = roles.id WHERE users.name = '" + username + "'");
        String role = rs.next() ? rs.getString("role") : null;
        dbManager.close();
        return role;
    }

    private AdminDashboard createDashboardInstance(String role) {
        return new AdminDashboard(role);
    }

    public static void displayLogin() {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
