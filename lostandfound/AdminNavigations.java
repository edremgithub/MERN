package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AdminNavigations extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String adminId;
	private AdminUI adminUI;

	
	public AdminNavigations( String adminId, AdminUI adminUI) {
		this.adminId = adminId;
		this.adminUI = adminUI;
		 setTitle("Admin Navigations  - KLD Lost & Found");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setBounds(100, 100, 389, 534);
	        setResizable(false);
	        setLocationRelativeTo(null); // Center the window on the screen
	        
	        // Set app icon		
	        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());
	        
	        // Set background
	        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AdminNavigations.jpg");
	        background.setLayout(null);
	        
	        
	        // Initialize contentPane and set background as content pane
	        contentPane = new JPanel();
	        contentPane.setLayout(null);  // Set layout as null
	        setContentPane(background);  // Set background panel as the content pane
	        
	        
	        // My Admin Account Button
	        RoundedButton btnadmin = new RoundedButton("My Account", new Color(255, 240, 0), 0.8f, 11);
	        btnadmin.setText("Back");
	        btnadmin.setFont(new Font("Tahoma", Font.BOLD, 11));
	        btnadmin.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                AdminUI frame = new AdminUI(adminId);
	                frame.setVisible(true);
	                dispose(); // Close the current frame
	            }
	        });
	        btnadmin.setBounds(255, 21, 100, 29);
	        btnadmin.setForeground(Color.BLACK);
	        background.add(btnadmin);  // Add button to background panel, not contentPane
	        
	        RoundedButton btnreportlost = new RoundedButton("Report Lost", new Color(0,150,57), 0.8f, 11);
	        btnreportlost.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		AdminReportLost frame = new AdminReportLost(adminId, adminUI);
	        			frame.setVisible(true);
	        			dispose(); // Close the current frame
	        	}
	        });
	        btnreportlost.setText("Lost Reports Table");
	        btnreportlost.setForeground(Color.BLACK);
	        btnreportlost.setFont(new Font("Tahoma", Font.BOLD, 11));
	        btnreportlost.setBounds(100, 184, 172, 22);
	        background.add(btnreportlost);
	        
	        RoundedButton btnReportFound = new RoundedButton("Report Found", new Color(255, 240, 0), 0.8f, 11);
	        btnReportFound.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		AdminReportFound frame = new AdminReportFound(adminId, adminUI);
	        			frame.setVisible(true);
	        			dispose(); // Close the current frame
	        	}
	        });
	        btnReportFound.setText("Found Reports Table");
	        btnReportFound.setForeground(Color.BLACK);
	        btnReportFound.setFont(new Font("Tahoma", Font.BOLD, 11));
	        btnReportFound.setBounds(100, 214, 172, 22);
	        background.add(btnReportFound);
	        
	        RoundedButton btn_approveclaimreq = new RoundedButton("Approve Claim Request", new Color(0,150,57), 0.8f, 11);
	        btn_approveclaimreq.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		AdminClaimReq frame = new AdminClaimReq(adminId, adminUI);
	        			frame.setVisible(true);
	        			dispose(); // Close the current frame
	        	}
	        });
	        btn_approveclaimreq.setText("Approve Claim Request");
	        btn_approveclaimreq.setForeground(Color.BLACK);
	        btn_approveclaimreq.setFont(new Font("Tahoma", Font.BOLD, 11));
	        btn_approveclaimreq.setBounds(100, 244, 172, 22);
	        background.add(btn_approveclaimreq);
	        
	        RoundedButton btnadmin_1_3 = new RoundedButton("Item Inventory", new Color(255, 240, 0), 0.8f, 11);
	        btnadmin_1_3.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		AdminItemStats frame = new AdminItemStats(adminId, adminUI);
	        			frame.setVisible(true);
	        			dispose(); // Close the current frame
	        	}
	        });
	        btnadmin_1_3.setText("Item Inventory");
	        btnadmin_1_3.setForeground(Color.BLACK);
	        btnadmin_1_3.setFont(new Font("Tahoma", Font.BOLD, 11));
	        btnadmin_1_3.setBounds(100, 274, 172, 22);
	        background.add(btnadmin_1_3);
	        
	        RoundedButton btnlostreportsarchive = new RoundedButton("Lost Reports Archive", new Color(220, 20, 60), 0.8f, 11);
	        btnlostreportsarchive.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		AdminReportItemLostArchive frame = new AdminReportItemLostArchive(adminId, adminUI);
	        			frame.setVisible(true);
	        			dispose(); // Close the current frame
	        	}
	        });
	        btnlostreportsarchive.setText("Lost Reports Archive");
	        btnlostreportsarchive.setForeground(Color.BLACK);
	        btnlostreportsarchive.setFont(new Font("Tahoma", Font.BOLD, 11));
	        btnlostreportsarchive.setBounds(100, 344, 172, 22);
	        background.add(btnlostreportsarchive);
	        
	        RoundedButton btnfoundreportmanagement = new RoundedButton("Found Reports Archive", new Color(220, 20, 60), 0.8f, 11);
	        btnfoundreportmanagement.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		AdminReportItemFoundArchive frame = new AdminReportItemFoundArchive(adminId, adminUI);
	        			frame.setVisible(true);
	        			dispose(); // Close the current frame
	        	}
	        });
	        btnfoundreportmanagement.setText("Found Reports Archive");
	        btnfoundreportmanagement.setForeground(Color.BLACK);
	        btnfoundreportmanagement.setFont(new Font("Tahoma", Font.BOLD, 11));
	        btnfoundreportmanagement.setBounds(100, 374, 172, 22);
	        background.add(btnfoundreportmanagement);
	        
	        RoundedButton btnItemInventoryArchive = new RoundedButton("Item Inventory Archive", new Color(220, 20, 60), 0.8f, 11);
	        btnItemInventoryArchive.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		AdminItemStatusManagementArchive frame = new AdminItemStatusManagementArchive(adminId, adminUI);
	        			frame.setVisible(true);
	        			dispose(); // Close the current frame
	        	}
	        });
	        btnItemInventoryArchive.setText("Item Inventory Archive");
	        btnItemInventoryArchive.setForeground(Color.BLACK);
	        btnItemInventoryArchive.setFont(new Font("Tahoma", Font.BOLD, 11));
	        btnItemInventoryArchive.setBounds(100, 404, 172, 22);
	        background.add(btnItemInventoryArchive);
	        
	}
}
