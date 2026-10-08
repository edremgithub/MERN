package kldLostAndFound;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;

public class CreateAccount extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtEmail, txtIdNumber, txtContact, txtFullname, txtAddress, txtCourse, txtYearSection;
    private JDateChooser dateChooserDob;
    private JPasswordField txtPassword, txtConfirmPassword;
    private CircleImagePanel imagePanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CreateAccount frame = new CreateAccount();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CreateAccount() {
        setResizable(false);
        setTitle("Create Account - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 781, 476);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());
        centerWindow();

        BackgroundPanel contentPane = new BackgroundPanel("src/kldLostAndFound/images/createacc1.jpg");
        setContentPane(contentPane);

        RoundedButton btnBack = new RoundedButton("Back", new Color(0, 128, 55), 0.8f, 11);
        btnBack.addActionListener(e -> {
            LogInUI loginUI = new LogInUI();
            loginUI.frmKldLostAnd.setVisible(true);
            CreateAccount.this.dispose();
        });
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnBack.setBounds(643, 21, 91, 30);
        contentPane.add(btnBack);

        txtEmail = new JTextField();
        txtEmail.setBounds(47, 216, 180, 20);
        txtIdNumber = new JTextField();
        txtIdNumber.setBounds(248, 180, 180, 20);
        txtContact = new JTextField();
        txtContact.setBounds(47, 255, 180, 20);
        dateChooserDob = new JDateChooser();
        dateChooserDob.setBounds(248, 216, 180, 20);
        dateChooserDob.setDateFormatString("yyyy-MM-dd");

        txtFullname = new JTextField();
        txtFullname.setBounds(47, 180, 180, 20);
        txtAddress = new JTextField();
        txtAddress.setBounds(248, 255, 180, 20);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(47, 294, 180, 20);
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(248, 294, 180, 20);

        imagePanel = new CircleImagePanel(true);
        imagePanel.setBounds(474, 95, 253, 255);
        imagePanel.setPreferredSize(new Dimension(150, 150));

        JLabel lblUpload = new JLabel("Upload Image");
        lblUpload.setBounds(740, 396, 150, 14);
        lblUpload.setFont(new Font("Tahoma", Font.ITALIC, 11));
        lblUpload.setHorizontalAlignment(SwingConstants.CENTER);
        lblUpload.setForeground(Color.DARK_GRAY);

        txtCourse = new JTextField();
        txtCourse.setBounds(47, 337, 179, 20);
        txtYearSection = new JTextField();
        txtYearSection.setBounds(249, 337, 179, 20);

        RoundedButton btnCreate = new RoundedButton("Create Account", new Color(0, 128, 55), 0.8f, 11);
        btnCreate.setBounds(170, 374, 138, 23);
        btnCreate.addActionListener(e -> handleCreateAccount());

        contentPane.setLayout(null);
        contentPane.add(txtEmail);
        contentPane.add(txtContact);
        contentPane.add(txtFullname);
        contentPane.add(txtPassword);
        contentPane.add(txtIdNumber);
        contentPane.add(txtAddress);
        contentPane.add(txtConfirmPassword);
        contentPane.add(imagePanel);
        contentPane.add(lblUpload);
        contentPane.add(dateChooserDob);
        contentPane.add(txtCourse);
        contentPane.add(txtYearSection);
        contentPane.add(btnCreate);

        // Add labels
        contentPane.add(makeLabel("Full Name", 47, 167));
        contentPane.add(makeLabel("Student ID", 248, 167));
        contentPane.add(makeLabel("Email", 47, 204));
        contentPane.add(makeLabel("Date of Birth", 248, 204));
        contentPane.add(makeLabel("Contact Number", 47, 241));
        contentPane.add(makeLabel("Address", 248, 241));
        contentPane.add(makeLabel("Password", 47, 279));
        contentPane.add(makeLabel("Confirm Password", 248, 279));
        contentPane.add(makeLabel("Course", 47, 320));
        contentPane.add(makeLabel("Year & Section", 248, 320));
    }

    private JLabel makeLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 120, 14);
        return label;
    }

    private void handleCreateAccount() {
        String studentId = txtIdNumber.getText().trim();
        String fullName = txtFullname.getText().trim();
        String email = txtEmail.getText().trim();
        String contact = txtContact.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String course = txtCourse.getText().trim();
        String yearSection = txtYearSection.getText().trim();
        String address = txtAddress.getText().trim();
        java.util.Date dobUtil = dateChooserDob.getDate();

        if (studentId.isEmpty() || fullName.isEmpty() || email.isEmpty() || contact.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty() || course.isEmpty()
                || yearSection.isEmpty() || address.isEmpty() || dobUtil == null) {
            JOptionPane.showMessageDialog(this, "Please complete all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }// Check if an image has been uploaded
        File imageFile_1 = imagePanel.getSelectedFile();  // Assuming you set the selected image file in CircleImagePanel
        if (imageFile_1== null) {
            JOptionPane.showMessageDialog(this, "Please upload a profile image.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM students WHERE Student_ID = ? OR Email = ?");
            checkStmt.setString(1, studentId);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Student ID or Email already exists.");
                return;
            }

            String sql = "INSERT INTO students (Student_ID, Full_Name, Email, Contact_Number, Password, Course, Year_Section, Date_of_Birth, Address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentId);
            stmt.setString(2, fullName);
            stmt.setString(3, email);
            stmt.setString(4, contact);
            stmt.setString(5, password);
            stmt.setString(6, course);
            stmt.setString(7, yearSection);
            stmt.setDate(8, new java.sql.Date(dobUtil.getTime()));
            stmt.setString(9, address);
            stmt.executeUpdate();

            File imageFile = imagePanel.getSelectedFile();
            if (imageFile != null) {
                String imgSql = "INSERT INTO student_images (Student_ID, Image_Data) VALUES (?, ?)";
                PreparedStatement imgStmt = conn.prepareStatement(imgSql);
                imgStmt.setString(1, studentId);
                try (FileInputStream fis = new FileInputStream(imageFile)) {
                    imgStmt.setBinaryStream(2, fis, (int) imageFile.length());
                    imgStmt.executeUpdate();
                }
            }

            
            new SuccessCreated().setVisible(true);
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void centerWindow() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }
}