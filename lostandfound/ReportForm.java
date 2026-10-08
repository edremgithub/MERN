package kldLostAndFound;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

public class ReportForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private UploadImagePanel imagePanel;
    private JTextField textField_1; // Number
    private JTextField textField_2; // Course
    private JTextField textField_3; // Item Name
    private JTextField textField_4; // Location
    private JTextField textField_5; // Full Name
    private JDateChooser dateChooser;
    private RoundedTextArea textArea_Des;
    private String idNumber;
    private MyAccount myAccount;
 

    public ReportForm(String userId, MyAccount myAccount) {
    

        setResizable(false);
        setTitle("Report Lost Item - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 794, 597);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/ReportForm.jpg");
        setContentPane(background);
        background.setLayout(null);

        // ---------- Submit Button ----------
        RoundedButton btnSubmit = new RoundedButton("Submit", new Color(0, 128, 55), 0.8f, 11);
        btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnSubmit.setBounds(468, 449, 89, 23);
        background.add(btnSubmit);
        getRootPane().setDefaultButton(btnSubmit);

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // --- Validate Fields ---
                if (isEmpty(textField_3, "Item Name") ||
                    isEmpty(textField_5, "Full Name") ||
                    isEmpty(textField_2, "Course") ||
                    isEmpty(textField_4, "Location") ||
                    isEmpty(textField_1, "Number") ||
                    textArea_Des.getText().trim().isEmpty() ||
                    dateChooser.getDate() == null ||
                    imagePanel.getImageFile() == null) {

                    JOptionPane.showMessageDialog(null,
                        "Please fill in all fields and upload an image before submitting.",
                        "Incomplete Form",
                        JOptionPane.WARNING_MESSAGE);
                    return; // Stop submission
                }

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
                    // Step 1: Insert item data
                    String insertItemSQL = "INSERT INTO lost_items (Student_ID, Item_Name, Course, Contact_Number, Full_Name, Location, Date_Lost, Item_Description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(insertItemSQL, Statement.RETURN_GENERATED_KEYS);

                    ps.setString(1,idNumber);
                    ps.setString(2, textField_3.getText());
                    ps.setString(3, textField_2.getText());
                    ps.setString(4, textField_1.getText());
                    ps.setString(5, textField_5.getText());
                    ps.setString(6, textField_4.getText());

                    if (dateChooser.getDate() != null) {
                        ps.setDate(7, new java.sql.Date(dateChooser.getDate().getTime()));
                    } else {
                        ps.setNull(7, Types.DATE);
                    }

                    ps.setString(8, textArea_Des.getText());

                    ps.executeUpdate();

                    ResultSet rsItem = ps.getGeneratedKeys();
                    int lostItemId = -1;
                    if (rsItem.next()) {
                        lostItemId = rsItem.getInt(1);
                    }
                    ps.close();

                    // Step 2: Upload image if available
                    File imageFile = imagePanel.getImageFile();
                    if (lostItemId != -1 && imageFile != null) {
                        String insertImageSQL = "INSERT INTO lost_item_images (Lost_Item_ID, Image_Data) VALUES (?, ?)";
                        PreparedStatement imageStmt = conn.prepareStatement(insertImageSQL);
                        FileInputStream fis = new FileInputStream(imageFile);
                        imageStmt.setInt(1, lostItemId);
                        imageStmt.setBinaryStream(2, fis, (int) imageFile.length());
                        imageStmt.executeUpdate();
                        imageStmt.close();
                        fis.close();
                    }

                    // Step 3: Show success window and close
                    new ReportSuccess(idNumber, myAccount).setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error reporting item: " + ex.getMessage());
                }
            }
        });
        
        // ---------- Upload Image Panel ----------
        imagePanel = new UploadImagePanel(true);
        imagePanel.setBounds(88, 224, 180, 240);
        background.add(imagePanel);

        JLabel lblUpload = new JLabel("Upload Image");
        lblUpload.setBounds(65, 345, 180, 20);
        lblUpload.setHorizontalAlignment(SwingConstants.CENTER);
        lblUpload.setForeground(Color.DARK_GRAY);
        lblUpload.setFont(new Font("Tahoma", Font.ITALIC, 11));
        background.add(lblUpload);

        // ---------- Text Fields ----------
        textField_3 = new JTextField(); // Item Name
        textField_3.setBounds(330, 210, 159, 20);
        setPlaceholder(textField_3, "Item Name");
        background.add(textField_3);

        textField_5 = new JTextField(); // Full Name
        textField_5.setBounds(527, 210, 159, 20);
        setPlaceholder(textField_5, "Full Name");
        background.add(textField_5);

        textField_2 = new JTextField(); // Course
        textField_2.setBounds(330, 253, 159, 20);
        setPlaceholder(textField_2, "Course");
        background.add(textField_2);

        textField_4 = new JTextField(); // Location
        textField_4.setBounds(527, 253, 159, 20);
        setPlaceholder(textField_4, "Location");
        background.add(textField_4);

        textField_1 = new JTextField(); // Number
        textField_1.setBounds(330, 296, 159, 20);
        setPlaceholder(textField_1, "Number");
        background.add(textField_1);

        // ---------- My Account Button ----------
        RoundedButton btnMyAccount = new RoundedButton("My Account", new Color(0, 128, 55), 0.8f, 11);
        btnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnMyAccount.setBounds(626, 19, 119, 40);
        btnMyAccount.addActionListener(e -> {
            myAccount.setVisible(true);
            dispose();
        });
        background.add(btnMyAccount);

        // ---------- Date Lost (JDateChooser) ----------
        dateChooser = new JDateChooser();
        dateChooser.setBounds(527, 296, 159, 20);
        JTextField dateEditor = (JTextField) dateChooser.getDateEditor().getUiComponent();
        dateEditor.setText("Date Lost");
        dateEditor.setForeground(Color.GRAY);
        dateEditor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (dateEditor.getText().equals("Date Lost")) {
                    dateEditor.setText("");
                    dateEditor.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (dateEditor.getText().isEmpty()) {
                    dateEditor.setText("Date Lost");
                    dateEditor.setForeground(Color.GRAY);
                }
            }
        });
        background.add(dateChooser);

        // ---------- Description Area ----------
        textArea_Des = new RoundedTextArea(5, 30);
        textArea_Des.setBounds(330, 364, 356, 78);
        textArea_Des.setBackground(new Color(255, 255, 255));
        background.add(textArea_Des);
    }

    // Utility: Placeholder for JTextField
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
    private boolean isEmpty(JTextField field, String placeholder) {
        return field.getText().trim().isEmpty() || field.getText().equals(placeholder);
    }
    
    private String getTimeUntilNextSubmission() {
        Calendar now = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);
        tomorrow.set(Calendar.MILLISECOND, 0);

        long diffInMillis = tomorrow.getTimeInMillis() - now.getTimeInMillis();
        long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;

        return String.format("%d hours and %d minutes", hours, minutes);
    }
}
