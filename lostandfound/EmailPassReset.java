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

public class EmailPassReset extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EmailPassReset frame = new EmailPassReset();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
 
    public EmailPassReset() { 
    	setResizable(false);
        setTitle("Email Verification - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 599, 546);
        setLocationRelativeTo(null); // center

        // Logo
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/EmailPassReset.jpg");
        setContentPane(background);
        background.setLayout(null);
        textField = new JTextField();
        textField.setOpaque(true);
        textField.setBackground(new Color(200, 200, 200)); // Solid gray
        textField.setBorder(BorderFactory.createEmptyBorder()); // Optional, for clean look
        textField.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        textField.setBounds(134, 266, 314, 28);
        background.add(textField);
        
        RoundedButton btnBack = new RoundedButton("Back",new Color(0, 128, 55), 0.8f, 11);
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 10));
        btnBack.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        	    				LogInUI login = new LogInUI();
        	    				login.frmKldLostAnd.setVisible(true);
        			dispose();
        	}
        });
        btnBack.setBounds(457, 26, 99, 34);
        background.add(btnBack);


        RoundedButton btnReset = new RoundedButton("Reset Password", new Color(0, 128, 55), 0.8f, 11);
        btnReset.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnReset.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        	}
        });
        btnReset.setBounds(228, 299, 128, 23);
        background.add(btnReset);
    }
}
