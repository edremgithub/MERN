package kldLostAndFound;

import javax.swing.JTextField;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedTextField extends JTextField {

    private static final long serialVersionUID = 1L;

    private int arcWidth = 15;
    private int arcHeight = 15;
    private float opacity = 0.7f;
    private Color backgroundColor = Color.WHITE;

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(null); // Removes the default text field border
        setForeground(Color.BLACK); // Ensure text is readable
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable anti-aliasing for smooth corners
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Apply opacity and background
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arcWidth, arcHeight));

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Optional: draw custom border if needed
        // Example:
        // Graphics2D g2 = (Graphics2D) g.create();
        // g2.setColor(Color.GRAY);
        // g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
        // g2.dispose();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    public void setOpacity(float opacity) {
        this.opacity = Math.min(1.0f, Math.max(0.0f, opacity)); // Clamp between 0 and 1
        repaint();
    }

    public void setCornerRadius(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        repaint();
    }
}
