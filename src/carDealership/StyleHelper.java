package carDealership;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;







public class StyleHelper {
    
    public static void styleButton(JButton button) {
        button.setBackground(new Color(67, 70, 75));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // button.setBorder(new RoundedBorder(12));
    
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 60, 60));
            }
    
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(67, 70, 75));
            }
        });
    }
    
    public static void styleNavButton(JButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10)); // 10px total horizontal padding

    
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(67, 70, 75)); // Light Sky Blue on hover
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });
    }
   
    public static void highlightSidebarButton(JButton button, boolean selected) {
        if (selected) {
            button.setBackground(new Color(50, 60, 80));
            button.setOpaque(true);
        } else {
            button.setBackground(new Color(70, 70, 70));
            button.setOpaque(false);
        }
    }
    
    public static void styleSidebarButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(1);
        button.setBorder(BorderFactory.createEmptyBorder(8, 10, 20, 20));


    
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!button.getModel().isPressed()) {
                    button.setBackground(new Color(50, 60, 80));
                    button.setOpaque(true);
                }
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!button.getModel().isPressed()) {
                    button.setBackground(new Color(35, 45, 65));
                    button.setOpaque(false);
                }
            }
        });
    }
    

    

    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(20, 20, 20));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);

        table.setSelectionBackground(new Color(200, 220, 255));
        table.setSelectionForeground(Color.BLACK);
    }
}
