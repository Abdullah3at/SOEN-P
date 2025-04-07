package carDealership;

import java.awt.*;
import javax.swing.*;

public class TransparentImagePanel extends JPanel {
    private Image backgroundImage;

    public TransparentImagePanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + imagePath);
        }
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imageWidth = backgroundImage.getWidth(null);
            int imageHeight = backgroundImage.getHeight(null);

            // Center it
            int x = (panelWidth - imageWidth) / 2;
            int y = (panelHeight - imageHeight) / 2;

            // Add transparency
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f)); // 0.25 = 25% visible
            g2d.drawImage(backgroundImage, x, y, this);
        }
    }
}
