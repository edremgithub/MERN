package kldLostAndFound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import java.awt.Font;

public class AdminAccountHistory extends JFrame {

    private static final long serialVersionUID = 1L;
    private String adminId;
    private AdminUI adminUI;
    private JList<String> list1;
    private JList<String> list2;
    private JList<String> list3;
    private JScrollPane scrollPane1, scrollPane2, scrollPane3;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lostandfound";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    

   

    public AdminAccountHistory( String adminId, AdminUI adminUI) {
		this.adminUI = adminUI;
		this.adminId = adminId;

		// Set the frame properties
		setTitle("Admin Account History - KLD Lost & Found");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 835, 584); // match your background size
		setLocationRelativeTo(null); // center on screen

		// App logo in the taskbar/icon
		setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

		BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AdminAccountHistory.jpg");
		setContentPane(background);
		background.setLayout(null);

		RoundedButton btnMyAccount = new RoundedButton("My Account", new Color(255, 224, 0), 0.8f, 11);
		btnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnMyAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminUI myAccount = new AdminUI(adminId);
				myAccount.setVisible(true);
				dispose();
			}
		});
		btnMyAccount.setBounds(685, 30, 107, 40);
		background.add(btnMyAccount);
    	setResizable(false);
        setTitle(" Admin Account History - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 835, 584); // match your background size
        setLocationRelativeTo(null); // center on screen

        // === First Rounded Panel ===
        RoundedPanel panel1 = new RoundedPanel(63);
        panel1.setBounds(47, 181, 203, 313);
        panel1.setLayout(new BorderLayout());
        background.add(panel1);

        scrollPane1 = createApproveClaimsScrollPane();
        panel1.add(scrollPane1, BorderLayout.CENTER);

        // === Second Rounded Panel ===
        RoundedPanel panel2 = new RoundedPanel(63);
        panel2.setBounds(308, 158, 204, 294);
        panel2.setLayout(new BorderLayout());
        background.add(panel2);

        scrollPane2 = createAdminLostItemsScrollPane();
        panel2.add(scrollPane2, BorderLayout.CENTER);

        // === Third Rounded Panel ===
        RoundedPanel panel3 = new RoundedPanel(63);
        panel3.setBounds(567, 175, 204, 313);
        panel3.setLayout(new BorderLayout());
        background.add(panel3);

        scrollPane3 = createAdminFoundItemsScrollPane();
        panel3.add(scrollPane3, BorderLayout.CENTER);
    }

    private JScrollPane createApproveClaimsScrollPane() {
        Vector<String> claimedItems = new Vector<>();
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT item_name, location_found, claim_date FROM claimed_items ORDER BY claim_date DESC";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {
                java.sql.ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String itemName = rs.getString("item_name");
                    String location = rs.getString("location_found");
                    java.sql.Timestamp claimDate = rs.getTimestamp("claim_date");
                    String formattedClaimDate = claimDate != null ? new java.text.SimpleDateFormat("MMM dd, yyyy").format(claimDate) : "Unknown date";
                    String itemDisplay = String.format("<html><b>%s</b><br>Found: %s<br>Claimed: %s</html>",
                        itemName != null ? itemName : "(No name)",
                        location != null ? location : "Location not specified",
                        formattedClaimDate);
                    claimedItems.add(itemDisplay);
                    // Debug print
                    System.out.println("Loaded claimed item: " + itemName + ", " + location + ", " + formattedClaimDate);
                }
                if (claimedItems.isEmpty()) {
                    System.out.println("No claimed items found in the database.");
                }
            }
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            claimedItems.add("Error loading claimed items");
        }
        list1 = new JList<>(claimedItems);
        list1.setOpaque(false);
        list1.setBackground(new Color(0, 0, 0, 0));
        list1.setForeground(Color.BLACK);
        list1.setFont(new Font("Tahoma", Font.PLAIN, 11));
        list1.setSelectionBackground(new Color(255, 255, 0, 100));
        list1.setSelectionForeground(Color.BLACK);
        list1.setFixedCellHeight(60);
        list1.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                javax.swing.JLabel label = (javax.swing.JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                label.setFont(new Font("Tahoma", Font.BOLD, 12));
                label.setOpaque(false);
                return label;
            }
        });
        JScrollPane scroll = new JScrollPane(list1);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        return scroll;
    }

    private JScrollPane createAdminLostItemsScrollPane() {
        Vector<String> lostItems = new Vector<>();
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT Item_Name, Location, Date_Lost FROM lost_items WHERE Admin_ID = ? ORDER BY Date_Lost DESC";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, adminId);
                java.sql.ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String itemName = rs.getString("Item_Name");
                    String location = rs.getString("Location");
                    java.sql.Date dateLost = rs.getDate("Date_Lost");
                    String formattedDateLost = dateLost != null ? new java.text.SimpleDateFormat("MMM dd, yyyy").format(dateLost) : "Unknown date";
                    String itemDisplay = String.format("<html><b>%s</b><br>Location: %s<br>Lost: %s</html>",
                        itemName != null ? itemName : "(No name)",
                        location != null ? location : "Location not specified",
                        formattedDateLost);
                    lostItems.add(itemDisplay);
                    // Debug print
                    System.out.println("Loaded admin lost item: " + itemName + ", " + location + ", " + formattedDateLost);
                }
                if (lostItems.isEmpty()) {
                    System.out.println("No lost items found for admin_id: " + adminId);
                }
            }
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            lostItems.add("Error loading lost items");
        }
        list2 = new JList<>(lostItems);
        list2.setOpaque(false);
        list2.setBackground(new Color(0, 0, 0, 0));
        list2.setForeground(Color.BLACK);
        list2.setFont(new Font("Tahoma", Font.PLAIN, 11));
        list2.setSelectionBackground(new Color(255, 255, 0, 100));
        list2.setSelectionForeground(Color.BLACK);
        list2.setFixedCellHeight(60);
        list2.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                javax.swing.JLabel label = (javax.swing.JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                label.setFont(new Font("Tahoma", Font.BOLD, 12));
                label.setOpaque(false);
                return label;
            }
        });
        JScrollPane scroll = new JScrollPane(list2);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        return scroll;
    }

    private JScrollPane createAdminFoundItemsScrollPane() {
        Vector<String> foundItems = new Vector<>();
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT item_name, location_found, date_found FROM verified_found_items WHERE admin_id = ? ORDER BY date_found DESC";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, adminId);
                java.sql.ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String itemName = rs.getString("item_name");
                    String location = rs.getString("location_found");
                    java.sql.Date dateFound = rs.getDate("date_found");
                    String formattedDateFound = dateFound != null ? new java.text.SimpleDateFormat("MMM dd, yyyy").format(dateFound) : "Unknown date";
                    String itemDisplay = String.format("<html><b>%s</b><br>Location: %s<br>Found: %s</html>",
                        itemName != null ? itemName : "(No name)",
                        location != null ? location : "Location not specified",
                        formattedDateFound);
                    foundItems.add(itemDisplay);
                    // Debug print
                    System.out.println("Loaded admin found item: " + itemName + ", " + location + ", " + formattedDateFound);
                }
                if (foundItems.isEmpty()) {
                    System.out.println("No found items found for admin_id: " + adminId);
                }
            }
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            foundItems.add("Error loading found items");
        }
        list3 = new JList<>(foundItems);
        list3.setOpaque(false);
        list3.setBackground(new Color(0, 0, 0, 0));
        list3.setForeground(Color.BLACK);
        list3.setFont(new Font("Tahoma", Font.PLAIN, 11));
        list3.setSelectionBackground(new Color(255, 255, 0, 100));
        list3.setSelectionForeground(Color.BLACK);
        list3.setFixedCellHeight(60);
        list3.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                javax.swing.JLabel label = (javax.swing.JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                label.setFont(new Font("Tahoma", Font.BOLD, 12));
                label.setOpaque(false);
                return label;
            }
        });
        JScrollPane scroll = new JScrollPane(list3);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        return scroll;
    }
}
