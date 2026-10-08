	package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.toedter.calendar.JDateChooser;

public class ReportFound extends JFrame {

    private static final long serialVersionUID = 1L;
    private UploadImagePanel imagePanel;
    private JTextField itemNameField;
    private JTextField fullNameField;
    private JTextField courseField;
    private JTextField locationField;
    private JTextField contactNumberField;
    private JDateChooser dateFoundChooser;
    private String idNumber;
    private MyAccount myAccount;
    private RoundedTextArea descriptionArea;

  

    public ReportFound(String idNumber, MyAccount myAccount) {
    			this.idNumber = idNumber;
    			this.myAccount = myAccount;
        setResizable(false);
        setTitle("Report Found Item - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 794, 597);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/FoundItem.jpg");
        setContentPane(background);
        background.setLayout(null);

        // Upload Image Panel
        imagePanel = new UploadImagePanel(true);
        imagePanel.setBounds(80, 218, 179, 240);
        background.add(imagePanel);

        JLabel lblUpload = new JLabel("Upload Image");
        lblUpload.setBounds(65, 345, 180, 20);
        lblUpload.setHorizontalAlignment(SwingConstants.CENTER);
        lblUpload.setForeground(Color.DARK_GRAY);
        lblUpload.setFont(new Font("Tahoma", Font.ITALIC, 11));
        background.add(lblUpload);

        // Item Name
        itemNameField = new JTextField();
        itemNameField.setBounds(330, 210, 159, 20);
        setPlaceholder(itemNameField, "Item Name");
        background.add(itemNameField);

        // Full Name
        fullNameField = new JTextField();
        fullNameField.setBounds(527, 210, 159, 20);
        setPlaceholder(fullNameField, "Full Name");
        background.add(fullNameField);

        // Course
        courseField = new JTextField();
        courseField.setBounds(330, 253, 159, 20);
        setPlaceholder(courseField, "Course");
        background.add(courseField);

        // Location Found
        locationField = new JTextField();
        locationField.setBounds(527, 253, 159, 20);
        setPlaceholder(locationField, "Location Found");
        background.add(locationField);

        // Contact Number
        contactNumberField = new JTextField();
        contactNumberField.setBounds(330, 296, 159, 20);
        setPlaceholder(contactNumberField, "Contact Number");
        background.add(contactNumberField);

        // Date Found
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

        // Description
        descriptionArea = new RoundedTextArea(5, 30);
        descriptionArea.setBounds(330, 364, 356, 78);
        descriptionArea.setBackground(Color.WHITE);
        background.add(descriptionArea);

        // Submit Button
        RoundedButton btnSubmit = new RoundedButton("Submit", new Color(255, 240, 0), 0.8f, 11);
        btnSubmit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
				String itemName = itemNameField.getText();
		        String fullName = fullNameField.getText();
		        String course = courseField.getText();
		        String location = locationField.getText();
		        String contactNumber = contactNumberField.getText();
		        java.util.Date utilDate = dateFoundChooser.getDate();
                String description = descriptionArea.getText();
                File imageFile = imagePanel.getSelectedFile();
                if (itemName.isEmpty() || itemName.equals("Item Name") ||
                    fullName.isEmpty() || fullName.equals("Full Name") ||
                    course.isEmpty() || course.equals("Course") ||
                    location.isEmpty() || location.equals("Location Found") ||
                    contactNumber.isEmpty() || contactNumber.equals("Contact Number") ||
                    utilDate == null ||
                    description.isEmpty() ||
                    imageFile == null) {
                    JOptionPane.showMessageDialog(null, "Please fill in all required information and upload an image before submitting.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                java.sql.Date dateFound = new java.sql.Date(utilDate.getTime());

		        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
                    // Proceed with item submission
                    try (
                        PreparedStatement insertItem = conn.prepareStatement(
                            "INSERT INTO reported_found_items (item_name, full_name, course, location_found, contact_number, date_found, description, Student_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                        PreparedStatement insertImage = conn.prepareStatement(
                            "INSERT INTO reported_found_images (item_id, image_data) VALUES (?, ?)")) {
                        // Insert into reported_found_items
                        insertItem.setString(1, itemName);
                        insertItem.setString(2, fullName);
                        insertItem.setString(3, course);
                        insertItem.setString(4, location);
                        insertItem.setString(5, contactNumber);
                        insertItem.setDate(6, dateFound);
                        insertItem.setString(7, description);
                        insertItem.setString(8, idNumber); // assuming idNumber maps to student_id
                        insertItem.executeUpdate();
                        ResultSet rsItem = insertItem.getGeneratedKeys();
                        if (rsItem.next()) {
                            int itemId = rsItem.getInt(1);
                            if (imageFile != null) {
                                FileInputStream fis = new FileInputStream(imageFile);
                                insertImage.setInt(1, itemId);
                                insertImage.setBinaryStream(2, fis, (int) imageFile.length());
                                insertImage.executeUpdate();
                            }
                        }
                        // Show remaining submissions after successful submission
                        ReportSuccess reportSuccess = new ReportSuccess(idNumber, myAccount);
                        reportSuccess.setVisible(true);
                        dispose();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error submitting report: " + ex.getMessage());
                }
        	}
        });
        btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnSubmit.setBounds(470, 451, 89, 23);
        background.add(btnSubmit);
        getRootPane().setDefaultButton(btnSubmit);
        
        RoundedButton btnMyAccount = new RoundedButton("My Account",new Color(0, 128, 55), 0.8f, 11);
        btnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnMyAccount.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		
     				myAccount.setVisible(true);
        			dispose();
        	}
        });
        btnMyAccount.setBounds(627, 18, 115, 40);
        background.add(btnMyAccount);
    }

    public ReportFound(String idNumber, MyAccount myAccount, String itemName, String fullName, String course, String location, String contactNumber, String description) {
        this(idNumber, myAccount); // call the default constructor
        itemNameField.setText(itemName);
        itemNameField.setForeground(Color.BLACK);
        fullNameField.setText(fullName);
        fullNameField.setForeground(Color.BLACK);
        courseField.setText(course);
        courseField.setForeground(Color.BLACK);
        locationField.setText(location);
        locationField.setForeground(Color.BLACK);
        contactNumberField.setText(contactNumber);
        contactNumberField.setForeground(Color.BLACK);
        descriptionArea.setText(description);
    }

    // Placeholder utility
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
