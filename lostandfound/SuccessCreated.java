package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SuccessCreated extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SuccessCreated frame = new SuccessCreated();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SuccessCreated() {
		  setResizable(false);
	        setTitle("Account Creation Successful - KLD Lost & Found");
	        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        setBounds(100, 100, 599, 546);
	        setLocationRelativeTo(null);

	        // App icon
	        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

	        // Background
	        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/SuccessCreated.jpg");
	        background.setLayout(null);
	        setContentPane(background);


	        // Go to My Account Button (bottom)
	        RoundedButton btnLg = new RoundedButton("Log In", new Color(0, 128, 55), 0.8f, 12);
	        btnLg.setFont(new Font("Tahoma", Font.BOLD, 13));
	        btnLg.setBounds(228, 315, 133, 40);
	        btnLg.addActionListener(new ActionListener() {
	            @Override
				public void actionPerformed(ActionEvent e) {

	                	            	// Go to Log In screen
	            	LogInUI loginUI = new LogInUI();
	            	loginUI.frmKldLostAnd.setVisible(true);
	                dispose(); // Close this window
	            }
	        });
	        background.add(btnLg);
	}

}
