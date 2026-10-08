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
import java.sql.Statement;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class AdminApproveClaimReq extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private UploadImagePanel imagePanel;
	private RoundedTextField textField ,textField_1, textField_2;
	private JTextField textField_3; // Item Name
	private JTextField textField_4; // Location
	private JTextField textField_5; // DateFound
	private String adminId;
	private AdminUI adminUI;
	private int itemId;
	private byte[] claimImageBytes;
	private int claimId;

	public AdminApproveClaimReq(
		String adminId, AdminUI adminUI,
		int claimId,
		int itemId, String studentId, String fullName, String courseYearSection, String claimDescription,
		String itemName, String location, String dateFound, String itemDescription,
		javax.swing.ImageIcon claimImage, byte[] claimImageBytes, javax.swing.ImageIcon verifiedItemImage
	) {
		this.adminId = adminId;
		this.adminUI = adminUI;
		this.claimId = claimId;	 
		this.itemId = itemId;
		this.claimImageBytes = claimImageBytes;
		setTitle("Admin Approved Claim Request - KLD Lost & Found"); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 730, 562);
		setResizable(false);
		setLocationRelativeTo(null); // Center the window on the screen
		setIconImage(new javax.swing.ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

		// Set background
		BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AdminApproveClaimReq.jpg");
		background.setLayout(null);
		setContentPane(background);

		RoundedButton btnBack = new RoundedButton("Back", new java.awt.Color(255,242,0), 0.8f, 11);
		btnBack.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 11));
		btnBack.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				AdminClaimReq frame = new AdminClaimReq(adminId, adminUI);
				frame.setVisible(true);
				dispose();
			}
		});
		btnBack.setBounds(588, 16, 107, 36);
		btnBack.setForeground(java.awt.Color.BLACK);
		background.add(btnBack);

		// Upload Image Panel - verified item image (now on the left)
		imagePanel = new UploadImagePanel(false);
		imagePanel.setBounds(74, 155, 143, 130);
		if (verifiedItemImage != null) imagePanel.setImage(verifiedItemImage);
		background.add(imagePanel);

		// Upload Image Panel - claim image (now on the right)
		UploadImagePanel imagePanel_1 = new UploadImagePanel(false);
		imagePanel_1.setBounds(542, 141, 94, 82);
		if (claimImage != null) {
			imagePanel_1.setImage(claimImage);
		} else {
			JLabel noProofLabel = new JLabel("No proof image", SwingConstants.CENTER);
			noProofLabel.setForeground(Color.GRAY);
			noProofLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
			noProofLabel.setBounds(0, 0, 94, 82);
			imagePanel_1.setLayout(null);
			imagePanel_1.add(noProofLabel);
		}
		background.add(imagePanel_1);

		// Claim description
		RoundedTextArea textArea = new RoundedTextArea(0,0);
		textArea.setEditable(false);
		textArea.setBounds(258, 234, 378, 48);
		textArea.setText(claimDescription != null ? claimDescription : "");
		background.add(textArea);

		// Full Name
		textField = new RoundedTextField(1);
		textField.setEditable(false);
		textField.setText(fullName != null ? fullName : "");
		background.add(textField);
		textField.setBounds(258, 172, 240, 20);
		textField.setColumns(10);

		// Student ID
		textField_1 = new RoundedTextField(1);
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(258, 141, 240, 20);
		textField_1.setText(studentId != null ? studentId : "");
		background.add(textField_1);

		// Course/Year & Section
		textField_2 = new RoundedTextField(1);
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		textField_2.setBounds(258, 203, 240, 20);
		textField_2.setText(courseYearSection != null ? courseYearSection : "");
		background.add(textField_2);

		// Item Name
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setBounds(272, 373, 175, 20);
		textField_3.setText(itemName != null ? itemName : "");
		background.add(textField_3);

		// Location
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setBounds(461, 373, 175, 20);
		textField_4.setText(location != null ? location : "");
		background.add(textField_4);

		// Date Found
		textField_5 = new JTextField();
		textField_5.setEditable(false);
		textField_5.setBounds(272, 405, 176, 20);
		textField_5.setText(dateFound != null ? dateFound : "");
		background.add(textField_5);

		// Item Description
		RoundedTextArea textArea_1 = new RoundedTextArea(0,0);
		textArea_1.setEditable(false);
		textArea_1.setBounds(265, 436, 371, 48);
		textArea_1.setText(itemDescription != null ? itemDescription : "");
		background.add(textArea_1);

		RoundedButton btnDecline = new RoundedButton("Archive", new java.awt.Color(220, 20, 60), 0.8f, 11);
		btnDecline.setText("Decline");
		btnDecline.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 13));
		btnDecline.setBounds(83, 417, 126, 27);
		background.add(btnDecline);
		
		// ... existing code ...
		btnDecline.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int confirm = javax.swing.JOptionPane.showConfirmDialog(
		            AdminApproveClaimReq.this,
		            "Are you sure you want to decline and remove this claim request?",
		            "Confirm Decline",
		            javax.swing.JOptionPane.YES_NO_OPTION
		        );
		        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
		            return;
		        }
		        Connection conn = null;
		        try {
		            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
		            conn.setAutoCommit(false);

		            // Get claim_id for this item
		            PreparedStatement psReq = conn.prepareStatement("SELECT claim_id FROM claim_item_req WHERE item_id = ?");
		            psReq.setInt(1, itemId);
		            ResultSet rsReq = psReq.executeQuery();
		            int claimId = -1;
		            if (rsReq.next()) {
		                claimId = rsReq.getInt("claim_id");
		            } else {
		                throw new Exception("Claim request not found.");
		            }

		            // 1. Delete from claim_item_images (child)
		            PreparedStatement delImg = conn.prepareStatement("DELETE FROM claim_item_images WHERE claim_id = ?");
		            delImg.setInt(1, claimId);
		            delImg.executeUpdate();

		            // 2. Delete from claim_item_req (parent)
		            PreparedStatement delReq = conn.prepareStatement("DELETE FROM claim_item_req WHERE claim_id = ?");
		            delReq.setInt(1, claimId);
		            delReq.executeUpdate();

		            conn.commit();
		            AdminClaimReq frame = new AdminClaimReq(adminId, adminUI);
					frame.setVisible(true);
					dispose(); // Close the current frame
		        } catch (Exception ex) {
		            if (conn != null) try { conn.rollback(); } catch (Exception ignore) {}
		            ex.printStackTrace();
		            javax.swing.JOptionPane.showMessageDialog(AdminApproveClaimReq.this, "Error: " + ex.getMessage());
		        } finally {
		            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (Exception ignore) {}
		        }
		    }
		});
		// ... existing code ...

		RoundedButton btnApprove = new RoundedButton("Archive", new java.awt.Color(0, 128, 55), 0.8f, 11);
		btnApprove.setText("Approve");
		btnApprove.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 13));
		btnApprove.setBounds(83, 388, 126, 27);
		background.add(btnApprove);

		// Approve button logic
		btnApprove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection conn = null;
				try {
					conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
					conn.setAutoCommit(false); // Start transaction

					// 1. Fetch claim request data
					PreparedStatement psReq = conn.prepareStatement("SELECT * FROM claim_item_req WHERE item_id = ?");
					psReq.setInt(1, itemId);
					ResultSet rsReq = psReq.executeQuery();

					if (!rsReq.next()) {
						throw new Exception("Claim request not found.");
					}

					int currentClaimId = rsReq.getInt("claim_id");

					// 2. Delete all other claim requests for the same item
					PreparedStatement delOtherClaims = conn.prepareStatement(
						"DELETE FROM claim_item_images WHERE claim_id IN (SELECT claim_id FROM claim_item_req WHERE item_id = ? AND claim_id != ?)"
					);
					delOtherClaims.setInt(1, itemId);
					delOtherClaims.setInt(2, currentClaimId);
					delOtherClaims.executeUpdate();

					PreparedStatement delOtherReq = conn.prepareStatement(
						"DELETE FROM claim_item_req WHERE item_id = ? AND claim_id != ?"
					);
					delOtherReq.setInt(1, itemId);
					delOtherReq.setInt(2, currentClaimId);
					delOtherReq.executeUpdate();

					// 3. Insert into claimed_items
					String insertClaimedSQL = "INSERT INTO claimed_items (item_id, full_name, student_id, course_year_section, claim_description, claim_date, item_name, location_found, date_found, item_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					PreparedStatement ps = conn.prepareStatement(insertClaimedSQL, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, rsReq.getInt("item_id"));
					ps.setString(2, rsReq.getString("full_name"));
					ps.setString(3, rsReq.getString("student_id"));
					ps.setString(4, rsReq.getString("course_year_section"));
					ps.setString(5, rsReq.getString("claim_description"));
					ps.setTimestamp(6, rsReq.getTimestamp("claim_date"));
					ps.setString(7, textField_3.getText()); // item_name
					ps.setString(8, textField_4.getText()); // location_found
					ps.setString(9, textField_5.getText()); // date_found
					ps.setString(10, textArea_1.getText()); // item_description
					ps.executeUpdate();

					ResultSet rs = ps.getGeneratedKeys();
					int claimId = 0;
					if (rs.next()) {
						claimId = rs.getInt(1);
					}

					// 4. Insert claim image (proof image)
					PreparedStatement psImgReq = conn.prepareStatement("SELECT image_data FROM claim_item_images WHERE claim_id = ?");
					psImgReq.setInt(1, rsReq.getInt("claim_id"));
					ResultSet rsImg = psImgReq.executeQuery();
					if (rsImg.next()) {
						byte[] imageData = rsImg.getBytes("image_data");
						String insertImageSQL = "INSERT INTO claimed_item_images (claim_id, image_data) VALUES (?, ?)";
						PreparedStatement psImg = conn.prepareStatement(insertImageSQL);
						psImg.setInt(1, claimId);
						psImg.setBytes(2, imageData);
						psImg.executeUpdate();
					}

					// Copy the item image from verified_found_images to claimed_item_original_images BEFORE deletion
					PreparedStatement psItemImg = conn.prepareStatement("SELECT image_data FROM verified_found_images WHERE item_id = ?");
					psItemImg.setInt(1, itemId);
					ResultSet rsItemImg = psItemImg.executeQuery();
					if (rsItemImg.next()) {
						byte[] itemImageData = rsItemImg.getBytes("image_data");
						System.out.println("Copying item image for item_id " + itemId + " to claim_id " + claimId + ", image length: " + (itemImageData != null ? itemImageData.length : "null"));
						String insertItemImageSQL = "INSERT INTO claimed_item_original_images (claim_id, image_data) VALUES (?, ?)";
						PreparedStatement psInsertItemImg = conn.prepareStatement(insertItemImageSQL);
						psInsertItemImg.setInt(1, claimId);
						psInsertItemImg.setBytes(2, itemImageData);
						psInsertItemImg.executeUpdate();
						psInsertItemImg.close();
					} else {
						System.out.println("No item image found for item_id " + itemId + " in verified_found_images.");
					}
					psItemImg.close();

					// 5. Delete the current claim request and its image (to remove the 'husk')
					PreparedStatement delImg = conn.prepareStatement("DELETE FROM claim_item_images WHERE claim_id = ?");
					delImg.setInt(1, currentClaimId);
					delImg.executeUpdate();

					PreparedStatement delReq = conn.prepareStatement("DELETE FROM claim_item_req WHERE claim_id = ?");
					delReq.setInt(1, currentClaimId);
					delReq.executeUpdate();

					// 6. Delete from verified_found_items and verified_found_images
					PreparedStatement delFound = conn.prepareStatement("DELETE FROM verified_found_items WHERE item_id = ?");
					delFound.setInt(1, itemId);
					delFound.executeUpdate();

					PreparedStatement delFoundImg = conn.prepareStatement("DELETE FROM verified_found_images WHERE item_id = ?");
					delFoundImg.setInt(1, itemId);
					delFoundImg.executeUpdate();

					conn.commit(); // Commit transaction

				AdminClaimReq frame = new AdminClaimReq(adminId, adminUI);
					frame.setVisible(true);
					dispose(); // Close the current frame
				} catch (Exception ex) {
					if (conn != null) try { conn.rollback(); } catch (Exception ignore) {}
					ex.printStackTrace();
					javax.swing.JOptionPane.showMessageDialog(AdminApproveClaimReq.this, "Error: " + ex.getMessage());
				} finally {
					if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (Exception ignore) {}
				}
			}
		});
	}

	// Utility to convert ImageIcon to byte[]
	public static byte[] imageIconToBytes(javax.swing.ImageIcon icon) {
		if (icon == null) return null;
		java.awt.Image img = icon.getImage();
		BufferedImage bImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bImage.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bImage, "jpg", bos);
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
