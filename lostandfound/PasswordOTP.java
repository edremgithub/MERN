	package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class PasswordOTP extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PasswordOTP frame = new PasswordOTP();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
 
    public PasswordOTP() {
        setResizable(false);
        setTitle("OTP Verification - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 599, 546);
        setLocationRelativeTo(null);

        // Set app icon
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        // Set background
        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/OTPBG.jpg");
        setContentPane(background);
        background.setLayout(null);


        RoundedButton btnVerify = new RoundedButton("Verify", new Color(0, 128, 55), 0.8f, 11);
        btnVerify.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnVerify.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        	}
        });
        btnVerify.setBounds(242, 329, 105, 36);
        background.add(btnVerify);

        // OTP TextFields (6 boxes)
        int boxWidth = 40;
        int boxHeight = 45;
        int spacing = 10;
        int startX = (599 - (6 * boxWidth + 5 * spacing)) / 2;
        int y = 261;

        JTextField[] otpFields = new JTextField[6];

        for (int i = 0; i < 6; i++) {
            final int index = i;
            JTextField field = new JTextField();
            field.setBounds(startX + i * (boxWidth + spacing), y, boxWidth, boxHeight);
            field.setOpaque(true);
            field.setBackground(new Color(200, 200, 200, 250));
            field.setHorizontalAlignment(SwingConstants.CENTER);
            field.setFont(new Font("SansSerif", Font.BOLD, 24));
            field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            // Enforce single digit only
            PlainDocument doc = (PlainDocument) field.getDocument();
            doc.setDocumentFilter(new DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                        throws BadLocationException {
                    if (text != null && text.length() > 0 && text.substring(0, 1).matches("\\d")) {
                        fb.remove(0, fb.getDocument().getLength());
                        fb.insertString(0, text.substring(0, 1), attrs);
                    }
                }

                @Override
                public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                        throws BadLocationException {
                    if (text != null && text.length() > 0 && text.substring(0, 1).matches("\\d")) {
                        fb.remove(0, fb.getDocument().getLength());
                        fb.insertString(0, text.substring(0, 1), attr);
                    }
                }
            });

            // Auto navigation
            field.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == java.awt.event.KeyEvent.VK_BACK_SPACE) {
                        if (field.getText().isEmpty() && index > 0) {
                            otpFields[index - 1].requestFocus();
                        }
                    } else {
                        if (field.getText().length() == 1 && index < otpFields.length - 1) {
                            otpFields[index + 1].requestFocus();
                        }
                    }
                }
            });

            background.add(field);
            otpFields[i] = field;
        }
    }
}
