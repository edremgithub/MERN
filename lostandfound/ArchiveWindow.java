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

public class ArchiveWindow extends JFrame { 

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    
	private String adminId;
	private AdminUI adminUI;
	private int itemId;
	private JFrame previousWindow;
	private String itemType; // "lost", "found", "claim", "stored"

	public ArchiveWindow(String adminId, AdminUI adminUI, int itemId, JFrame previousWindow, String itemType) {
		this.adminId = adminId;
		this.adminUI = adminUI;
		this.itemId = itemId;
		this.previousWindow = previousWindow;
		this.itemType = itemType;
		setResizable(false);
		setTitle("Archive - KLD Lost & Found");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 460, 408);		 
		setLocationRelativeTo(null);

		// App icon
		setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

		// Background
		BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/WarningArchive.jpg");
		background.setLayout(null);
		setContentPane(background);

		RoundedButton btnCancel = new RoundedButton("Cancel", new Color(255, 255, 255), 0.8f, 11);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	 
				previousWindow.setVisible(true); // Show the previous window
				dispose(); // Close the archive window
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCancel.setBounds(97, 258, 95, 28);
		background.add(btnCancel);
		
		RoundedButton btnArchive = new RoundedButton("Archive", new Color(220, 20, 60), 0.8f, 11);
		btnArchive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
					switch(itemType) {
						case "lost":
							String sqlLostItem = "UPDATE lost_items SET archived = TRUE WHERE Lost_Item_ID = ?";
							java.sql.PreparedStatement psLostItem = conn.prepareStatement(sqlLostItem);
							psLostItem.setInt(1, itemId);
							psLostItem.executeUpdate();

							String sqlLostImg = "UPDATE lost_item_images SET archived = TRUE WHERE Lost_Item_ID = ?";
							java.sql.PreparedStatement psLostImg = conn.prepareStatement(sqlLostImg);
							psLostImg.setInt(1, itemId);
							psLostImg.executeUpdate();
							break;

						case "found":
							String sqlFoundItem = "UPDATE reported_found_items SET archived = TRUE WHERE item_id = ?";
							java.sql.PreparedStatement psFoundItem = conn.prepareStatement(sqlFoundItem);
							psFoundItem.setInt(1, itemId);
							psFoundItem.executeUpdate();

							String sqlFoundImg = "UPDATE reported_found_images SET archived = TRUE WHERE item_id = ?";
							java.sql.PreparedStatement psFoundImg = conn.prepareStatement(sqlFoundImg);
							psFoundImg.setInt(1, itemId);
							psFoundImg.executeUpdate();
							break;

						case "claim":
							String sqlClaimReq = "UPDATE claim_item_req SET archived = TRUE WHERE claim_id = ?";
							java.sql.PreparedStatement psClaimReq = conn.prepareStatement(sqlClaimReq);
							psClaimReq.setInt(1, itemId);
							psClaimReq.executeUpdate();

							String sqlClaimImg = "UPDATE claim_item_images SET archived = TRUE WHERE claim_id = ?";
							java.sql.PreparedStatement psClaimImg = conn.prepareStatement(sqlClaimImg);
							psClaimImg.setInt(1, itemId);
							psClaimImg.executeUpdate();
							break;

						case "stored":
							String sqlStoredItem = "UPDATE verified_found_items SET archived = TRUE WHERE item_id = ?";
							java.sql.PreparedStatement psStoredItem = conn.prepareStatement(sqlStoredItem);
							psStoredItem.setInt(1, itemId);
							psStoredItem.executeUpdate();

							String sqlStoredImg = "UPDATE verified_found_images SET archived = TRUE WHERE item_id = ?";
							java.sql.PreparedStatement psStoredImg = conn.prepareStatement(sqlStoredImg);
							psStoredImg.setInt(1, itemId);
							psStoredImg.executeUpdate();
							break;
					}

					javax.swing.JOptionPane.showMessageDialog(ArchiveWindow.this, "Item archived successfully!");
					
					// Navigate to appropriate page after successful archive
					switch(itemType) {
						case "lost":
							AdminReportLost adminReportLostItem = new AdminReportLost(adminId, adminUI);
							adminReportLostItem.setVisible(true);
							break;
						case "found":
							AdminReportFound adminReportFoundItem = new AdminReportFound(adminId, adminUI);
							adminReportFoundItem.setVisible(true);
							break;
						case "claim":
							AdminClaimReq adminClaimReq = new AdminClaimReq(adminId, adminUI);
							adminClaimReq.setVisible(true);
							break;
						case "stored":
							AdminItemStats adminItemStats = new AdminItemStats(adminId, adminUI);
							adminItemStats.setVisible(true);
							break;
					}
					dispose(); // Close the archive window
				} catch (java.sql.SQLException ex) {
					javax.swing.JOptionPane.showMessageDialog(ArchiveWindow.this, "Error archiving item: " + ex.getMessage());
				}
			}
		});
		btnArchive.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnArchive.setBounds(250, 258, 95, 28);
		background.add(btnArchive);
	}

}
