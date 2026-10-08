package kldLostAndFound;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class LogInUI {

    protected JFrame frmKldLostAnd;
    private JPasswordField txtPassword;
    private JTextField txtEmail;

    // MySQL JDBC connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lostandfound"; // Database URL
    private static final String DB_USERNAME = "root"; // MySQL username
    private static final String DB_PASSWORD = ""; // MySQL password

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LogInUI window = new LogInUI();
                    window.frmKldLostAnd.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LogInUI() {
        initialize();
    }

    private void initialize() {	
        frmKldLostAnd = new JFrame();
        frmKldLostAnd.setResizable(false);
        frmKldLostAnd.setTitle("KLD Lost & Found");
        frmKldLostAnd.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set the window icon
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg"));
        frmKldLostAnd.setIconImage(logoIcon.getImage());

        // Set background panel with image
        String imagePath = "src/kldLostAndFound/images/loginbg.jpg";
        BackgroundPanel contentPane = new BackgroundPanel(imagePath);

        frmKldLostAnd.setBounds(100, 100, 359, 457);
        frmKldLostAnd.setContentPane(contentPane);
        contentPane.setLayout(null);

        // Labels and Fields
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEmail.setBounds(63, 119, 71, 20);
        contentPane.add(lblEmail);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPassword.setBounds(63, 172, 71, 20);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(63, 203, 188, 20);
        contentPane.add(txtPassword);

        // Eye button for password visibility toggle
        JButton eyeButton = new JButton();
        ImageIcon eyeIcon = new ImageIcon(getClass().getResource("/kldLostAndFound/images/eyeIcon.png")); // Add your own eye icon here
        ImageIcon eyeSlashIcon = new ImageIcon(getClass().getResource("/kldLostAndFound/images/eyeslashIcon.png")); // Add your own eye-slash icon here
        eyeButton.setIcon(eyeIcon);
        eyeButton.setBounds(249, 203, 24, 20); // Adjusted y-coordinate to place it above the password field
        contentPane.add(eyeButton);

        // Add ActionListener to toggle password visibility
        eyeButton.addActionListener(new ActionListener() {
            boolean isPasswordVisible = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPasswordVisible) {
                    txtPassword.setEchoChar('•'); // Mask password again
                    eyeButton.setIcon(eyeIcon); // Change to eye icon
                } else {
                    txtPassword.setEchoChar((char) 0); // Show password as plain text
                    eyeButton.setIcon(eyeSlashIcon); // Change to eye-slash icon
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

        // Create Account Button
        RoundedButton btnCreate = new RoundedButton("Create Account", new Color(0, 128, 55), 0.8f, 11);
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateAccount createAccount = new CreateAccount();
                createAccount.setVisible(true);
                frmKldLostAnd.dispose();
            }
        });
        btnCreate.setBounds(59, 265, 107, 23);
        contentPane.add(btnCreate);

        // Login Button
        RoundedButton btnLogin = new RoundedButton("Log In", new Color(255, 240, 0), 0.8f, 11);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();

                // Check if email and password are both entered
                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(btnLogin, "Please enter both email and password.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
                    return; // Stop further processing if fields are empty
                }

                // Login logic for student or admin
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                    // First try student login - using BINARY for case-sensitive comparison
                    String studentSQL = "SELECT * FROM students WHERE BINARY Email = ? AND Password = ?";
                    PreparedStatement studentStmt = conn.prepareStatement(studentSQL);
                    studentStmt.setString(1, email);
                    studentStmt.setString(2, password);
                    ResultSet studentRs = studentStmt.executeQuery();

                    if (studentRs.next()) {
                        String idNumber = studentRs.getString("Student_ID");
                        MyAccount myAccount = new MyAccount(idNumber);
                        myAccount.setVisible(true);
                        frmKldLostAnd.dispose();
                        return;
                    }

                    // If not student, try admin login - using BINARY for case-sensitive comparison
                    String adminSQL = "SELECT * FROM admin WHERE BINARY Email = ? AND Password = ?";
                    PreparedStatement adminStmt = conn.prepareStatement(adminSQL);
                    adminStmt.setString(1, email);
                    adminStmt.setString(2, password);
                    ResultSet adminRs = adminStmt.executeQuery();

                    if (adminRs.next()) {
                        String adminId = adminRs.getString("Admin_ID");
                        AdminUI adminDashboard = new AdminUI(adminId);
                        adminDashboard.setVisible(true);
                        frmKldLostAnd.dispose();
                        return;
                    }

                    // ❌ Neither student nor admin found
                    JOptionPane.showMessageDialog(btnLogin, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(btnLogin, "Login error. Please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnLogin.setBounds(174, 265, 107, 23);
        btnLogin.setForeground(Color.BLACK);
        contentPane.add(btnLogin);

        // Email Field
        txtEmail = new JTextField();
        txtEmail.setBounds(63, 141, 210, 20);
        contentPane.add(txtEmail);
        txtEmail.setColumns(10);

        // Center the window on screen
        frmKldLostAnd.setLocationRelativeTo(null);
        frmKldLostAnd.setVisible(true);
        frmKldLostAnd.getRootPane().setDefaultButton(btnLogin);
        
        JButton btnAboutUS = new JButton("About Us");
        btnAboutUS.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnAboutUS.setBorderPainted(false);
        btnAboutUS.setContentAreaFilled(false);
        btnAboutUS.setFocusPainted(false);
        btnAboutUS.setOpaque(false);
        btnAboutUS.setForeground(new Color(0, 128, 55));
        btnAboutUS.setBounds(119, 297, 103, 23);
        btnAboutUS.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAboutUS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // About Us action
            }
        });
        // Add hover effect (underline text using HTML)
        btnAboutUS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAboutUS.setText("<html><u>About Us</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAboutUS.setText("About Us");
            }
        });
        contentPane.add(btnAboutUS);
    }
}
