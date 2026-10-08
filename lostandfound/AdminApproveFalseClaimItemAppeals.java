package kldLostAndFound;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AdminApproveFalseClaimItemAppeals extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	 private JTextField textField_SearchBarClaimReq;
	    private RoundedTextArea text_1;
	    private RoundedShadowLabel hoverLabel;
	    private RoundedTextField textField_2;
	    private UploadImagePanel imagePanel_1;
		  private UploadImagePanel imagePanel_3;
		  private UploadImagePanel imagePanel_4;
		  private UploadImagePanel imagePanel_5;
		  private UploadImagePanel imagePanel_6;
		  private UploadImagePanel imagePanel_2;
		  private String adminId;
		  private AdminUI adminUI;
		  private BackgroundPanel background;

    // Add arrays for UI components
    private UploadImagePanel[] imagePanels = new UploadImagePanel[6];
    private RoundedTextArea[] itemNameAreas = new RoundedTextArea[6];
    private RoundedTextArea[] dateAreas = new RoundedTextArea[6];
    private RoundedTextArea[] studentNumberAreas = new RoundedTextArea[6];
    private RoundedTextArea[] nameAreas = new RoundedTextArea[6];
    // Store loaded appeals
    private List<AppealData> loadedAppeals = new ArrayList<>();

    // Data class for appeal details
    private static class AppealData {
        int appealId;
        int claimId;
        String fullName;
        String studentId;
        String courseYearSection;
        String appealDescription;
        String itemName;
        String locationFound;
        String dateFound;
        String itemDescription;
        byte[] imageData;
    }

	public AdminApproveFalseClaimItemAppeals( String adminId, AdminUI adminUI) {
			this.adminId = adminId;	
				this.adminUI = adminUI;
	        setResizable(false); // Disable resizing
		  setTitle("Admin Claim Request - KLD Lost & Found");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setBounds(100, 100, 730, 562);
	        setResizable(false);
	        setLocationRelativeTo(null); // Center the window on the screen
	        
	        // Set app icon		
	        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());
	        
	        // Set background
	        background = new BackgroundPanel("src/kldLostAndFound/images/ApproveFalseClaimAppeals.jpg");
	        background.setLayout(null);
	        
	        // Initialize contentPane and set background as content pane
	        contentPane = new JPanel();
	        contentPane.setLayout(null);  // Set layout as null
	        setContentPane(background);  // Set background panel as the content pane
	        
	        // Admin Options Button
	        RoundedButton btnadmin = new RoundedButton("Back", new Color(255, 240, 0), 0.8f, 11);
	        btnadmin.setText("Back");
	        btnadmin.setFont(new Font("Tahoma", Font.BOLD, 11));
	        btnadmin.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	               AdminClaimReq frame = new AdminClaimReq(adminId, adminUI);
	                frame.setVisible(true);
	                dispose(); 
	            }
	        });
	        btnadmin.setBounds(566, 16, 129, 36);
	        btnadmin.setForeground(Color.BLACK);
	        background.add(btnadmin);  // Add button to background panel, not contentPane
	        
	        // Upload Image Panel
	        imagePanel_2 = new UploadImagePanel(false);
	        imagePanel_2.setBounds(40, 259, 53, 47);
	        background.add(imagePanel_2);
	        
	        imagePanel_1 = new UploadImagePanel(false);
	        imagePanel_1.setBounds(40, 211, 53, 47);
	        background.add(imagePanel_1);
	        
	        imagePanel_3 = new UploadImagePanel(false);
	        imagePanel_3.setBounds(40, 308, 53, 47);
	        background.add(imagePanel_3);
	        
	        imagePanel_5 = new UploadImagePanel(false);
	        imagePanel_5.setBounds(40, 405, 53, 47);
	        background.add(imagePanel_5);
	        
	        imagePanel_4 = new UploadImagePanel(false);
	        imagePanel_4.setBounds(40, 356, 53, 47);
	        background.add(imagePanel_4);
	        
	        imagePanel_6 = new UploadImagePanel(false);
	        imagePanel_6.setBounds(40, 454, 53, 47);
	        background.add(imagePanel_6);
	        
	        // Text Field for the input
	        textField_SearchBarClaimReq = new RoundedTextField(5);
	        textField_SearchBarClaimReq.setBounds(75, 149, 458, 20);
	        background.add(textField_SearchBarClaimReq);
	        textField_SearchBarClaimReq.setColumns(10);
	        setPlaceholder(textField_SearchBarClaimReq, " 🔎 Search Claim Request");

	        // Initialize hover shadow label
	        hoverLabel = new RoundedShadowLabel();
	        hoverLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
	        hoverLabel.setForeground(Color.WHITE);
	        hoverLabel.setVisible(false);
	        background.add(hoverLabel);
	        background.setComponentZOrder(hoverLabel, 0); // Ensure it's on top

	        // Load icons for buttons
	        ImageIcon leftIcon = loadIcon("turnLeft.jpg", 22, 20);
	        ImageIcon rightIcon = loadIcon("turnRight.jpg", 22, 20);

	        // Left button (Previous)
	        JButton btnLeft = createHoverButton(leftIcon, "Previous");
	        btnLeft.setBounds(46, 149, 22, 20); // Adjust the position manually here
	        background.add(btnLeft);

	        // Right button (Next)
	        JButton btnRight = createHoverButton(rightIcon, "Next");
	        btnRight.setBounds(541, 149, 22, 20); // Adjust the position manually here
	        background.add(btnRight);
	        
	        text_1 = new RoundedTextArea(1 , 1);
			text_1.setBounds(98, 211, 93, 47);
			background.add(text_1);
	        
	        RoundedTextArea text_2 = new RoundedTextArea(1, 1);
			text_2.setBounds(98, 259, 93, 47);
			background.add(text_2);
			
			RoundedTextArea text_3 = new RoundedTextArea(1, 1);
			text_3.setBounds(98, 308, 93, 47);
			background.add(text_3);
			
			RoundedTextArea text_4 = new RoundedTextArea(1, 1);
			text_4.setBounds(98, 356, 93, 47);
			background.add(text_4);
			
			RoundedTextArea text_5 = new RoundedTextArea(1, 1);
			text_5.setBounds(98, 405, 93, 47);
			background.add(text_5);
			
			RoundedTextArea text_6 = new RoundedTextArea(1, 1);
			text_6.setBounds(98, 454, 93, 47);
			background.add(text_6);
			
			RoundedTextArea text_date_1 = new RoundedTextArea(1, 1);
			text_date_1.setBounds(192, 211, 80, 47);
			background.add(text_date_1);
			
			RoundedTextArea text_date_2 = new RoundedTextArea(1, 1);
			text_date_2.setBounds(192, 259, 80, 47);
			background.add(text_date_2);
			
			RoundedTextArea text_date_3 = new RoundedTextArea(1, 1);
			text_date_3.setBounds(192, 308, 80, 47);
			background.add(text_date_3);
			
			RoundedTextArea text_date_4 = new RoundedTextArea(1, 1);
			text_date_4.setBounds(192, 356, 80, 47);
			background.add(text_date_4);
			
			RoundedTextArea text_date_5 = new RoundedTextArea(1, 1);
			text_date_5.setBounds(192, 405, 80, 47);
			background.add(text_date_5);
			
			RoundedTextArea text_date_2_1 = new RoundedTextArea(1, 1);
			text_date_2_1.setBounds(192, 454, 80, 47);
			background.add(text_date_2_1);
			
			RoundedTextArea text_des_1 = new RoundedTextArea(1, 1);
			text_des_1.setBounds(273, 211, 168, 47);
			background.add(text_des_1);
			
			RoundedTextArea text_name_1 = new RoundedTextArea(1, 1);
			text_name_1.setBounds(442, 211, 129, 47);
			background.add(text_name_1);
			
			RoundedTextArea text_des_2 = new RoundedTextArea(1, 1);
			text_des_2.setBounds(273, 259, 168, 47);
			background.add(text_des_2);
			
			RoundedTextArea text_des_3 = new RoundedTextArea(1, 1);
			text_des_3.setBounds(273, 308, 168, 47);
			background.add(text_des_3);
			
			RoundedTextArea text_des_4 = new RoundedTextArea(1, 1);
			text_des_4.setBounds(273, 356, 168, 47);
			background.add(text_des_4);
			
			RoundedTextArea text_des_5 = new RoundedTextArea(1, 1);
			text_des_5.setBounds(273, 405, 168, 47);
			background.add(text_des_5);
			
			RoundedTextArea text_des_6 = new RoundedTextArea(1, 1);
			text_des_6.setBounds(273, 454, 168, 47);
			background.add(text_des_6);
			
			RoundedTextArea text_name_2 = new RoundedTextArea(1, 1);
			text_name_2.setBounds(442, 259, 129, 47);
			background.add(text_name_2);
			
			RoundedTextArea text_name_3 = new RoundedTextArea(1, 1);
			text_name_3.setBounds(442, 308, 129, 47);
			background.add(text_name_3);
			
			RoundedTextArea text_name_4 = new RoundedTextArea(1, 1);
			text_name_4.setBounds(442, 356, 129, 47);
			background.add(text_name_4);
			
			RoundedTextArea text_name_5 = new RoundedTextArea(1, 1);
			text_name_5.setBounds(442, 405, 129, 47);
			background.add(text_name_5);
			
			RoundedTextArea text_name_6 = new RoundedTextArea(1, 1);
			text_name_6.setBounds(442, 454, 129, 47);
			background.add(text_name_6);
			
			// Replace individual panel/textarea variables with arrays
			imagePanels[0] = imagePanel_1;
			imagePanels[1] = imagePanel_2;
			imagePanels[2] = imagePanel_3;
			imagePanels[3] = imagePanel_4;
			imagePanels[4] = imagePanel_5;
			imagePanels[5] = imagePanel_6;

			itemNameAreas[0] = text_name_1;
			itemNameAreas[1] = text_name_2;
			itemNameAreas[2] = text_name_3;
			itemNameAreas[3] = text_name_4;
			itemNameAreas[4] = text_name_5;
			itemNameAreas[5] = text_name_6;

			dateAreas[0] = text_date_1;
			dateAreas[1] = text_date_2;
			dateAreas[2] = text_date_3;
			dateAreas[3] = text_date_4;
			dateAreas[4] = text_date_5;
			dateAreas[5] = text_date_2_1;

			studentNumberAreas[0] = text_des_1;
			studentNumberAreas[1] = text_des_2;
			studentNumberAreas[2] = text_des_3;
			studentNumberAreas[3] = text_des_4;
			studentNumberAreas[4] = text_des_5;
			studentNumberAreas[5] = text_des_6;

			nameAreas[0] = text_1;
			nameAreas[1] = text_2;
			nameAreas[2] = text_3;
			nameAreas[3] = text_4;
			nameAreas[4] = text_5;
			nameAreas[5] = text_6;

			// Restore Approve buttons for each row
			for (int i = 0; i < 6; i++) {
				int y = 222 + i * 48; // Adjust the y-coordinate for each button
				RoundedButton btnVerify = new RoundedButton("Approve", new Color(220, 20, 60), 0.8f, 11);
				btnVerify.setFont(new Font("Tahoma", Font.BOLD, 11));
				btnVerify.setBounds(594, y, 83, 28);
				background.add(btnVerify);
				final int buttonIndex = i; // Capture the value of i for each button
				btnVerify.addActionListener(e -> handleAppealButtonClick(buttonIndex));
			}

			// Load appeals after UI setup
			loadAppeals();
		}

		private void loadAppeals() {
			loadedAppeals.clear();
			try (java.sql.Connection conn = java.sql.DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
				String sql = "SELECT a.appeal_id, a.claim_id, a.full_name, a.student_id, a.course_year_section, a.appeal_description, " +
						"c.item_name, c.location_found, c.date_found, c.item_description, ci.image_data " +
						"FROM appeal_requests a " +
						"JOIN claimed_items c ON a.claim_id = c.claim_id " +
						"LEFT JOIN claimed_item_original_images ci ON a.claim_id = ci.claim_id " +
						"WHERE a.status = 'pending' " +
						"ORDER BY a.created_at DESC LIMIT 6";
				try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
					java.sql.ResultSet rs = stmt.executeQuery();
					int row = 0;
					while (rs.next() && row < 6) {
						AppealData data = new AppealData();
						data.appealId = rs.getInt("appeal_id");
						data.claimId = rs.getInt("claim_id");
						data.fullName = rs.getString("full_name");
						data.studentId = rs.getString("student_id");
						data.courseYearSection = rs.getString("course_year_section");
						data.appealDescription = rs.getString("appeal_description");
						data.itemName = rs.getString("item_name");
						data.locationFound = rs.getString("location_found");
						data.dateFound = rs.getString("date_found");
						data.itemDescription = rs.getString("item_description");
						data.imageData = rs.getBytes("image_data");
						loadedAppeals.add(data);
						String itemName = rs.getString("item_name");
						String dateFound = rs.getString("date_found");
						String studentId = rs.getString("student_id");
						String fullName = rs.getString("full_name");
						byte[] imageData = rs.getBytes("image_data");

						// Set image
						if (imagePanels[row] != null) {
							if (imageData != null) {
								imagePanels[row].setImage(new ImageIcon(imageData));
							} else {
								imagePanels[row].setImage(null);
							}
						}
						// Set text fields
						if (itemNameAreas[row] != null) itemNameAreas[row].setText(itemName != null ? itemName : "N/A");
						if (dateAreas[row] != null) dateAreas[row].setText(dateFound != null ? dateFound : "N/A");
						if (studentNumberAreas[row] != null) studentNumberAreas[row].setText(studentId != null ? studentId : "N/A");
						if (nameAreas[row] != null) nameAreas[row].setText(fullName != null ? fullName : "N/A");
						row++;
					}
					// Clear remaining rows
					for (int i = row; i < 6; i++) {
						if (imagePanels[i] != null) imagePanels[i].setImage(null);
						if (itemNameAreas[i] != null) itemNameAreas[i].setText("");
						if (dateAreas[i] != null) dateAreas[i].setText("");
						if (studentNumberAreas[i] != null) studentNumberAreas[i].setText("");
						if (nameAreas[i] != null) nameAreas[i].setText("");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error loading appeals: " + e.getMessage());
			}
		}

	    // Load icon image with scaling
	    private ImageIcon loadIcon(String fileName, int width, int height) {
	        URL resource = getClass().getResource("/kldLostAndFound/images/" + fileName);
	        ImageIcon icon;

	        if (resource == null) {
	            System.err.println("Image not found: " + fileName);
	            icon = new ImageIcon("src/kldLostAndFound/images/" + fileName);
	            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
	                System.err.println("Image could not be loaded from local path either: " + fileName);
	                return null;
	            }
	        } else {
	            icon = new ImageIcon(resource);
	        }

	        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
	        return new ImageIcon(scaled);
	    }

	    // Create hover button with icon
	    private JButton createHoverButton(ImageIcon icon, String tooltip) {
	        JButton button = new RoundedIconButton(icon, 7);
	        button.setContentAreaFilled(false);
	        button.setBorderPainted(false);
	        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

	        // Initialize hover shadow label
	        RoundedShadowLabel hoverLabel = new RoundedShadowLabel();
	        hoverLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
	        hoverLabel.setForeground(Color.WHITE);
	        hoverLabel.setVisible(false);
	        background.add(hoverLabel);
	        background.setComponentZOrder(hoverLabel, 0);

	        button.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseEntered(MouseEvent e) {
	                button.setLocation(button.getX(), button.getY() - 2);
	                button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

	                hoverLabel.setText(tooltip);
	                hoverLabel.setFont(new Font("SansSerif", Font.BOLD, 10));

	                int labelWidth = hoverLabel.getPreferredSize().width - 45;
	                int labelHeight = hoverLabel.getPreferredSize().height - 2;
	                hoverLabel.setSize(labelWidth, labelHeight);
	                hoverLabel.setLocation(button.getX(), button.getY() - labelHeight - 3);

	                hoverLabel.setVisible(true);
	            }

	            @Override
	            public void mouseExited(MouseEvent e) {
	                button.setLocation(button.getX(), button.getY() + 2);
	                button.setBorder(null);
	                hoverLabel.setVisible(false);
	            }
	        });

	        return button;
	    }

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

	/**
	 * This method is called when any "Approve" button is clicked.
	 * It shows the details for the selected appeal.
	 * @param buttonIndex The index of the button that was clicked (0-5).
	 */
	private void handleAppealButtonClick(int buttonIndex) {
		if (buttonIndex < loadedAppeals.size()) {
			AppealData data = loadedAppeals.get(buttonIndex);
			AdminItemClaimAppealDetails frame = new AdminItemClaimAppealDetails(
				adminId, adminUI,
				data.appealId, data.claimId, data.fullName, data.studentId, data.courseYearSection,
				data.appealDescription, data.itemName, data.locationFound, data.dateFound, data.itemDescription, data.imageData
			);
			frame.setVisible(true);
			dispose();
		}
	}

}
