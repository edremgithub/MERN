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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class AppealClaimForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFrame frmKldLostAnd;
	 private UploadImagePanel imagePanel_Item;
	 private RoundedTextField textField ,textField_1, textField_2;
	 private JTextField textField_3; 	// Item Name	
	    private JTextField textField_4; // Location	
	    private JTextField textField_5; // DateFound
	    private String itemId;
	    private String idNumber;
	    private MyAccount myAccount;
	    private String itemName;
	    private String location;
	    private String dateFound;
	    private String itemDescription;
	    private String claimDescription;
	    private ImageIcon imageIcon;
	    private boolean isNewClaim = true;
	    private int claimId = -1;
	    private JTextField textField_studentid;
	    private JLabel hoverStudentIdLabel;

	public AppealClaimForm() {
		setResizable(false);
		setTitle("Appeal Claim - KLD Lost & Found");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 562);
		setLocationRelativeTo(null);

		// Set app icon
		setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

		// Set background
		BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AppealClaimForm.jpg");
		setContentPane(background);
		background.setLayout(null);

		// Upload Image Panel - aligned to your background
		imagePanel_Item = new UploadImagePanel(false);
		imagePanel_Item.setBounds(73, 152, 143, 130);
		background.add(imagePanel_Item);

		RoundedTextArea textArea = new RoundedTextArea(0,0);
		textArea.setBounds(258, 234, 378, 48);
		textArea.setText(itemDescription != null ? itemDescription : "");
		textArea.setEditable(false);
		background.add(textArea);

		// Adjusted and centered bottom text fields
		int containerWidth = 584; // width of the inner container (approximate)
		int fieldWidth = 143;
		int fieldHeight = 25;
		int gap = 10;
		int totalFieldsWidth = 3 * fieldWidth + 2 * gap;
		int leftMargin = (containerWidth - totalFieldsWidth) / 2 + 73; // 73 is the left offset of the container

		// Create a hover label (initially invisible, only once)
		hoverStudentIdLabel = new JLabel();
		hoverStudentIdLabel.setOpaque(true);
		hoverStudentIdLabel.setBackground(new Color(0, 0, 0, 210)); // black with higher opacity
		hoverStudentIdLabel.setForeground(Color.WHITE); // white text
		hoverStudentIdLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		hoverStudentIdLabel.setVisible(false);
		background.add(hoverStudentIdLabel, 0); // Add at the top of the z-order

		// Full Name
		textField = new RoundedTextField(1);
		setPlaceholder(textField, "Full Name");
		textField.setBounds(leftMargin, 353, fieldWidth, fieldHeight);
		textField.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				hoverStudentIdLabel.setText("Full Name");
				hoverStudentIdLabel.setBounds(
					textField.getX(),
					textField.getY() - 25,
					textField.getWidth(),
					20
				);
				background.remove(hoverStudentIdLabel);
				background.add(hoverStudentIdLabel, 0);
				hoverStudentIdLabel.setVisible(true);
				hoverStudentIdLabel.repaint();
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				hoverStudentIdLabel.setVisible(false);
			}
		});
		background.add(textField);

		// Student ID
		textField_1 = new RoundedTextField(1);
		setPlaceholder(textField_1, "Student ID");
		textField_1.setBounds(leftMargin + fieldWidth + gap, 353, fieldWidth, fieldHeight);
		textField_1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				hoverStudentIdLabel.setText("Student ID");
				hoverStudentIdLabel.setBounds(
					textField_1.getX(),
					textField_1.getY() - 25,
					textField_1.getWidth(),
					20
				);
				background.remove(hoverStudentIdLabel);
				background.add(hoverStudentIdLabel, 0);
				hoverStudentIdLabel.setVisible(true);
				hoverStudentIdLabel.repaint();
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				hoverStudentIdLabel.setVisible(false);
			}
		});
		background.add(textField_1);

		// Course/Year & Section
		textField_2 = new RoundedTextField(1);
		setPlaceholder(textField_2, "Course/ Year & Section");
		textField_2.setBounds(leftMargin + 2 * (fieldWidth + gap), 353, fieldWidth, fieldHeight);
		textField_2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				hoverStudentIdLabel.setText("Course/ Year & Section");
				hoverStudentIdLabel.setBounds(
					textField_2.getX(),
					textField_2.getY() - 25,
					textField_2.getWidth(),
					20
				);
				background.remove(hoverStudentIdLabel);
				background.add(hoverStudentIdLabel, 0);
				hoverStudentIdLabel.setVisible(true);
				hoverStudentIdLabel.repaint();
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				hoverStudentIdLabel.setVisible(false);
			}
		});
		background.add(textField_2);

		// Large text area for claim description
		int textAreaWidth = totalFieldsWidth;
		RoundedTextArea textArea_1 = new RoundedTextArea(0,0);
		textArea_1.setBounds(leftMargin, 389, textAreaWidth, 48);
		background.add(textArea_1);

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

		// Student ID from claimed_items (top right)
		textField_studentid = new JTextField();
		textField_studentid.setText((String) null);
		textField_studentid.setEditable(false);
		textField_studentid.setBounds(480, 203, 159, 20);
		textField_studentid.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				hoverStudentIdLabel.setText("Claimed by:");
				hoverStudentIdLabel.setBounds(
					textField_studentid.getX(),
					textField_studentid.getY() - 25,
					textField_studentid.getWidth(),
					20
				);
				background.remove(hoverStudentIdLabel);
				background.add(hoverStudentIdLabel, 0);
				hoverStudentIdLabel.setVisible(true);
				hoverStudentIdLabel.repaint();
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				hoverStudentIdLabel.setVisible(false);
			}
		});
		background.add(textField_studentid);
		// Ensure hover label is always on top
		background.setComponentZOrder(hoverStudentIdLabel, 0);

		// Appeal Button (centered below the text area)
		int btnWidth = 94;
		int btnHeight = 20;
		int btnX = leftMargin + (textAreaWidth - btnWidth) / 2;
		int btnY = 442;
		RoundedButton btnAppeal = new RoundedButton("Appeal", new Color(220, 20, 60), 0.8f, 11);
		btnAppeal.setBounds(btnX, btnY, btnWidth, btnHeight);
		btnAppeal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get and validate all fields
				String fullName = textField.getText().trim();
				String studentId = textField_1.getText().trim();
				String courseYearSection = textField_2.getText().trim();
				String claimDescription = textArea_1.getText().trim();
				byte[] imageBytes = null;

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
					AppealClaimForm.this,
					"Are you sure you want to submit this appeal request?",
					"Confirm Appeal Request",
					JOptionPane.YES_NO_OPTION
				);
				if (confirm != JOptionPane.YES_OPTION) {
					return;
				}

				try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
					// Check for existing appeal
					String checkSQL = "SELECT COUNT(*) FROM appeal_requests WHERE claim_id = ? AND student_id = ?";
					PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
					checkStmt.setInt(1, Integer.parseInt(itemId));
					checkStmt.setString(2, studentId);
					ResultSet checkRs = checkStmt.executeQuery();
					if (checkRs.next() && checkRs.getInt(1) > 0) {
						JOptionPane.showMessageDialog(null, "You have already filed an appeal for this item.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					checkStmt.close();

					// Insert new appeal request
					String insertAppealSQL = "INSERT INTO appeal_requests (claim_id, full_name, student_id, course_year_section, appeal_description) VALUES (?, ?, ?, ?, ?)";
					PreparedStatement ps = conn.prepareStatement(insertAppealSQL, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, Integer.parseInt(itemId));
					ps.setString(2, fullName);
					ps.setString(3, studentId);
					ps.setString(4, courseYearSection);
					ps.setString(5, claimDescription);
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					int newAppealId = -1;
					if (rs.next()) {
						newAppealId = rs.getInt(1);
					}
					ps.close();

					// Instead, show the FalseClaim success window
					FalseClaim falseClaim = new FalseClaim(idNumber, myAccount);
					falseClaim.setVisible(true);
					dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error submitting appeal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		background.add(btnAppeal);
		getRootPane().setDefaultButton(btnAppeal);

		
		
		RoundedButton btnBack = new RoundedButton("Back", new Color(0, 128, 55), 0.8f, 11);
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SearchItem searchItem = new SearchItem(idNumber, myAccount);
				searchItem.setVisible(true);
				dispose();
			}
		});
		btnBack.setBounds(585, 17, 99, 40);
		background.add(btnBack);

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

	public void setItemDetails(String itemId, String itemName, String location, String dateFound, String itemDescription, String idNumber, MyAccount myAccount) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.location = location;
		this.dateFound = dateFound;
		this.itemDescription = itemDescription;
		this.idNumber = idNumber;
		this.myAccount = myAccount;
		
		// Update the text fields
		if (textField_3 != null) textField_3.setText(itemName);
		if (textField_4 != null) textField_4.setText(location);
		if (textField_5 != null) textField_5.setText(dateFound);
		
		// Update the item description area
		for (java.awt.Component comp : getContentPane().getComponents()) {
			if (comp instanceof RoundedTextArea && comp.getBounds().y == 234) {
				((RoundedTextArea)comp).setText(itemDescription != null ? itemDescription : "");
			}
		}

		// Load item image for the claimed item from claimed_item_original_images using claim_id
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
		     PreparedStatement pstmt = conn.prepareStatement("SELECT image_data FROM claimed_item_original_images WHERE claim_id = ?")) {
			pstmt.setInt(1, Integer.parseInt(itemId)); // itemId here is claim_id
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				byte[] imageData = rs.getBytes("image_data");
				if (imageData != null) {
					imageIcon = new ImageIcon(imageData);
					if (imagePanel_Item != null) {
						imagePanel_Item.setImage(imageIcon);
						imagePanel_Item.repaint();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Pre-fill student information if available
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
			 PreparedStatement pstmt = conn.prepareStatement("SELECT Full_Name, Course, Year_Section FROM students WHERE Student_ID = ?")) {
			pstmt.setString(1, idNumber);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String fullName = rs.getString("Full_Name");
				String course = rs.getString("Course");
				String yearSection = rs.getString("Year_Section");
				if (textField != null && fullName != null) textField.setText(fullName);
				if (textField_1 != null) textField_1.setText(idNumber);
				if (textField_2 != null) textField_2.setText(((course != null ? course : "") + " " + (yearSection != null ? yearSection : "")).trim());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Fetch and set the student_id from claimed_items
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
			 PreparedStatement pstmt = conn.prepareStatement("SELECT student_id FROM claimed_items WHERE claim_id = ?")) {
			pstmt.setInt(1, Integer.parseInt(itemId));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String studentId = rs.getString("student_id");
				if (textField_studentid != null) textField_studentid.setText(studentId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
