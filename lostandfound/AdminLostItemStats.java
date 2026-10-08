package kldLostAndFound;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AdminLostItemStats extends JFrame {

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
    private int lostItemId;

    public AdminLostItemStats(
        String adminId, 
        AdminUI adminUI, 
        String itemName, 
        String fullName, 
        String course, 
        String location, 
        String number, 
        String dateLost, 
        String description, 
        ImageIcon image,
        int lostItemId
    ) {
        this.adminId = adminId;
        this.adminUI = adminUI;
        this.lostItemId = lostItemId;

        setTitle("Admin Lost Item Status - KLD Lost & Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 562);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AdminLostItemStats.jpg");
        background.setLayout(null);
        setContentPane(background);

        // Archive Button
        RoundedButton btnArchive = new RoundedButton("Delete", new Color(220, 20, 60), 0.8f, 11);
        btnArchive.setText("Archive");		
        btnArchive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArchiveWindow archiveWindow = new ArchiveWindow(adminId, adminUI, lostItemId, AdminLostItemStats.this, "lost");
                archiveWindow.setVisible(true);
                dispose(); // Close the current window
            } 		
        });
        btnArchive.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnArchive.setBounds(83, 382, 126, 32);
        background.add(btnArchive); 	

        // Back Button
        RoundedButton btnBack = new RoundedButton("Back", new Color(0, 128, 55), 0.8f, 11);
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminReportLost adminReportLostItem = new AdminReportLost(adminId, adminUI);
                adminReportLostItem.setVisible(true);
                dispose();
            }
        });
        btnBack.setBounds(83, 429, 126, 32);
        background.add(btnBack);

        // Description TextArea (Rounded)
        RoundedTextArea textArea = new RoundedTextArea(5, 30);
        textArea.setEditable(false);
        textArea.setBounds(260, 367, 384, 112);
        textArea.setBackground(Color.WHITE);
        textArea.setText(description); // Set description
        background.add(textArea);

        // Upload Image Panel
        imagePanel = new UploadImagePanel(false);
        imagePanel.setBounds(67, 151, 159, 141);
        if (image != null) {
            imagePanel.setImage(image);
        }
        background.add(imagePanel);

        // Item Name
        textField_1 = new JTextField(itemName);
        textField_1.setEditable(false);
        textField_1.setBounds(264, 167, 159, 20);
        background.add(textField_1);

        // Full Name
        textField_2 = new JTextField(fullName);
        textField_2.setEditable(false);
        textField_2.setBounds(472, 167, 159, 20);
        background.add(textField_2);

        // Course
        textField_3 = new JTextField(course);
        textField_3.setEditable(false);
        textField_3.setBounds(264, 210, 159, 20);
        background.add(textField_3);

        // Location
        textField_4 = new JTextField(location);
        textField_4.setEditable(false);
        textField_4.setBounds(472, 210, 159, 20);
        background.add(textField_4);

        // Number
        textField_5 = new JTextField(number);
        textField_5.setEditable(false);
        textField_5.setBounds(264, 253, 159, 20);
        background.add(textField_5);

        // Date Lost
        textField_6 = new JTextField(dateLost);
        textField_6.setEditable(false);
        textField_6.setBounds(472, 253, 159, 20);
        background.add(textField_6);
    }

    // Utility method to add placeholder behavior to a JTextField
  
    }

