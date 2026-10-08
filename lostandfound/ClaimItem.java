package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JLabel;

public class ClaimItem extends JFrame {

	private static final long serialVersionUID = 1L;
	private JFrame frmKldLostAnd;
	 private UploadImagePanel imagePanel_Item;
	 private RoundedTextField textField ,textField_1, textField_2;
	 private JTextField textField_3; 	// Item Name	
	    private JTextField textField_4; // Location	
	    private JTextField textField_5; // DateFound
	    private String itemId;
	    private String idNumber;
	    private MyAccount myAccount;
	    private int claimId;
	    private boolean isNewClaim;  // New flag to track if this is a new claim
	    private JLabel hoverLabel; // Add this line for the hover label
	    private JTextField textField_Name;

	public ClaimItem(String itemId, String itemName, String location, String dateFound, String description, ImageIcon imageIcon, String idNumber, int claimId, String reporterName) {
		this.itemId = itemId;
		this.idNumber = idNumber;
		this.claimId = claimId;
		this.isNewClaim = (claimId == -1);  // Set isNewClaim based on claimId
		setResizable(false);
		setTitle("Request Claim - KLD Lost & Found");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 562);
		setLocationRelativeTo(null);

		// Set app icon
		setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

		// Set background
		BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/ReqClaim.jpg");
		setContentPane(background);
		background.setLayout(null);

		// Create hover label
		hoverLabel = new JLabel();
		hoverLabel.setOpaque(true);
		hoverLabel.setBackground(new Color(0, 0, 0, 210)); // black with higher opacity
		hoverLabel.setForeground(Color.WHITE); // white text
		hoverLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		hoverLabel.setVisible(false);
		background.add(hoverLabel, 0); // Add at the top of the z-order

		// Upload Image Panel - aligned to your background
		imagePanel_Item = new UploadImagePanel(false);
		imagePanel_Item.setBounds(73, 152, 143, 130);
		if (imageIcon != null) {
			imagePanel_Item.setImage(imageIcon);
		}
		background.add(imagePanel_Item);

