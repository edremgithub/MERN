package kldLostAndFound;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class RoundedTextArea extends JTextArea {
    private static final long serialVersionUID = 1L;

    public RoundedTextArea(int rows, int columns) {
        super(rows, columns);
        setOpaque(false);
        setBackground(new Color(255, 255, 255)); // Set default background color
        setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        setLineWrap(true);
        setWrapStyleWord(true);
        setForeground(Color.BLACK); // Ensure text is visible
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
        g2.dispose();
    }
}
