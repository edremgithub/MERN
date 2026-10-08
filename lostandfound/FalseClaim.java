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

public class FalseClaim extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String idNumber;
	private MyAccount myAccount;
	

	
	public FalseClaim( String idNumber, MyAccount myAccount) {
		this.idNumber = idNumber;
		this.myAccount = myAccount;
		setResizable(false);
    setTitle("False Claim Appeal - KLD Lost & Found");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setBounds(100, 100, 599, 546);
    setLocationRelativeTo(null);

    // App icon
    setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

    // Background
    BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/FalseClaim.jpg");
    background.setLayout(null);
    setContentPane(background);


    // Go to My Account Button (bottom)
    RoundedButton btnMyAccount = new RoundedButton("Go to My Account", new Color(220, 20, 60), 0.8f, 11);
    btnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 13));
    btnMyAccount.setBounds(192, 315, 200, 40);
    btnMyAccount.addActionListener(new ActionListener() {
        @Override
		public void actionPerformed(ActionEvent e) {

       	 // Close the current window
			dispose();

			// Open My Account window
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						MyAccount myAccount = new MyAccount(idNumber);
						myAccount.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
        }
    });
    background.add(btnMyAccount);
}}


