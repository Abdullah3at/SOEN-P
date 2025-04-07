package carDealership;

import java.awt.*;
import javax.swing.*;

public class ModernPanel extends JPanel {

    private final int topPadding = 20;
    private final int leftPadding = 24;
    private final int bottomPadding = 20;
    private final int rightPadding = 24;

    private final int cornerRadius = 20;

    public ModernPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = leftPadding / 2;
        int y = topPadding / 2;
        int width = getWidth() - (leftPadding / 2 + rightPadding / 2);
        int height = getHeight() - (topPadding / 2 + bottomPadding / 2);

        // Drop shadow
        g2.setColor(new Color(0, 0, 0, 25));
        g2.fillRoundRect(x + 4, y + 4, width - 8, height - 8, cornerRadius, cornerRadius);

        // Background panel fill
        g2.setColor(new Color(245, 245, 250));
        g2.fillRoundRect(x, y, width - 8, height - 8, cornerRadius, cornerRadius);

        g2.dispose();
    }
}
