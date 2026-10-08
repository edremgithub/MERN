package kldLostAndFound;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

public class AdminReportFoundItems extends JFrame {

    private static final long serialVersionUID = 1L;
    private UploadImagePanel imagePanel;
    private JTextField itemNameField;
    private JTextField fullNameField;
    private JTextField courseField;
    private JTextField locationField;
    private JTextField contactNumberField;
    private JDateChooser dateFoundChooser;
    private String adminId;
    private AdminUI adminUI;
    private RoundedTextArea descriptionArea;

    public AdminReportFoundItems(String adminId, AdminUI adminUI) {
        this.adminId = adminId;
        this.adminUI = adminUI;

        setResizable(false);
        setTitle("Report Found Item - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 794, 597);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/FoundItem.jpg");
        setContentPane(background);
        background.setLayout(null);

        imagePanel = new UploadImagePanel(true);
        imagePanel.setBounds(80, 218, 179, 240);
        background.add(imagePanel);

        JLabel lblUpload = new JLabel("Upload Image");
        lblUpload.setBounds(65, 345, 180, 20);
        lblUpload.setHorizontalAlignment(SwingConstants.CENTER);
        lblUpload.setForeground(Color.DARK_GRAY);
        lblUpload.setFont(new Font("Tahoma", Font.ITALIC, 11));
        background.add(lblUpload);

        itemNameField = new JTextField();
        itemNameField.setBounds(330, 210, 159, 20);
        setPlaceholder(itemNameField, "Item Name");
        background.add(itemNameField);

        fullNameField = new JTextField();
        fullNameField.setBounds(527, 210, 159, 20);
        setPlaceholder(fullNameField, "Full Name");
        background.add(fullNameField);

        courseField = new JTextField();
        courseField.setBounds(330, 253, 159, 20);
        setPlaceholder(courseField, "Course/Faculty");
        background.add(courseField);

        locationField = new JTextField();
        locationField.setBounds(527, 253, 159, 20);
        setPlaceholder(locationField, "Location Found");
        background.add(locationField);

        contactNumberField = new JTextField();
        contactNumberField.setBounds(330, 296, 159, 20);
        setPlaceholder(contactNumberField, "Contact Number");
        background.add(contactNumberField);

        dateFoundChooser = new JDateChooser();
        dateFoundChooser.setBounds(527, 296, 159, 20);
        JTextField dateEditor = (JTextField) dateFoundChooser.getDateEditor().getUiComponent();
        dateEditor.setText("Date Found");
        dateEditor.setForeground(Color.GRAY);
        dateEditor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (dateEditor.getText().equals("Date Found")) {
                    dateEditor.setText("");
                    dateEditor.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (dateEditor.getText().isEmpty()) {
                    dateEditor.setText("Date Found");
                    dateEditor.setForeground(Color.GRAY);
                }
            }
        });
        background.add(dateFoundChooser);

        descriptionArea = new RoundedTextArea(5, 30);
        descriptionArea.setBounds(330, 364, 356, 78);
        descriptionArea.setBackground(Color.WHITE);
        background.add(descriptionArea);

        RoundedButton btnSubmit = new RoundedButton("Submit", new Color(255, 240, 0), 0.8f, 11);
        btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnSubmit.setBounds(470, 451, 89, 23);
        background.add(btnSubmit);
        getRootPane().setDefaultButton(btnSubmit);

        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText().trim();
                String fullName = fullNameField.getText().trim();
                String course = courseField.getText().trim();
                String location = locationField.getText().trim();
                String contact = contactNumberField.getText().trim();
                String description = descriptionArea.getText().trim();
                java.util.Date dateFound = dateFoundChooser.getDate();
                File imageFile = imagePanel.getSelectedFile();

                if (itemName.isEmpty() || itemName.equals("Item Name") ||
                    fullName.isEmpty() || fullName.equals("Full Name") ||
                    course.isEmpty() || course.equals("Course/Faculty") ||
                    location.isEmpty() || location.equals("Location Found") ||
                    contact.isEmpty() || contact.equals("Contact Number") ||
                    dateFound == null ||
                    description.isEmpty() ||
                    imageFile == null) {

                    JOptionPane.showMessageDialog(AdminReportFoundItems.this,
                            "Please fill out all fields and upload an image.",
                            "Missing Information", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean success = insertFoundItemAndImage(
                        itemName, fullName, course, location, contact, dateFound, description, imageFile, Integer.parseInt(adminId)
                );

                if (success) {
                    Admin_Report_Success reportSuccess = new Admin_Report_Success(adminId, adminUI);
                    reportSuccess.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AdminReportFoundItems.this,
                            "An error occurred while submitting the report and image.",
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        RoundedButton btnMyAccount = new RoundedButton("My Account", new Color(255, 224, 0), 0.8f, 11);
        btnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnMyAccount.setBounds(627, 18, 115, 40);
        background.add(btnMyAccount);

        btnMyAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminUI myAccount = new AdminUI(adminId);
                myAccount.setVisible(true);
                dispose();
            }
        });
    }

    private boolean insertFoundItemAndImage(String itemName, String fullName, String course, String location,
                                            String contact, java.util.Date dateFound, String description,
                                            File imageFile, int adminId) {
        Connection conn = null;
        PreparedStatement itemStmt = null;
        PreparedStatement imageStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
            conn.setAutoCommit(false);

            String itemSql = "INSERT INTO reported_found_items (item_name, full_name, course, location_found, contact_number, date_found, description, admin_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            itemStmt = conn.prepareStatement(itemSql, Statement.RETURN_GENERATED_KEYS);
            itemStmt.setString(1, itemName);
            itemStmt.setString(2, fullName);
            itemStmt.setString(3, course);
            itemStmt.setString(4, location);
            itemStmt.setString(5, contact);
            itemStmt.setDate(6, new java.sql.Date(dateFound.getTime()));
            itemStmt.setString(7, description);
            itemStmt.setInt(8, adminId);

            int rowsInserted = itemStmt.executeUpdate();
            if (rowsInserted == 0) {
                conn.rollback();
                return false;
            }

            generatedKeys = itemStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                conn.rollback();
                return false;
            }
            int itemId = generatedKeys.getInt(1);

            String imageSql = "INSERT INTO reported_found_images (item_id, image_data) VALUES (?, ?)";
            imageStmt = conn.prepareStatement(imageSql);
            imageStmt.setInt(1, itemId);
            imageStmt.setBinaryStream(2, new FileInputStream(imageFile), (int) imageFile.length());

            int imageInserted = imageStmt.executeUpdate();
            if (imageInserted == 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { if (generatedKeys != null) generatedKeys.close(); } catch (Exception e) {}
            try { if (itemStmt != null) itemStmt.close(); } catch (Exception e) {}
            try { if (imageStmt != null) imageStmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
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
}
