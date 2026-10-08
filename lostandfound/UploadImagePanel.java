package kldLostAndFound;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.swing.*;

public class UploadImagePanel extends JPanel {
    private File selectedFile;
    private Image image;
    private final String defaultText = "Upload Image";
    private final int cornerRadius = 20;
    private boolean editable = false;
    private MouseAdapter mouseAdapter;

    public UploadImagePanel(boolean editable) {
        this.editable = editable;
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    image = new ImageIcon(selectedFile.getAbsolutePath())
                            .getImage()
                            .getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    repaint();
                }
            }
        };

        if (editable) {
            addMouseListener(mouseAdapter);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2d.setClip(clip);

        if (image != null) {
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2d.setColor(new Color(245, 245, 245));
            g2d.fill(clip);

            if (editable) {
                g2d.setColor(Color.GRAY);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(defaultText);
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;
                g2d.drawString(defaultText, x, y);
            }
        }

        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2f));
        g2d.draw(clip);

        g2d.dispose();
    }

    public File getImageFile() {
        return selectedFile;
    }

    public void setImage(ImageIcon icon) {
        if (icon != null) {
            this.image = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        } else {
            this.image = null;
            this.selectedFile = null;
        }
        repaint();
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        if (editable) {
            addMouseListener(mouseAdapter);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            removeMouseListener(mouseAdapter);
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public boolean isEditable() {
        return editable;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public byte[] getImageBytes() {
        if (selectedFile != null) {
            try {
                return java.nio.file.Files.readAllBytes(selectedFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Image getImage() {
        return image;
    }
}
