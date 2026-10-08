	package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ForgetPasswordUI extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ForgetPasswordUI frame = new ForgetPasswordUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    } 

    public ForgetPasswordUI() {
    	setResizable(false);
        setTitle("Forgot Password - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 599, 546); // match ForgotPassword.jpg size
        setLocationRelativeTo(null); // center on screen

        // App icon
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/ForgotPassword.jpg");
        setContentPane(background);
        background.setLayout(null);


        RoundedButton btnLogIn = new RoundedButton("Log In", new Color(0, 128, 55), 0.8f, 11);
        btnLogIn.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnLogIn.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		LogInUI login = new LogInUI();
        		login.frmKldLostAnd.setVisible(true);
        		dispose(); // Close the current window
        	}
        });
        btnLogIn.setBounds(235, 358, 105, 36);
        background.add(btnLogIn);
    }
}
