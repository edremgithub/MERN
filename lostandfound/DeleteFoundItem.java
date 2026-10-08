package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DeleteFoundItem extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    	private String adminId;
    		private AdminUI adminUI;
    	private int itemId;
    	private String itemType;
	public DeleteFoundItem(String adminId, AdminUI adminUI, int itemId, String itemType) {
		this.adminId = adminId;
		this.adminUI = adminUI;
		this.itemId = itemId;
		this.itemType = itemType;
		setResizable(false);	 
		setTitle("Warning!!");	 
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 347, 316);
		setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());
		setLocationRelativeTo(null);

		BackgroundPanel contentPane = new BackgroundPanel("src/kldLostAndFound/images/DELETE.jpg");
		setContentPane(contentPane);
		contentPane.setLayout(null);
			 
		RoundedButton btnCancel = new RoundedButton("Cancel", new Color(255, 255, 255), 0.8f, 11);
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCancel.setBounds(58, 207, 89, 23);
		contentPane.add(btnCancel);
		
		RoundedButton btnDelete = new RoundedButton("Delete", new Color(220, 20, 60), 0.8f, 11);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = javax.swing.JOptionPane.showConfirmDialog(DeleteFoundItem.this, "Are you sure you want to permanently delete this item?", "Confirm Delete", javax.swing.JOptionPane.YES_NO_OPTION);
				if (confirm == javax.swing.JOptionPane.YES_OPTION) {
					try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
						switch(itemType) {
							case "lost":
								try (java.sql.PreparedStatement psImg = conn.prepareStatement("DELETE FROM lost_item_images WHERE Lost_Item_ID = ?")) {
									psImg.setInt(1, itemId);
									psImg.executeUpdate();
								}
								try (java.sql.PreparedStatement psItem = conn.prepareStatement("DELETE FROM lost_items WHERE Lost_Item_ID = ?")) {
									psItem.setInt(1, itemId);
									psItem.executeUpdate();
								}
								break;
							case "found":
								try (java.sql.PreparedStatement psImg = conn.prepareStatement("DELETE FROM reported_found_images WHERE item_id = ?")) {
									psImg.setInt(1, itemId);
									psImg.executeUpdate();
								}
								try (java.sql.PreparedStatement psItem = conn.prepareStatement("DELETE FROM reported_found_items WHERE item_id = ?")) {
									psItem.setInt(1, itemId);
									psItem.executeUpdate();
								}
								break;
							case "stored":
								try (java.sql.PreparedStatement psImg = conn.prepareStatement("DELETE FROM verified_found_images WHERE item_id = ?")) {
									psImg.setInt(1, itemId);
									psImg.executeUpdate();
								}
								try (java.sql.PreparedStatement psItem = conn.prepareStatement("DELETE FROM verified_found_items WHERE item_id = ?")) {
									psItem.setInt(1, itemId);
									psItem.executeUpdate();
								}
								break;
						}
						javax.swing.JOptionPane.showMessageDialog(DeleteFoundItem.this, "Item deleted successfully!");
						// Return to the correct archive window
						switch(itemType) {
							case "lost":
								AdminReportItemLostArchive lostArchive = new AdminReportItemLostArchive(adminId, adminUI);
								lostArchive.setVisible(true);
								break;
							case "found":
								AdminReportItemFoundArchive foundArchive = new AdminReportItemFoundArchive(adminId, adminUI);
								foundArchive.setVisible(true);
								break;
							case "stored":
								AdminItemStatusManagementArchive storedArchive = new AdminItemStatusManagementArchive(adminId, adminUI);
								storedArchive.setVisible(true);
								break;
						}
						dispose();
					} catch (Exception ex) {
						javax.swing.JOptionPane.showMessageDialog(DeleteFoundItem.this, "Error deleting item: " + ex.getMessage());
					}
				}
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDelete.setBounds(179, 207, 89, 23);
		contentPane.add(btnDelete);
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Return to the correct archive window
				switch(itemType) {
					case "lost":
						AdminReportItemLostArchive lostArchive = new AdminReportItemLostArchive(adminId, adminUI);
						lostArchive.setVisible(true);
						break;
					case "found":
						AdminReportItemFoundArchive foundArchive = new AdminReportItemFoundArchive(adminId, adminUI);
						foundArchive.setVisible(true);
						break;
					case "stored":
						AdminItemStatusManagementArchive storedArchive = new AdminItemStatusManagementArchive(adminId, adminUI);
						storedArchive.setVisible(true);
						break;
				}
				dispose();
			}
		});
	}

}
