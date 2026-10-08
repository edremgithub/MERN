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
import javax.swing.border.EmptyBorder;

public class AdminClaimReqSuccess extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

    	private String adminId;
    	private AdminUI adminUI;
	public AdminClaimReqSuccess( String adminId, AdminUI adminUI) {
		this.adminId = adminId;
		this.adminUI = adminUI;
		  setResizable(false);
	        setTitle("Item Successfuly Transferred - KLD Lost & Found");
	        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        setBounds(100, 100, 599, 546);
	        setLocationRelativeTo(null);

	        // App icon
	        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

	        // Background
	        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AdminClaimReqSuccess.jpg");
	        background.setLayout(null);
	        setContentPane(background);


	        // Go to My Account Button (bottom)
	        RoundedButton btnView = new RoundedButton("Admin Options", new Color(255,242,0), 0.8f, 11);
	        btnView.setFont(new Font("Tahoma", Font.BOLD, 13));
	        btnView.setBounds(228, 315, 133, 40);
	        btnView.addActionListener(new ActionListener() {
	            @Override
				public void actionPerformed(ActionEvent e) {

	                	            
	            	AdminClaimReq adminClaimReq = new AdminClaimReq(adminId, adminUI);
	            	adminClaimReq.setVisible(true);
	            	dispose();
	            }
	        });
	        background.add(btnView);
	     
	}

}
