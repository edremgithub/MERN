package kldLostAndFound;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextField;

public class TransparentTextField extends JTextField {

    private static final long serialVersionUID = 1L;

    private float opacity;

    public TransparentTextField(float opacity) {
        super();
        this.opacity = opacity;
        setOpaque(false); // Ensures the background is not solid
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (opacity > 0) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
        super.paintComponent(g);
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();
    }
}
