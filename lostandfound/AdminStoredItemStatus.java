package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

public class AdminStoredItemStatus extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private UploadImagePanel imagePanel;
    private JTextField textField_1; // Item Name
    private JTextField textField_2; // Full Name
    private JTextField textField_3; // Course
    private JTextField textField_4; // Location
    private JTextField textField_5; // Number
    private JTextField textField_6; // Date Lost
    private String adminId;
    private AdminUI adminUI;
    private RoundedTextArea textArea;
    private int itemId;

	
	public AdminStoredItemStatus(String adminId, AdminUI adminUI, String itemName, String fullName, String course, String location, String number, String dateFound, String description, ImageIcon image) {
		this.adminId = adminId;
		this.adminUI = adminUI;
		setTitle("Admin Stored Item Status - KLD Lost & Found");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 562);
		setResizable(false);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

		BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AdminStoredItemStatus.jpg");
		background.setLayout(null);
		setContentPane(background);

		RoundedButton btnArchive = new RoundedButton("Archive", new Color(220, 20, 60), 0.8f, 11);	
		btnArchive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArchiveWindow archiveWindow = new ArchiveWindow(adminId, adminUI, itemId, AdminStoredItemStatus.this, "stored");
				archiveWindow.setVisible(true);
				dispose(); // Close the current window
			}
		});	
		btnArchive.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnArchive.setBounds(83, 382, 126, 32);
		background.add(btnArchive);

		RoundedButton btnBack = new RoundedButton("Back", new Color(0, 128, 55), 0.8f, 11);
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminItemStats adminItemStats = new AdminItemStats(adminId, adminUI);
				adminItemStats.setVisible(true);
				dispose();
			}	 
		});
		btnBack.setBounds(83, 429, 126, 32);
		background.add(btnBack);

		textArea = new RoundedTextArea(5, 30);
		textArea.setEditable(false);
		textArea.setBounds(260, 367, 384, 112);
		textArea.setBackground(new Color(255, 255, 255));
		textArea.setText(description != null ? description : "");
		background.add(textArea);

		imagePanel = new UploadImagePanel(false);
		imagePanel.setBounds(68, 151, 159, 141);
		if (image != null) imagePanel.setImage(image);
		background.add(imagePanel);

		System.out.println("[DEBUG] AdminStoredItemStatus: itemName=" + itemName + ", fullName=" + fullName + ", course=" + course + ", location=" + location + ", number=" + number + ", dateFound=" + dateFound + ", description=" + description);

		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(264, 167, 159, 20);
		textField_1.setText(itemName != null ? itemName : "");
		textField_1.setForeground(java.awt.Color.BLACK);
		background.add(textField_1);

		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(472, 167, 159, 20);
		textField_2.setText(fullName != null ? fullName : "");
		textField_2.setForeground(java.awt.Color.BLACK);
		background.add(textField_2);

		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setBounds(264, 210, 159, 20);
		textField_3.setText(course != null ? course : "");
		textField_3.setForeground(java.awt.Color.BLACK);
		background.add(textField_3);

		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setBounds(472, 210, 159, 20);
		textField_4.setText(location != null ? location : "");
		textField_4.setForeground(java.awt.Color.BLACK);
		background.add(textField_4);

		textField_5 = new JTextField();
		textField_5.setEditable(false);
		textField_5.setBounds(264, 253, 159, 20);
		textField_5.setText(number != null ? number : "");
		textField_5.setForeground(java.awt.Color.BLACK);
		background.add(textField_5);

		textField_6 = new JTextField();
		textField_6.setEditable(false);
		textField_6.setBounds(472, 253, 159, 20);
		textField_6.setText(dateFound != null ? dateFound : "");
		textField_6.setForeground(java.awt.Color.BLACK);
		background.add(textField_6);
	}

	// Utility method to add placeholder behavior to a JTextField
	
	}

