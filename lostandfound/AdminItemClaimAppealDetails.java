package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AdminItemClaimAppealDetails extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	 private UploadImagePanel imagePanel;
	 private RoundedTextField textField ,textField_1, textField_2;
	 private JTextField textField_3; 	// Item Name	
	    private JTextField textField_4; // Location	
	    private JTextField textField_5; // DateFound
     private String adminId;
     private AdminUI adminUI;
     private int appealId;
	

	public AdminItemClaimAppealDetails(
        String adminId, AdminUI adminUI,
        int appealId, int claimId, String fullName, String studentId, String courseYearSection,
        String appealDescription, String itemName, String location, String dateFound, String itemDescription, byte[] imageData
    ) {
        this.adminId = adminId;
        this.adminUI = adminUI;
        this.appealId = appealId;
        setTitle("Admin Approve Item Appeal - KLD Lost & Found"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 562);
        setResizable(false);
        setLocationRelativeTo(null); // Center the window on the screen
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());
        
        // Set background
        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AdminItemClaimAppealDetails.jpg");
        background.setLayout(null);
        setContentPane(background);
        
         // My Admin Account Button
        RoundedButton btnBack = new RoundedButton("Back", new Color(255,242,0), 0.8f, 11);
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminApproveFalseClaimItemAppeals adminApproveFalseClaimItemAppeals = new AdminApproveFalseClaimItemAppeals(adminId, adminUI);
                adminApproveFalseClaimItemAppeals.setVisible(true);
                dispose(); // Close the current window
            }
        });
        btnBack.setBounds(588, 16, 107, 36);
        btnBack.setForeground(Color.BLACK);
        background.add(btnBack);  // Add button to background panel, not contentPane
        
         // Upload Image Panel - aligned to your background
        imagePanel = new UploadImagePanel(false);
        imagePanel.setBounds(74, 155, 143, 130); // 👈 Replace with your actual bounds
        background.add(imagePanel);
        if (imageData != null) {
            imagePanel.setImage(new ImageIcon(imageData));
        }

        RoundedTextArea textArea = new RoundedTextArea(0,0);
        textArea.setEditable(false);
        textArea.setBounds(258, 234, 378, 48);
        textArea.setText(appealDescription != null ? appealDescription : "");
        background.add(textArea);

        // Student ID
        textField_1 = new RoundedTextField(1);
        textField_1.setEditable(false);
        textField_1.setColumns(10);
        textField_1.setBounds(272, 141, 364, 20);
        setPlaceholder(textField_1, "Student ID");
        textField_1.setText(studentId != null && !studentId.isEmpty() ? studentId : "");
        background.add(textField_1);

        // Full Name
        textField = new RoundedTextField(1);
        textField.setEditable(false);
        setPlaceholder(textField, "Full Name");
        textField.setText(fullName != null && !fullName.isEmpty() ? fullName : "");
        textField.setBounds(272, 172, 364, 20);
        textField.setColumns(10);
        background.add(textField);

        // Course/Year & Section
        textField_2 = new RoundedTextField(1);
        textField_2.setEditable(false);
        textField_2.setColumns(10);
        textField_2.setBounds(272, 203, 364, 20);
        setPlaceholder(textField_2, "Course/ Year & Section");
        textField_2.setText(courseYearSection != null && !courseYearSection.isEmpty() ? courseYearSection : "");
        background.add(textField_2);

        // Item Name
        textField_3 = new JTextField();
        textField_3.setEditable(false);
        textField_3.setBounds(272, 373, 175, 20);
        setPlaceholder(textField_3, "Item Name");
        textField_3.setText(itemName != null && !itemName.isEmpty() ? itemName : "");
        background.add(textField_3);

        // Location
        textField_4 = new JTextField();
        textField_4.setEditable(false);
        textField_4.setBounds(461, 373, 175, 20);
        setPlaceholder(textField_4, "Location");
        textField_4.setText(location != null && !location.isEmpty() ? location : "");
        background.add(textField_4);

        // Date Found
        textField_5 = new JTextField();
        textField_5.setEditable(false);
        textField_5.setBounds(272, 405, 176, 20);
        setPlaceholder(textField_5, "Date Found");
        textField_5.setText(dateFound != null && !dateFound.isEmpty() ? dateFound : "");
        background.add(textField_5);

        RoundedTextArea textArea_1 = new RoundedTextArea(0,0);
        textArea_1.setEditable(false);
        textArea_1.setBounds(265, 436, 371, 48);
        textArea_1.setText(itemDescription != null ? itemDescription : "");
        background.add(textArea_1);
        
        // Approve Button
        RoundedButton btnApprove = new RoundedButton("Approve", new Color(0, 128, 55), 0.8f, 11);
        btnApprove.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnApprove.setBounds(83, 390, 126, 27);
        btnApprove.addActionListener(e -> updateAppealStatus("approved"));
        background.add(btnApprove);

        // Decline Button
        RoundedButton btnDecline = new RoundedButton("Decline", new Color(220, 20, 60), 0.8f, 11);
        btnDecline.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnDecline.setBounds(83, 427, 126, 27);
        btnDecline.addActionListener(e -> updateAppealStatus("declined"));
        background.add(btnDecline);
    } 
            
        // Utility method to add placeholder behavior to a JTextField
        private void setPlaceholder(JTextField textField, String placeholder) {
            textField.setText(placeholder);
            textField.setForeground(Color.GRAY);

            textField.addFocusListener(new FocusAdapter() {
                @Override
    			public void focusGained(FocusEvent e) {
                    if (textField.getText().equals(placeholder)) {
                        textField.setText("");
                        textField.setForeground(Color.BLACK);
                    }
                }

                @Override
    			public void focusLost(FocusEvent e) {
                    if (textField.getText().isEmpty()) {
                        textField.setText(placeholder);
                        textField.setForeground(Color.GRAY);
                    }
                }
            });
	}

    // Update appeal status in the database
    private void updateAppealStatus(String newStatus) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to " + newStatus + " this appeal?",
            "Confirm",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
            String sql = "UPDATE appeal_requests SET status = ?, decision_date = NOW() WHERE appeal_id = ?";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newStatus);
                stmt.setInt(2, appealId);
                stmt.executeUpdate();
            }
            javax.swing.JOptionPane.showMessageDialog(this, "Appeal " + newStatus + " successfully!");
            // Return to dashboard
            AdminApproveFalseClaimItemAppeals dashboard = new AdminApproveFalseClaimItemAppeals(adminId, adminUI);
            dashboard.setVisible(true);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error updating appeal: " + ex.getMessage());
        }
    }
}
