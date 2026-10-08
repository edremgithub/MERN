package kldLostAndFound;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AdminFoundItemVerification extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private UploadImagePanel imagePanel;
    private JTextField textField_1; // Contact Number
    private JTextField textField_2; // Course
    private JTextField textField_3; // Item Name
    private JTextField textField_4; // Location Found
    private JTextField textField_5; // Full Name
    private JTextField textField_6; // Date Found
    private RoundedTextArea descriptionTextArea;
    private String adminId;
    private AdminUI adminUI;
    private String idNumber;		
    private int itemId;

    public AdminFoundItemVerification(
            String adminId,
            AdminUI adminUI,
            String studentId,
            String itemName,
            String fullName,
            String course,	
            String locationFound,
            String contactNumber,
            String dateFound,
            String description,
            ImageIcon image,
            int itemId
    ) {
        this.adminId = adminId;
        this.adminUI = adminUI;
        this.idNumber = studentId;
        this.itemId = itemId;

        setTitle("Admin Found Item Verification - KLD Lost & Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 562);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AdminFoundItemVerification.jpg");
        background.setLayout(null);
        setContentPane(background);
        this.contentPane = background;

        // Back Button
        RoundedButton btnBack = new RoundedButton("Back", new Color(255, 240, 0), 0.8f, 11);
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnBack.addActionListener(e -> {
            AdminReportFound frame = new AdminReportFound(adminId, adminUI);
            frame.setVisible(true);
            dispose();
        });
        btnBack.setBounds(588, 16, 107, 36);
        btnBack.setForeground(Color.BLACK);
        background.add(btnBack);

        // Image Panel
        imagePanel = new UploadImagePanel(false);
        imagePanel.setBounds(66, 169, 141, 141);
        if (image != null) {
            imagePanel.setImage(image);
        }
        background.add(imagePanel);

        // Text Fields
        textField_3 = new JTextField(itemName); textField_3.setEditable(false); textField_3.setBounds(258, 179, 159, 20); background.add(textField_3);
        textField_5 = new JTextField(fullName); textField_5.setEditable(false); textField_5.setBounds(477, 179, 159, 20); background.add(textField_5);
        textField_2 = new JTextField(course); textField_2.setEditable(false); textField_2.setBounds(258, 210, 159, 20); background.add(textField_2);
        textField_4 = new JTextField(locationFound); textField_4.setEditable(false); textField_4.setBounds(477, 210, 159, 20); background.add(textField_4);
        textField_1 = new JTextField(contactNumber); textField_1.setEditable(false); textField_1.setBounds(258, 241, 159, 20); background.add(textField_1);
        textField_6 = new JTextField(dateFound); textField_6.setEditable(false); textField_6.setBounds(477, 241, 159, 20); background.add(textField_6);

        // Description
        descriptionTextArea = new RoundedTextArea(5, 30);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setBounds(258, 270, 380, 43);
        descriptionTextArea.setBackground(Color.WHITE);
        descriptionTextArea.setText(description);
        background.add(descriptionTextArea);

        // Archive Button
        RoundedButton btnArchive = new RoundedButton("Archive", new Color(220, 20, 60), 0.8f, 11);
        btnArchive.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ArchiveWindow archiveWindow = new ArchiveWindow(adminId, adminUI, itemId, AdminFoundItemVerification.this, "found");
        		archiveWindow.setVisible(true);
        		dispose(); // Close the current window
        	}
        });
        btnArchive.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnArchive.setBounds(111, 403, 96, 30);
        background.add(btnArchive);

        // Verify Button
        RoundedButton btnVerify = new RoundedButton("Verify", new Color(0, 128, 55), 0.8f, 11);
        btnVerify.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnVerify.addActionListener(e -> transferItemToVerified());
        btnVerify.setBounds(424, 404, 159, 30);
        background.add(btnVerify);
    }

    private boolean isAdminIdValid(String adminId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
            String query = "SELECT Admin_ID FROM admin WHERE Admin_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, adminId);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error checking Admin ID: " + e.getMessage());
            return false;
        }
    }

    private void transferItemToVerified() {
        if (!isAdminIdValid(adminId)) {
            JOptionPane.showMessageDialog(this, "Invalid Admin ID. Please check.");
            return;
        }

        String itemName = textField_3.getText();
        String fullName = textField_5.getText();
        String course = textField_2.getText();
        String locationFound = textField_4.getText();
        String contactNumber = textField_1.getText();
        String rawDate = textField_6.getText().trim();
        String description = descriptionTextArea.getText();
        String studentId = idNumber;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
            conn.setAutoCommit(false);

            String insertItemSQL = "INSERT INTO verified_found_items (item_name, full_name, course, location_found, contact_number, date_found, description, student_id, admin_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int newItemId = -1;

            try (PreparedStatement stmt = conn.prepareStatement(insertItemSQL, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, itemName);
                stmt.setString(2, fullName);
                stmt.setString(3, course);
                stmt.setString(4, locationFound);
                stmt.setString(5, contactNumber);

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    java.util.Date parsed = sdf.parse(rawDate);
                    stmt.setDate(6, new java.sql.Date(parsed.getTime()));
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                    return;
                }

                stmt.setString(7, description);
                stmt.setString(8, studentId != null && !studentId.isEmpty() ? studentId : null);
                stmt.setString(9, adminId);

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    newItemId = rs.getInt(1);
                }
            }

            if (newItemId != -1 && imagePanel.getImage() != null) {
                String insertImageSQL = "INSERT INTO verified_found_images (item_id, image_data) VALUES (?, ?)";
                try (PreparedStatement imageStmt = conn.prepareStatement(insertImageSQL)) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Image img = imagePanel.getImage();
                    BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    bufferedImage.getGraphics().drawImage(img, 0, 0, null);
                    ImageIO.write(bufferedImage, "jpg", baos);
                    byte[] imageBytes = baos.toByteArray();
                    baos.close();

                    imageStmt.setInt(1, newItemId);
                    imageStmt.setBytes(2, imageBytes);
                    imageStmt.executeUpdate();
                }
            }

            // Get original item_id for deletion
            int originalItemId = -1;
            PreparedStatement getItemStmt;
            if (studentId != null && !studentId.trim().isEmpty()) {
                getItemStmt = conn.prepareStatement("SELECT item_id FROM reported_found_items WHERE item_name = ? AND student_id = ? LIMIT 1");
                getItemStmt.setString(1, itemName);
                getItemStmt.setString(2, studentId);
            } else {
                getItemStmt = conn.prepareStatement("SELECT item_id FROM reported_found_items WHERE item_name = ? AND admin_id = ? LIMIT 1");
                getItemStmt.setString(1, itemName);
                getItemStmt.setString(2, adminId);
            }

            ResultSet idRs = getItemStmt.executeQuery();
            if (idRs.next()) {
                originalItemId = idRs.getInt("item_id");
            } else {
                JOptionPane.showMessageDialog(this, "Original item not found.");
                conn.rollback();
                return;
            }

            // Delete original image
            try (PreparedStatement deleteImage = conn.prepareStatement("DELETE FROM reported_found_images WHERE item_id = ?")) {
                deleteImage.setInt(1, originalItemId);
                deleteImage.executeUpdate();
            }

            // Delete original item
            try (PreparedStatement deleteItem = conn.prepareStatement("DELETE FROM reported_found_items WHERE item_id = ?")) {
                deleteItem.setInt(1, originalItemId);
                deleteItem.executeUpdate();
            }

            conn.commit();
            new AdminSuccessItem(adminId, adminUI).setVisible(true);
            dispose();

        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(this, "Error during transfer: " + e.getMessage());
        }
    }
}
