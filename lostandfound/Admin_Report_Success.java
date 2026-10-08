package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Admin_Report_Success extends JFrame {

    private static final long serialVersionUID = 1L;
    private String adminId;
    private AdminUI adminUI;

    

    public Admin_Report_Success( String adminId, AdminUI adminUI) {
    				this.adminId = adminId;
    				this.adminUI = adminUI;
        setResizable(false);
        setTitle("Report Submitted - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 599, 546);
        setLocationRelativeTo(null);

        // App icon
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        // Background
        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/ReportSuccess.jpg");
        background.setLayout(null);
        setContentPane(background);


        // Go to My Account Button (bottom)
        RoundedButton btnMyAccount = new RoundedButton("Go to My Account", new Color(255, 242, 0), 0.8f, 12);
        btnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnMyAccount.setBounds(192, 315, 200, 40);
        btnMyAccount.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
            	adminUI.setVisible(true);
            					dispose();
            }
        });
        background.add(btnMyAccount);
    }
}
