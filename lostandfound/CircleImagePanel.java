package kldLostAndFound;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CircleImagePanel extends JPanel {
    private BufferedImage image;
    private File selectedFile;
    private boolean editable = false;

    public File getSelectedFile() {
        return selectedFile;
    }

    public CircleImagePanel(boolean editable) {
        this.editable = editable;
        setPreferredSize(new Dimension(150, 150));
        setOpaque(false);
        setCursor(editable ? new Cursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());

        if (editable) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    int result = chooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFile = chooser.getSelectedFile();
                        try {
                            image = ImageIO.read(selectedFile);
                            repaint();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to load image.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int size = Math.min(getWidth(), getHeight());
            BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleBuffer.createGraphics();

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(image, 0, 0, size, size, null);
            g2.dispose();

            g.drawImage(circleBuffer, 0, 0, null);
        } else {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.setColor(Color.GRAY);
            g2.drawOval(0, 0, getWidth(), getHeight());

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            FontMetrics fm = g2.getFontMetrics();
            String text = "Upload";
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 4;
            g2.drawString(text, x, y);
        }
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        setCursor(editable ? new Cursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
    }

    public void setImage1(ImageIcon icon) {
        if (icon != null) {
            Image img = icon.getImage();
            BufferedImage buffered = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = buffered.createGraphics();
            bGr.drawImage(img, 0, 0, null);
            bGr.dispose();
            this.image = buffered;
            repaint();
        } else {
            this.image = null;
            repaint();
        }
    }
}