		UploadImagePanel imagePanel_Proof = new UploadImagePanel(true);
		imagePanel_Proof.setBounds(531, 352, 108, 109);
		imagePanel_Proof.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				hoverLabel.setText("Proof Image (Optional)");
				int labelWidth = 150;
				int labelX = imagePanel_Proof.getX() + (imagePanel_Proof.getWidth() - labelWidth) / 2;
				hoverLabel.setBounds(
					labelX,
					imagePanel_Proof.getY() - 25,
					labelWidth,
					20
				);
				background.remove(hoverLabel);
				background.add(hoverLabel, 0);
				hoverLabel.setVisible(true);
				hoverLabel.repaint();
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				hoverLabel.setVisible(false);
			}
		});
		background.add(imagePanel_Proof);

		RoundedTextArea textArea = new RoundedTextArea(0,0);
		textArea.setBounds(258, 234, 378, 48);
		textArea.setText(description != null ? description : "");
		background.add(textArea);

		textField = new RoundedTextField(1);
		setPlaceholder(textField, "Full Name");
		background.add(textField);
		textField.setBounds(73, 353, 143, 20);
		background.add(textField);
		textField.setColumns(10);
		
		// Add focus listener to update textField_Name
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String name = textField.getText().trim();
				if (!name.isEmpty() && !name.equals("Full Name")) {
					textField_Name.setText(name);
				}
			}
		});

		textField_1 = new RoundedTextField(1);
		textField_1.setColumns(10);
		textField_1.setBounds(225, 353, 143, 20);
		background.add(textField_1);
		// Prefill Student ID if idNumber is provided
		if (idNumber != null && !idNumber.isEmpty()) {
			textField_1.setText(idNumber);
			textField_1.setForeground(Color.BLACK);
		} else {
			setPlaceholder(textField_1, "Student ID");
		}
		background.add(textField_1);

		textField_2 = new RoundedTextField(1);
		textField_2.setColumns(10);
		textField_2.setBounds(378, 353, 143, 20);
		background.add(textField_2);
		setPlaceholder(textField_2, "Course/ Year & Section");
		background.add(textField_2);
		
		// Item Name
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setBounds(258, 172, 159, 20);
		textField_3.setText(itemName);
		background.add(textField_3);
		
		// Location
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setBounds(480, 172, 159, 20);
		textField_4.setText(location);
		background.add(textField_4);
		
		// Date Found
		textField_5 = new JTextField();
		textField_5.setEditable(false);
		textField_5.setBounds(258, 203, 159, 20);
		textField_5.setText(dateFound);
		background.add(textField_5);

		RoundedTextArea textArea_1 = new RoundedTextArea(0,0);
		textArea_1.setBounds(73, 389, 448, 48);
		background.add(textArea_1);


		RoundedButton btnClaim = new RoundedButton("Claim Item", new Color(0, 128, 55), 0.8f, 11);
		btnClaim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Only proceed if this was triggered by the button click
				if (e.getSource() != btnClaim) {
					return;
				}

				// Get and validate all fields
				String fullName = textField.getText().trim();
				String studentId = textField_1.getText().trim();
				String courseYearSection = textField_2.getText().trim();
				String claimDescription = textArea_1.getText().trim();
				byte[] imageBytes = null;
				if (imagePanel_Proof != null) {
					imageBytes = imagePanel_Proof.getImageBytes();
				}

				// Validate all fields and image
				if (fullName.isEmpty() || fullName.equals("Full Name")) {
					JOptionPane.showMessageDialog(null, "Please enter your full name.", "Error", JOptionPane.ERROR_MESSAGE);
					textField.requestFocus();
					return;
				}
				if (studentId.isEmpty() || studentId.equals("Student ID")) {
					JOptionPane.showMessageDialog(null, "Please enter your student ID.", "Error", JOptionPane.ERROR_MESSAGE);
					textField_1.requestFocus();
					return;
				}
				if (courseYearSection.isEmpty() || courseYearSection.equals("Course/ Year & Section")) {
					JOptionPane.showMessageDialog(null, "Please enter your course, year and section.", "Error", JOptionPane.ERROR_MESSAGE);
					textField_2.requestFocus();
					return;
				}
				if (claimDescription.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter a claim description.", "Error", JOptionPane.ERROR_MESSAGE);
					textArea_1.requestFocus();
					return;
				}

				// Confirm with user before submitting
				int confirm = JOptionPane.showConfirmDialog(
					ClaimItem.this,
					"Are you sure you want to submit this claim request?",
					"Confirm Claim Request",
					JOptionPane.YES_NO_OPTION
				);
				if (confirm != JOptionPane.YES_OPTION) {
					return;
				}

				try (java.sql.Connection conn = java.sql.DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
					
					// Only create a new claim if this is a new claim AND the button was clicked
					if (isNewClaim && e.getSource() == btnClaim) {
						// Insert new claim request
						String insertClaimSQL = "INSERT INTO claim_item_req (item_id, full_name, student_id, course_year_section, claim_description) VALUES (?, ?, ?, ?, ?)";
						java.sql.PreparedStatement ps = conn.prepareStatement(insertClaimSQL, java.sql.Statement.RETURN_GENERATED_KEYS);
						ps.setInt(1, Integer.parseInt(itemId));
						ps.setString(2, fullName);
						ps.setString(3, studentId);
						ps.setString(4, courseYearSection);
						ps.setString(5, claimDescription);
						ps.executeUpdate();
						java.sql.ResultSet rs = ps.getGeneratedKeys();
						int newClaimId = -1;
						if (rs.next()) {
							newClaimId = rs.getInt(1);
						}
						ps.close();
						// Insert claim image
						if (imageBytes != null && newClaimId != -1) {
							String insertImageSQL = "INSERT INTO claim_item_images (claim_id, image_data) VALUES (?, ?)";
							java.sql.PreparedStatement psImg = conn.prepareStatement(insertImageSQL);
							psImg.setInt(1, newClaimId);
							psImg.setBytes(2, imageBytes);
							psImg.executeUpdate();
							psImg.close();
						}
					} else if (!isNewClaim && claimId > 0) {
						// Update existing claim
						String updateClaimSQL = "UPDATE claim_item_req SET full_name = ?, student_id = ?, course_year_section = ?, claim_description = ? WHERE claim_id = ?";
						java.sql.PreparedStatement ps = conn.prepareStatement(updateClaimSQL);
						ps.setString(1, fullName);
						ps.setString(2, studentId);
						ps.setString(3, courseYearSection);
						ps.setString(4, claimDescription);
						ps.setInt(5, claimId);
						ps.executeUpdate();
						ps.close();
						// Update claim image
						if (imageBytes != null) {
							String updateImageSQL = "UPDATE claim_item_images SET image_data = ? WHERE claim_id = ?";
							java.sql.PreparedStatement psImg = conn.prepareStatement(updateImageSQL);
							psImg.setBytes(1, imageBytes);
							psImg.setInt(2, claimId);
							psImg.executeUpdate();
							psImg.close();
						}
					} else {
						throw new IllegalStateException("Invalid claim state");
					}

					ClaimRequest claimRequest = new ClaimRequest(idNumber);
					claimRequest.setVisible(true);
					dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error submitting claim: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnClaim.setBounds(246, 442, 94, 20);
		background.add(btnClaim);
		getRootPane().setDefaultButton(btnClaim);

		
		
		RoundedButton btnMyAccount = new RoundedButton("Back",new Color(0, 128, 55), 0.8f, 11);
		btnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnMyAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Open My Account window
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							SearchItem searchItem = new SearchItem(idNumber, myAccount);
							searchItem.setVisible(true);
							dispose();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnMyAccount.setBounds(585, 17, 99, 40);
		background.add(btnMyAccount);
		
		textField_Name = new JTextField();
		textField_Name.setText(reporterName != null ? reporterName : "");
		textField_Name.setEditable(false);
		textField_Name.setBounds(480, 203, 159, 20);
		textField_Name.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				hoverLabel.setText("Reported By:");
				hoverLabel.setBounds(
					textField_Name.getX(),
					textField_Name.getY() - 25,
					textField_Name.getWidth(),
					20
				);
				background.remove(hoverLabel);
				background.add(hoverLabel, 0);
				hoverLabel.setVisible(true);
				hoverLabel.repaint();
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				hoverLabel.setVisible(false);
			}
		});
		background.add(textField_Name);

		// Pre-fill student information if available (identical to AppealClaimForm)
		try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
			 java.sql.PreparedStatement pstmt = conn.prepareStatement("SELECT Full_Name, Course, Year_Section FROM students WHERE Student_ID = ?")) {
			if (idNumber != null && !idNumber.isEmpty()) {
				pstmt.setString(1, idNumber);
				java.sql.ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					String fullName = rs.getString("Full_Name");
					String course = rs.getString("Course");
					String yearSection = rs.getString("Year_Section");
					if (textField != null && fullName != null) {
						textField.setText(fullName);
						textField.setForeground(Color.BLACK);
					}
					if (textField_1 != null) {
						textField_1.setText(idNumber);
						textField_1.setForeground(Color.BLACK);
					}
					if (textField_2 != null) {
						textField_2.setText(((course != null ? course : "") + " " + (yearSection != null ? yearSection : "")).trim());
						textField_2.setForeground(Color.BLACK);
					}
				}
			}
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
		// Fetch and set the student_id from claimed_items if claimId > 0
		if (claimId > 0) {
			try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
				 java.sql.PreparedStatement pstmt = conn.prepareStatement("SELECT student_id FROM claimed_items WHERE claim_id = ?")) {
				pstmt.setInt(1, claimId);
				java.sql.ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					String studentId = rs.getString("student_id");
					if (textField_1 != null && studentId != null) {
						textField_1.setText(studentId);
						textField_1.setForeground(Color.BLACK);
					}
				}
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
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
		});
	}
}