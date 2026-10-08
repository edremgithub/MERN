package kldLostAndFound;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchItem extends JFrame {

    private static final long serialVersionUID = 1L;
    private RoundedTextField textField;
    private String idNumber;
    private MyAccount myAccount;
    private RoundedButton btnView;
    private JScrollPane scrollPane, scrollPane2, scrollPane3;
    private Vector<String> allItems = new Vector<>();
    private Vector<String> verifiedItems = new Vector<>();

    public SearchItem(String idNumber, MyAccount myAccount) {
        this.idNumber = idNumber;
        this.myAccount = myAccount;

        setTitle("Search Item - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(730, 562);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/SearchItembg.jpg");
        setContentPane(background);
        background.setLayout(null);

        // My Account Button
        RoundedButton btnMyAccount = new RoundedButton("My Account", new Color(0, 128, 55), 0.8f, 11);
        btnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnMyAccount.setBounds(569, 23, 116, 40);
        btnMyAccount.addActionListener(e -> {
            MyAccount newAccount = new MyAccount(idNumber);
            newAccount.setVisible(true);
            dispose();
        });
        background.add(btnMyAccount);

        // Search Text Field
        textField = new RoundedTextField(10);
        textField.setBounds(50, 140, 614, 40);
        setPlaceholder(textField, " 🔎 Search Item");
        background.add(textField);

   /*     // View All Button
        btnView = new RoundedButton("View All", new Color(255, 240, 0), 0.8f, 11);
        btnView.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnView.setBounds(410, 360, 128, 27);
        btnView.addActionListener(e -> {
            RecentlyClaimed recentlyClaimed = new RecentlyClaimed(idNumber, myAccount);
            recentlyClaimed.setVisible(true);
            dispose();
        });
        background.add(btnView);  */

        // Rounded Panels and Scroll Panes
        RoundedPanel roundedPanel1 = new RoundedPanel(20);
        roundedPanel1.setBounds(50, 235, 162, 236);
        roundedPanel1.setLayout(new BorderLayout());
        background.add(roundedPanel1);

        scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        roundedPanel1.add(scrollPane, BorderLayout.CENTER);

        RoundedPanel roundedPanel2 = new RoundedPanel(20);
        roundedPanel2.setBounds(235, 234, 176, 236);
        roundedPanel2.setLayout(new BorderLayout());
        background.add(roundedPanel2);

        scrollPane2 = new JScrollPane();
        scrollPane2.setOpaque(false);
        scrollPane2.getViewport().setOpaque(false);
        roundedPanel2.add(scrollPane2, BorderLayout.CENTER);

        RoundedPanel roundedPanel3 = new RoundedPanel(20);
        roundedPanel3.setBounds(453, 234, 215, 236);
        roundedPanel3.setLayout(new BorderLayout());
        background.add(roundedPanel3);

        scrollPane3 = new JScrollPane();
        scrollPane3.setOpaque(false);
        scrollPane3.getViewport().setOpaque(false);
        roundedPanel3.add(scrollPane3, BorderLayout.CENTER);

        // Real-time Search Listener
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterList();
            }

            public void removeUpdate(DocumentEvent e) {
                filterList();
            }

            public void changedUpdate(DocumentEvent e) {
                filterList();
            }
        });

        // Load items from DB
        loadLostItemsFromDatabase();
        loadVerifiedFoundItemsFromDatabase();
        loadClaimedItemsFromDatabase();
    }

    private void loadLostItemsFromDatabase() {
        allItems.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Lost_Item_ID, Item_Name, Location FROM lost_items")) {

            while (rs.next()) {
                int id = rs.getInt("Lost_Item_ID");
                String itemName = rs.getString("Item_Name");
                String location = rs.getString("Location");
                allItems.add(id + " - " + itemName + " (Lost at: " + location + ")");
            }

            setLostListWithListener(allItems);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading items from database.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadVerifiedFoundItemsFromDatabase() {
        verifiedItems.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT item_id, item_name, full_name, location_found, date_found FROM verified_found_items")) {

            while (rs.next()) {
                int id = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                String fullName = rs.getString("full_name");
                String location = rs.getString("location_found");
                String dateFound = rs.getString("date_found");
                verifiedItems.add(id + " - " + itemName + " (by: " + fullName + ", at: " + location + ")");
            }

            setVerifiedListWithListener(verifiedItems);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading verified found items.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadClaimedItemsFromDatabase() {
        Vector<String> claimedItems = new Vector<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT claim_id, item_name, claim_date, location_found FROM claimed_items")) {
            while (rs.next()) {
                int claimId = rs.getInt("claim_id");
                String itemName = rs.getString("item_name");
                String claimDate = rs.getString("claim_date");
                String location = rs.getString("location_found");
                claimedItems.add(claimId + " - " + itemName + " (Claimed: " + claimDate + ", at: " + location + ")");
            }
            setClaimedListWithListener(claimedItems);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading claimed items.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterList() {
        String searchText = textField.getText().toLowerCase();
        if (searchText.equals(" 🔎 search item") || searchText.isEmpty()) {
            // Show all items
            setLostListWithListener(allItems);
            setVerifiedListWithListener(verifiedItems);
            // For claimed items, reload all from DB
            Vector<String> claimedItems = new Vector<>();
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT claim_id, item_name, claim_date, location_found FROM claimed_items")) {
                while (rs.next()) {
                    int claimId = rs.getInt("claim_id");
                    String itemName = rs.getString("item_name");
                    String claimDate = rs.getString("claim_date");
                    String location = rs.getString("location_found");
                    claimedItems.add(claimId + " - " + itemName + " (Claimed: " + claimDate + ", at: " + location + ")");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            setClaimedListWithListener(claimedItems);
            return;
        }
        Vector<String> filteredLost = new Vector<>();
        Vector<String> filteredVerified = new Vector<>();
        Vector<String> filteredClaimed = new Vector<>();

        for (String item : allItems) {
            if (item.toLowerCase().contains(searchText)) {
                filteredLost.add(item);
            }
        }

        for (String item : verifiedItems) {
            if (item.toLowerCase().contains(searchText)) {
                filteredVerified.add(item);
            }
        }

        // Filter claimed items
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT claim_id, item_name, claim_date, location_found FROM claimed_items")) {
            while (rs.next()) {
                int claimId = rs.getInt("claim_id");
                String itemName = rs.getString("item_name");
                String claimDate = rs.getString("claim_date");
                String location = rs.getString("location_found");
                String display = claimId + " - " + itemName + " (Claimed: " + claimDate + ", at: " + location + ")";
                if (display.toLowerCase().contains(searchText)) {
                    filteredClaimed.add(display);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setLostListWithListener(filteredLost);
        setVerifiedListWithListener(filteredVerified);
        setClaimedListWithListener(filteredClaimed);
    }

    private JList<String> createStyledList(Vector<String> data) {
        JList<String> list = new JList<>(data);
        list.setOpaque(false);
        list.setBackground(new Color(0, 0, 0, 0));
        list.setForeground(Color.BLACK);
        list.setFont(new Font("Tahoma", Font.BOLD, 12));
        list.setSelectionBackground(new Color(255, 255, 0, 100));
        list.setSelectionForeground(Color.BLACK);
        list.setFixedCellHeight(30);
        return list;
    }

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Do NOT reset the placeholder here
                // if (field.getText().isEmpty()) {
                //     field.setText(placeholder);
                //     field.setForeground(Color.GRAY);
                // }
            }
        });

        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                if (!field.getText().equals(placeholder) && !field.getText().isEmpty()) filterList();
            }
            public void removeUpdate(DocumentEvent e) {
                if (!field.getText().equals(placeholder) && !field.getText().isEmpty()) filterList();
            }
            public void changedUpdate(DocumentEvent e) {
                if (!field.getText().equals(placeholder) && !field.getText().isEmpty()) filterList();
            }
        });
    }

    // Helper for lost items list
    private void setLostListWithListener(Vector<String> data) {
        JList<String> list = createStyledList(data);
        scrollPane.setViewportView(list);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && list.getSelectedValue() != null) {
                    String selectedValue = list.getSelectedValue();
                    int itemId = Integer.parseInt(selectedValue.split(" - ")[0]);
                    new LostItemStatus(itemId, idNumber, myAccount).setVisible(true);
                    dispose();
                }
            }
        });
    }
    // Helper for verified found items list
    private void setVerifiedListWithListener(Vector<String> data) {
        JList<String> list = createStyledList(data);
        scrollPane2.setViewportView(list);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && list.getSelectedValue() != null) {
                    String selectedValue = list.getSelectedValue();
                    int itemId = Integer.parseInt(selectedValue.split(" - ")[0]);
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
                         PreparedStatement pstmt = conn.prepareStatement("SELECT item_name, location_found, date_found, description, full_name FROM verified_found_items WHERE item_id = ?");
                         PreparedStatement imgStmt = conn.prepareStatement("SELECT image_data FROM verified_found_images WHERE item_id = ?")) {
                        pstmt.setInt(1, itemId);
                        ResultSet rs = pstmt.executeQuery();
                        String itemName = null, location = null, dateFound = null, description = null, reporterName = null;
                        if (rs.next()) {
                            itemName = rs.getString("item_name");
                            location = rs.getString("location_found");
                            dateFound = rs.getString("date_found");
                            description = rs.getString("description");
                            reporterName = rs.getString("full_name");
                        }
                        ImageIcon imageIcon = null;
                        imgStmt.setInt(1, itemId);
                        ResultSet imgRs = imgStmt.executeQuery();
                        if (imgRs.next()) {
                            byte[] imgData = imgRs.getBytes("image_data");
                            if (imgData != null) {
                                imageIcon = new ImageIcon(imgData);
                            }
                        }
                        ClaimItem claimItem = new ClaimItem(String.valueOf(itemId), itemName, location, dateFound, description, imageIcon, idNumber, -1, reporterName);
                        claimItem.setVisible(true);
                        dispose();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error loading item details: " + e.getMessage());
                    }
                }
            }
        });
    }
    // Helper for claimed items list (no double-click action for now)
    private void setClaimedListWithListener(Vector<String> data) {
        JList<String> list = createStyledList(data);
        scrollPane3.setViewportView(list);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && list.getSelectedValue() != null) {
                    String selectedValue = list.getSelectedValue();
                    int claimId = Integer.parseInt(selectedValue.split(" - ")[0]);
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
                         PreparedStatement pstmt = conn.prepareStatement("SELECT item_name, location_found, claim_date, item_description, claim_description FROM claimed_items WHERE claim_id = ?")) {
                        pstmt.setInt(1, claimId);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            String itemName = rs.getString("item_name");
                            String location = rs.getString("location_found");
                            String claimDate = rs.getString("claim_date");
                            String itemDescription = rs.getString("item_description");
                            String claimDescription = rs.getString("claim_description");
                            AppealClaimForm appealForm = new AppealClaimForm();
                            appealForm.setItemDetails(String.valueOf(claimId), itemName, location, claimDate, itemDescription, idNumber, myAccount);
                            appealForm.setVisible(true);
                            dispose();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error loading item details: " + e.getMessage());
                    }
                }
            }
        });
    }
}
