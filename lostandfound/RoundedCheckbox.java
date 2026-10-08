package kldLostAndFound;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;

public class RoundedCheckbox extends JCheckBox {

    private Color originalBgColor;
    private Color hoverBgColor;
    private Color originalFgColor;
    private Color hoverFgColor;
    private float opacity;
    private int radius;

    // Constructor
    public RoundedCheckbox(String text, Color bgColor, float opacity, int radius) {
        super(text);
        this.originalBgColor = bgColor;
        this.hoverBgColor = Color.WHITE;
        this.originalFgColor = Color.WHITE;
        this.hoverFgColor = bgColor;
        this.opacity = opacity;
        this.radius = radius;

        setContentAreaFilled(false); // Ensures the checkbox area is transparent
        setFocusPainted(false); // Removes the focus outline
        setFont(new Font("Tahoma", Font.BOLD, 10));
        setForeground(originalFgColor);
        setBackground(originalBgColor);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBgColor);
                setForeground(hoverFgColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(originalBgColor);
                setForeground(originalFgColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background with rounded corners
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius); // Apply rounded corners
        g2.dispose();

        super.paintComponent(g);
    }
}
