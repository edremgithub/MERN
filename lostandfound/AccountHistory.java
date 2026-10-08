package kldLostAndFound;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import java.sql.*; // for future database use
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.Timestamp;

public class AccountHistory extends JFrame {

    private static final long serialVersionUID = 1L;

    private String currentIdNumber;
    private MyAccount myAccount;
    private JList<String> list1;
    private JList<String> list2;
    private JList<String> list3;
    private JScrollPane scrollPane1, scrollPane2, scrollPane3;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lostandfound";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public AccountHistory(String idNumber, MyAccount myAccount) {
        this.currentIdNumber = idNumber;
        this.myAccount = myAccount;
        initializeUI();
    }

    private void initializeUI() {
        setResizable(false);
        setTitle("Account History - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 835, 584);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AccountHistory.jpg");
        setContentPane(background);
        background.setLayout(null);

        // My Account Button
        RoundedButton btnMyAccount = new RoundedButton("My Account", new Color(0, 128, 55), 0.8f, 11);
        btnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnMyAccount.setBounds(685, 30, 107, 40);
        btnMyAccount.addActionListener(e -> {
            myAccount.setVisible(true);
            dispose();
        });
        background.add(btnMyAccount);

        // === First Rounded Panel ===
        RoundedPanel panel1 = new RoundedPanel(63);
        panel1.setBounds(47, 181, 203, 313);   // ITEMS CLAIMED (width +6, centered)
        panel1.setLayout(new BorderLayout());
        background.add(panel1);

        scrollPane1 = createClaimedItemsScrollPane();
        panel1.add(scrollPane1, BorderLayout.CENTER);

        // === Second Rounded Panel ===
        RoundedPanel panel2 = new RoundedPanel(63);
        panel2.setBounds(308, 158, 204, 294);  // ITEMS LOST (width +6, centered)
        panel2.setLayout(new BorderLayout());
        background.add(panel2);

        scrollPane2 = createLostItemsScrollPane();
        panel2.add(scrollPane2, BorderLayout.CENTER);

        // === Third Rounded Panel ===
        RoundedPanel panel3 = new RoundedPanel(63);
        panel3.setBounds(567, 175, 205, 313);  // ITEMS FOUND (width +6, centered)
        panel3.setLayout(new BorderLayout());
        background.add(panel3);

        scrollPane3 = createFoundItemsScrollPane();
        panel3.add(scrollPane3, BorderLayout.CENTER);

        // === Example JDBC Code (Commented for now) ===
        /*
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kld_db", "username", "password")) {
            String query = "SELECT item_name, item_status FROM account_history WHERE student_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, currentIdNumber);
            ResultSet rs = stmt.executeQuery();

            Vector<String> historyItems = new Vector<>();
            while (rs.next()) {
                String itemName = rs.getString("item_name");
                String itemStatus = rs.getString("item_status");
                historyItems.add(itemStatus + ": " + itemName);
            }
            list1.setListData(historyItems); // When you replace the sample list
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        */
    }

    private JScrollPane createClaimedItemsScrollPane() {
        Vector<String> claimedItems = new Vector<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT item_name, location_found, date_found, claim_date " +
                          "FROM claimed_items " +
                          "WHERE student_id = ? " +
                          "ORDER BY claim_date DESC";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, currentIdNumber);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String itemName = rs.getString("item_name");
                    String location = rs.getString("location_found");
                    Date foundDate = rs.getDate("date_found");
                    Timestamp claimDate = rs.getTimestamp("claim_date");
                    
                    // Format the date to be more readable
                    String formattedClaimDate = new SimpleDateFormat("MMM dd, yyyy").format(claimDate);
                    
                    // Create a formatted string for each item
                    String itemDisplay = String.format("<html><b>%s</b><br>Found: %s<br>Claimed: %s</html>",
                            itemName,
                            location != null ? location : "Location not specified",
                            formattedClaimDate);
                    
                    claimedItems.add(itemDisplay);
                    // Debug print
                    System.out.println("Loaded claimed item: " + itemName + ", " + location + ", " + formattedClaimDate);
                }
                if (claimedItems.isEmpty()) {
                    System.out.println("No claimed items found for student_id: " + currentIdNumber);
                }
            }
        } catch (SQLException ex) {
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
        list1.setFixedCellHeight(60); // Increased height to accommodate multiple lines

        // Set custom cell renderer for larger, bold, centered font
        list1.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Tahoma", Font.BOLD, 12)); // Bold, size 12
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

    private JScrollPane createLostItemsScrollPane() {
        Vector<String> lostItems = new Vector<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT Item_Name, Location, Date_Lost FROM lost_items WHERE Student_ID = ? ORDER BY Date_Lost DESC";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, currentIdNumber);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String itemName = rs.getString("Item_Name");
                    String location = rs.getString("Location");
                    Date dateLost = rs.getDate("Date_Lost");
                    String formattedDateLost = dateLost != null ? new SimpleDateFormat("MMM dd, yyyy").format(dateLost) : "Unknown date";
                    String itemDisplay = String.format("<html><b>%s</b><br>Location: %s<br>Lost: %s</html>",
                        itemName != null ? itemName : "(No name)",
                        location != null ? location : "Location not specified",
                        formattedDateLost);
                    lostItems.add(itemDisplay);
                    // Debug print
                    System.out.println("Loaded lost item: " + itemName + ", " + location + ", " + formattedDateLost);
                }
                if (lostItems.isEmpty()) {
                    System.out.println("No lost items found for student_id: " + currentIdNumber);
                }
            }
        } catch (SQLException ex) {
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
        list2.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
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

    private JScrollPane createFoundItemsScrollPane() {
        Vector<String> foundItems = new Vector<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT item_name, location_found, date_found FROM verified_found_items WHERE student_id = ? ORDER BY date_found DESC";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, currentIdNumber);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String itemName = rs.getString("item_name");
                    String location = rs.getString("location_found");
                    Date dateFound = rs.getDate("date_found");
                    String formattedDateFound = dateFound != null ? new SimpleDateFormat("MMM dd, yyyy").format(dateFound) : "Unknown date";
                    String itemDisplay = String.format("<html><b>%s</b><br>Location: %s<br>Found: %s</html>",
                        itemName != null ? itemName : "(No name)",
                        location != null ? location : "Location not specified",
                        formattedDateFound);
                    foundItems.add(itemDisplay);
                    // Debug print
                    System.out.println("Loaded found item: " + itemName + ", " + location + ", " + formattedDateFound);
                }
                if (foundItems.isEmpty()) {
                    System.out.println("No found items found for student_id: " + currentIdNumber);
                }
            }
        } catch (SQLException ex) {
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
        list3.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
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
