package carDealership;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserManagementPanel extends JPanel {
    private AdminDashboard parent;
    private JTable userTable;

    public UserManagementPanel(AdminDashboard parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // --- HEADER ---
        ModernPanel headerPanel = new ModernPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("User Management");
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerPanel.add(label);

        // --- BUTTONS ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");
        JButton viewUsersButton = new JButton("View Users");

        JButton[] buttons = {addUserButton, editUserButton, deleteUserButton, viewUsersButton};
        for (JButton btn : buttons) {
            StyleHelper.styleButton(btn);
            btn.setPreferredSize(new Dimension(140, 40));
            buttonPanel.add(btn);
        }

        // --- HEADER COMBINED ---
        JPanel combinedHeader = new JPanel();
        combinedHeader.setLayout(new BoxLayout(combinedHeader, BoxLayout.Y_AXIS));
        combinedHeader.setBackground(Color.LIGHT_GRAY);
        combinedHeader.add(Box.createVerticalStrut(10));
        combinedHeader.add(headerPanel);
        combinedHeader.add(Box.createVerticalStrut(5));
        combinedHeader.add(buttonPanel);
        combinedHeader.add(Box.createVerticalStrut(10));

        // --- TABLE SETUP ---
        String[] columnNames = {"User ID", "Username", "Role", "Is_Temp_password"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(model);
        userTable.setRowHeight(25);
        StyleHelper.styleTable(userTable);

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        userTable.setOpaque(false);

        // --- WATERMARK PANEL ---
        JPanel watermarkPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                ImageIcon icon = new ImageIcon("src/images/car.png");
                int fixedWidth = 1100;
                int fixedHeight = 1100;
                int x = (getWidth() - fixedWidth) / 2;
                int y = (getHeight() - fixedHeight) / 2;

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
                g2d.drawImage(icon.getImage(), x, y, fixedWidth, fixedHeight, this);
                g2d.dispose();
            }
        };
        watermarkPanel.setOpaque(false);

        // --- LAYERED PANE ---
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.setPreferredSize(new Dimension(1000, 400));
        layeredPane.add(watermarkPanel, Integer.valueOf(1));
        layeredPane.add(scrollPane, Integer.valueOf(0));

        // --- FINAL LAYOUT ---
        add(combinedHeader, BorderLayout.NORTH);
        add(layeredPane, BorderLayout.CENTER);

        // --- Button Actions ---
        addUserButton.addActionListener(e -> parent.showAddUserDialog());
        editUserButton.addActionListener(e -> parent.showEditUserDialog());
        deleteUserButton.addActionListener(e -> parent.showDeleteUserDialog());
        viewUsersButton.addActionListener(e -> parent.showViewUsersDialog(model));

        // Load users
        parent.showViewUsersDialog(model);
    }

    public JTable getUserTable() {
        return userTable;
    }
}
