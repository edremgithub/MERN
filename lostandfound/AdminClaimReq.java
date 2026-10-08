package kldLostAndFound;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AdminClaimReq extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_SearchBarClaimReq;
    private RoundedShadowLabel hoverLabel;
    private String adminId;
    private AdminUI adminUI;
    private BackgroundPanel background;
    private int currentPage = 1;  // Add this field for pagination

    // Helper class to store claim request data
    class ClaimRequestData {
        public int claimId;
        public int itemId;
        public String studentId, fullName, courseYearSection, claimDescription;
        public String itemName, location, dateFound, itemDescription;
        public ImageIcon claimImage, verifiedItemImage;
    }

    private List<ClaimRequestData> claimRequests = new ArrayList<>();
    private UploadImagePanel[] imagePanels = new UploadImagePanel[6];
    private RoundedTextArea[] itemNameAreas = new RoundedTextArea[6];
    private RoundedTextArea[] dateAreas = new RoundedTextArea[6];
    private RoundedTextArea[] studentNumberAreas = new RoundedTextArea[6];
    private RoundedTextArea[] nameAreas = new RoundedTextArea[6];

    public AdminClaimReq(String adminId, AdminUI adminUI) {
        this.adminId = adminId;
        this.adminUI = adminUI;
        setResizable(false);
        setTitle("Admin Claim Request - KLD Lost & Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 562);
        setResizable(false);
        setLocationRelativeTo(null);

        // Set app icon
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        // Set background
        background = new BackgroundPanel("src/kldLostAndFound/images/AdminClaimReq.jpg");
        background.setLayout(null);

        // Initialize contentPane and set background as content pane
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(background);

        // Initialize image panels and text areas
        for (int i = 0; i < 6; i++) {
            int yPos = 211 + (i * 48);

            imagePanels[i] = new UploadImagePanel(false);
            imagePanels[i].setBounds(40, yPos, 53, 47);
            imagePanels[i].setEnabled(false);
            background.add(imagePanels[i]);

            itemNameAreas[i] = new RoundedTextArea(1, 1);
            itemNameAreas[i].setBounds(98, yPos, 93, 47);
            itemNameAreas[i].setEditable(false);
            background.add(itemNameAreas[i]);

            dateAreas[i] = new RoundedTextArea(1, 1);
            dateAreas[i].setBounds(192, yPos, 80, 47);
            dateAreas[i].setEditable(false);
            background.add(dateAreas[i]);

            studentNumberAreas[i] = new RoundedTextArea(1, 1);
            studentNumberAreas[i].setBounds(273, yPos, 168, 47);
            studentNumberAreas[i].setEditable(false);
            background.add(studentNumberAreas[i]);

            nameAreas[i] = new RoundedTextArea(1, 1);
            nameAreas[i].setBounds(442, yPos, 129, 47);
            nameAreas[i].setEditable(false);
            background.add(nameAreas[i]);

            RoundedButton btnVerify = new RoundedButton("Approve", new Color(0, 128, 55), 0.8f, 11);
            btnVerify.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnVerify.setBounds(594, yPos + 11, 83, 28);
            background.add(btnVerify);

            final int buttonIndex = i;
            btnVerify.addActionListener(e -> handleAppealButtonClick(buttonIndex));
        }

        // Admin Options Button
        RoundedButton btnAdmin = new RoundedButton("Admin Options", new Color(255, 240, 0), 0.8f, 11);
        btnAdmin.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnAdmin.addActionListener(e -> {
            AdminNavigations frame = new AdminNavigations(adminId, adminUI);
            frame.setVisible(true);
            dispose();
        });
        btnAdmin.setBounds(566, 12, 129, 20);
        btnAdmin.setForeground(Color.BLACK);
        background.add(btnAdmin);

        // Claim Appeals Button
        RoundedButton btnClaimAppeals = new RoundedButton("Claim Appeals", new Color(220, 20, 60), 0.8f, 11);
        btnClaimAppeals.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnClaimAppeals.addActionListener(e -> {
            AdminApproveFalseClaimItemAppeals frame = new AdminApproveFalseClaimItemAppeals(adminId, adminUI);
            frame.setVisible(true);
            dispose();
        });
        btnClaimAppeals.setBounds(566, 35, 129, 20);
        btnClaimAppeals.setForeground(Color.BLACK);
        background.add(btnClaimAppeals);

        // Search Bar
        textField_SearchBarClaimReq = new RoundedTextField(5);
        textField_SearchBarClaimReq.setBounds(75, 149, 458, 20);
        background.add(textField_SearchBarClaimReq);
        textField_SearchBarClaimReq.setColumns(10);
        setPlaceholder(textField_SearchBarClaimReq, " 🔎 Search Claim Request");

        // Add pagination buttons
        JButton btnLeft = createHoverButton(loadIcon("turnLeft.jpg", 22, 20), "Previous");
        btnLeft.setBounds(46, 149, 22, 20);
        background.add(btnLeft);

        JButton btnRight = createHoverButton(loadIcon("turnRight.jpg", 22, 20), "Next");
        btnRight.setBounds(541, 149, 22, 20);
        background.add(btnRight);

        // Add pagination button listeners
        btnRight.addActionListener(e -> {
            currentPage++;
            loadClaimRequests();
        });

        btnLeft.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadClaimRequests();
            }
        });

        // Add search functionality
        textField_SearchBarClaimReq.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = textField_SearchBarClaimReq.getText().trim();
                if (query.isEmpty()) {
                    currentPage = 1;
                    loadClaimRequests();
                } else {
                    searchClaimRequests(query);
                }
            }
        });

        // Initialize hover shadow label
        hoverLabel = new RoundedShadowLabel();
        hoverLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        hoverLabel.setForeground(Color.WHITE);
        hoverLabel.setVisible(false);
        background.add(hoverLabel);
        background.setComponentZOrder(hoverLabel, 0);

        // Load claim requests from database
        loadClaimRequests();
    }

    private void handleAppealButtonClick(int buttonIndex) {
        if (buttonIndex < claimRequests.size()) {
            ClaimRequestData data = claimRequests.get(buttonIndex);
            AdminApproveClaimReq frame = new AdminApproveClaimReq(
                adminId, adminUI,
                data.claimId,
                data.itemId,
                data.studentId, data.fullName, data.courseYearSection, data.claimDescription,
                data.itemName, data.location, data.dateFound, data.itemDescription,
                data.claimImage,		
                AdminApproveClaimReq.imageIconToBytes(data.claimImage),
                data.verifiedItemImage
            );
            frame.setVisible(true);
            dispose();	 
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

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

    // Debug method to print all claim requests and their item info
    private void debugPrintAllClaimRequests() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
            String sql = "SELECT cir.claim_id, cir.item_id, vfi.item_name, vfi.date_found, cir.student_id, cir.full_name " +
                         "FROM claim_item_req cir " +
                         "LEFT JOIN verified_found_items vfi ON cir.item_id = vfi.item_id";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                System.out.println("--- DEBUG: All claim requests and their item info ---");
                while (rs.next()) {
                    System.out.println(
                        "claim_id=" + rs.getInt("claim_id") +
                        ", item_id=" + rs.getInt("item_id") +
                        ", item_name='" + rs.getString("item_name") + "'" +
                        ", date_found='" + rs.getString("date_found") + "'" +
                        ", student_id='" + rs.getString("student_id") + "'" +
                        ", full_name='" + rs.getString("full_name") + "'"
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cleans up orphan claim requests and images, including those with invalid item_name or date_found
    private void cleanupOrphanClaimRequestsAndImages() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
            // 1. Delete orphan claim_item_images
            try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM claim_item_images WHERE claim_id NOT IN (SELECT claim_id FROM claim_item_req)")) {
                ps.executeUpdate();
            }
            // 2. Delete claim_item_images for claim requests with invalid item_name or date_found using JOIN
            try (PreparedStatement ps = conn.prepareStatement(
                "DELETE cii FROM claim_item_images cii " +
                "INNER JOIN claim_item_req cir ON cii.claim_id = cir.claim_id " +
                "INNER JOIN verified_found_items vfi ON cir.item_id = vfi.item_id " +
                "WHERE vfi.item_name IS NULL OR vfi.item_name = '' OR vfi.item_name = 'N/A' " +
                "OR vfi.date_found IS NULL OR vfi.date_found = '' OR vfi.date_found = 'N/A'"
            )) {
                ps.executeUpdate();
            }
            // 3. Delete orphan claim_item_req
            try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM claim_item_req WHERE item_id NOT IN (SELECT item_id FROM verified_found_items)")) {
                ps.executeUpdate();
            }
            // 4. Delete claim_item_req for items with invalid item_name or date_found using JOIN
            try (PreparedStatement ps = conn.prepareStatement(
                "DELETE cir FROM claim_item_req cir " +
                "INNER JOIN verified_found_items vfi ON cir.item_id = vfi.item_id " +
                "WHERE vfi.item_name IS NULL OR vfi.item_name = '' OR vfi.item_name = 'N/A' " +
                "OR vfi.date_found IS NULL OR vfi.date_found = '' OR vfi.date_found = 'N/A'"
            )) {
                ps.executeUpdate();
            }
            // 5. Delete orphan claim_item_images again (for any images left after above deletions)
            try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM claim_item_images WHERE claim_id NOT IN (SELECT claim_id FROM claim_item_req)")) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadClaimRequests() {
        cleanupOrphanClaimRequestsAndImages();
        debugPrintAllClaimRequests();
        claimRequests.clear();
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/lostandfound", "root", "")) {

            String sql = """
                SELECT cir.claim_id, cir.item_id, cir.full_name, cir.student_id, cir.course_year_section, 
                       cir.claim_description, vfi.item_name, vfi.location_found, vfi.date_found, 
                       vfi.description as item_description, cii.image_data, vfiimg.image_data as verified_image
                FROM claim_item_req cir
                INNER JOIN verified_found_items vfi ON cir.item_id = vfi.item_id
                LEFT JOIN claim_item_images cii ON cir.claim_id = cii.claim_id
                LEFT JOIN verified_found_images vfiimg ON vfi.item_id = vfiimg.item_id
                WHERE cir.archived = FALSE AND vfi.archived = FALSE
                  AND vfi.item_name IS NOT NULL 
                  AND vfi.item_name != 'N/A'
                  AND vfi.item_name != ''
                  AND vfi.date_found IS NOT NULL
                  AND vfi.date_found != 'N/A'
                  AND vfi.date_found != ''
                ORDER BY cir.claim_id DESC
                LIMIT ?, 6
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, (currentPage - 1) * 6);
                ResultSet rs = stmt.executeQuery();

                int row = 0;
                while (rs.next() && row < 6) {
                    ClaimRequestData data = new ClaimRequestData();
                    data.claimId = rs.getInt("claim_id");
                    data.itemId = rs.getInt("item_id");
                    data.fullName = rs.getString("full_name");
                    data.studentId = rs.getString("student_id");
                    data.courseYearSection = rs.getString("course_year_section");
                    data.claimDescription = rs.getString("claim_description");
                    data.itemName = rs.getString("item_name");
                    data.location = rs.getString("location_found");
                    data.dateFound = rs.getString("date_found");
                    data.itemDescription = rs.getString("item_description");
                    byte[] claimImgData = rs.getBytes("image_data");
                    byte[] verifiedImgData = rs.getBytes("verified_image");
                    if (claimImgData != null) data.claimImage = new ImageIcon(claimImgData);
                    if (verifiedImgData != null) data.verifiedItemImage = new ImageIcon(verifiedImgData);
                    claimRequests.add(data);

                    // Debug printout
                    System.out.println("Loaded claim: itemId=" + data.itemId + ", itemName='" + data.itemName + "', dateFound='" + data.dateFound + "', studentId='" + data.studentId + "', name='" + data.fullName + "'");

                    // Set image panel to verified item image
                    if (imagePanels[row] != null) {
                        if (data.verifiedItemImage != null) {
                            imagePanels[row].setImage(data.verifiedItemImage);
                        } else {
                            imagePanels[row].setImage(null);
                        }
                    }

                    // Set table columns
                    itemNameAreas[row].setText(data.itemName != null ? data.itemName : "N/A");
                    dateAreas[row].setText(data.dateFound != null ? data.dateFound : "N/A");
                    studentNumberAreas[row].setText(data.studentId != null ? data.studentId : "N/A");
                    nameAreas[row].setText(data.fullName != null ? data.fullName : "N/A");

                    // Debug printout for UI assignment
                    System.out.println("Set row " + row + ": " +
                        "itemName=" + data.itemName +
                        ", dateFound=" + data.dateFound +
                        ", studentId=" + data.studentId +
                        ", fullName=" + data.fullName);

                    row++;
                }

                // Clear remaining rows if less than 6
                for (int i = row; i < 6; i++) {
                    itemNameAreas[i].setText("");
                    dateAreas[i].setText("");
                    studentNumberAreas[i].setText("");
                    nameAreas[i].setText("");
                    if (imagePanels[i] != null) imagePanels[i].setImage(null);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading claim requests: " + e.getMessage());
        }
    }

    private void searchClaimRequests(String query) {
        cleanupOrphanClaimRequestsAndImages();
        claimRequests.clear();
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/lostandfound", "root", "")) {

            String sql = """
                SELECT cir.claim_id, cir.item_id, cir.full_name, cir.student_id, cir.course_year_section, 
                       cir.claim_description, vfi.item_name, vfi.location_found, vfi.date_found, 
                       vfi.description as item_description, cii.image_data, vfiimg.image_data as verified_image
                FROM claim_item_req cir
                INNER JOIN verified_found_items vfi ON cir.item_id = vfi.item_id
                LEFT JOIN claim_item_images cii ON cir.claim_id = cii.claim_id
                LEFT JOIN verified_found_images vfiimg ON vfi.item_id = vfiimg.item_id
                WHERE cir.archived = FALSE AND vfi.archived = FALSE
                  AND vfi.item_name IS NOT NULL 
                  AND vfi.item_name != 'N/A'
                  AND vfi.item_name != ''
                  AND vfi.date_found IS NOT NULL
                  AND vfi.date_found != 'N/A'
                  AND vfi.date_found != ''
                  AND (vfi.item_name LIKE ? OR cir.full_name LIKE ? OR cir.student_id LIKE ?)
                ORDER BY cir.claim_id DESC
                LIMIT 6
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                String searchPattern = "%" + query + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setString(3, searchPattern);
                ResultSet rs = stmt.executeQuery();

                int row = 0;
                while (rs.next() && row < 6) {
                    ClaimRequestData data = new ClaimRequestData();
                    data.claimId = rs.getInt("claim_id");
                    data.itemId = rs.getInt("item_id");
                    data.fullName = rs.getString("full_name");
                    data.studentId = rs.getString("student_id");
                    data.courseYearSection = rs.getString("course_year_section");
                    data.claimDescription = rs.getString("claim_description");
                    data.itemName = rs.getString("item_name");
                    data.location = rs.getString("location_found");
                    data.dateFound = rs.getString("date_found");
                    data.itemDescription = rs.getString("item_description");
                    byte[] claimImgData = rs.getBytes("image_data");
                    byte[] verifiedImgData = rs.getBytes("verified_image");
                    if (claimImgData != null) data.claimImage = new ImageIcon(claimImgData);
                    if (verifiedImgData != null) data.verifiedItemImage = new ImageIcon(verifiedImgData);
                    claimRequests.add(data);

                    // Debug printout
                    System.out.println("Loaded claim: itemId=" + data.itemId + ", itemName='" + data.itemName + "', dateFound='" + data.dateFound + "', studentId='" + data.studentId + "', name='" + data.fullName + "'");

                    // Set image panel to verified item image
                    if (imagePanels[row] != null) {
                        if (data.verifiedItemImage != null) {
                            imagePanels[row].setImage(data.verifiedItemImage);
                        } else {
                            imagePanels[row].setImage(null);
                        }
                    }

                    // Set table columns
                    itemNameAreas[row].setText(data.itemName != null ? data.itemName : "N/A");
                    dateAreas[row].setText(data.dateFound != null ? data.dateFound : "N/A");
                    studentNumberAreas[row].setText(data.studentId != null ? data.studentId : "N/A");
                    nameAreas[row].setText(data.fullName != null ? data.fullName : "N/A");

                    // Debug printout for UI assignment
                    System.out.println("Set row " + row + ": " +
                        "itemName=" + data.itemName +
                        ", dateFound=" + data.dateFound +
                        ", studentId=" + data.studentId +
                        ", fullName=" + data.fullName);

                    row++;
                }

                // Clear remaining rows if less than 6
                for (int i = row; i < 6; i++) {
                    itemNameAreas[i].setText("");
                    dateAreas[i].setText("");
                    studentNumberAreas[i].setText("");
                    nameAreas[i].setText("");
                    if (imagePanels[i] != null) imagePanels[i].setImage(null);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching claim requests: " + e.getMessage());
        }
    }

    private ImageIcon loadIcon(String fileName, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/kldLostAndFound/images/" + fileName));
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

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
}
