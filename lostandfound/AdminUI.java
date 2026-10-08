package kldLostAndFound;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import javax.swing.*;

public class AdminUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField txtFullName, txtIdNumber, txtEmail, txtContact, txtAddress, txtCourse, txtYearSection, txtBirthday;
    private RoundedShadowLabel hoverLabel;
    private CircleImagePanel imagePanel;
    private String adminId;

    public AdminUI(String adminId) {
        this.adminId = adminId;

        setResizable(false);
        setTitle("Admin Account - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 794, 597);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel contentPane = new BackgroundPanel("src/kldLostAndFound/images/myadmin.jpg");
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        txtFullName = createTextField(50, 348, 243, 28, fieldFont);
        txtIdNumber = createTextField(384, 142, 309, 20, fieldFont);
        txtEmail = createTextField(384, 306, 309, 20, fieldFont);
        txtContact = createTextField(384, 341, 309, 20, fieldFont);
        txtAddress = createTextField(384, 244, 309, 20, fieldFont);
        txtCourse = createTextField(384, 173, 309, 20, fieldFont);
        txtYearSection = createTextField(384, 204, 309, 20, fieldFont);
        txtBirthday = createTextField(384, 275, 309, 20, fieldFont);

        contentPane.add(txtFullName);
        contentPane.add(txtIdNumber);
        contentPane.add(txtEmail);
        contentPane.add(txtContact);
        contentPane.add(txtAddress);
        contentPane.add(txtCourse);
        contentPane.add(txtYearSection);
        contentPane.add(txtBirthday);

        // Circle Image Panel
        imagePanel = new CircleImagePanel(false);
        imagePanel.setBounds(62, 123, 208, 201);
        imagePanel.setPreferredSize(new Dimension(150, 150));
        contentPane.add(imagePanel);

        JLabel lblUpload = new JLabel("Upload Image");
        lblUpload.setBounds(740, 396, 150, 14);
        lblUpload.setFont(new Font("Tahoma", Font.ITALIC, 11));
        lblUpload.setHorizontalAlignment(SwingConstants.CENTER);
        lblUpload.setForeground(Color.DARK_GRAY);
        contentPane.add(lblUpload);

        RoundedButton btnSignOut = new RoundedButton("Sign Out", new Color(255, 242, 0), 0.8f, 11);
        btnSignOut.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnSignOut.addActionListener(e -> {
            LogInUI loginUI = new LogInUI();
            loginUI.frmKldLostAnd.setVisible(true);
            dispose();
        });
        btnSignOut.setBounds(663, 20, 91, 36);
        contentPane.add(btnSignOut);

        hoverLabel = new RoundedShadowLabel("", 4);
        hoverLabel.setSize(150, 25);
        hoverLabel.setVisible(false);
        hoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hoverLabel.setVerticalAlignment(SwingConstants.CENTER);
        hoverLabel.setBorder(null);
        contentPane.add(hoverLabel);

        // Load button icons
        ImageIcon lostIcon = loadIcon("ReportLostItemButton.jpg", 150, 116);
        ImageIcon foundIcon = loadIcon("ReportFoundItemButton.jpg", 150, 116);
        ImageIcon listIcon = loadIcon("AdminOptions.jpg", 150, 116);
        ImageIcon historyIcon = loadIcon("HistoryButton.jpg", 150, 116);

        int buttonWidth = 150;
        int buttonHeight = 116;
        int spacing = 15;
        int totalWidth = (4 * buttonWidth) + (3 * spacing);
        int startX = (getWidth() - totalWidth) / 2 - 8;
        int y = 407;

        contentPane.add(createHoverButton(listIcon, startX, y, buttonWidth, buttonHeight, "Admin Options"));
        contentPane.add(createHoverButton(lostIcon, startX + (buttonWidth + spacing), y, buttonWidth, buttonHeight, "Report Lost Item"));
        contentPane.add(createHoverButton(foundIcon, startX + 2 * (buttonWidth + spacing), y, buttonWidth, buttonHeight, "Report Found Items"));
        contentPane.add(createHoverButton(historyIcon, startX + 3 * (buttonWidth + spacing), y, buttonWidth, buttonHeight, "Go to Account History"));

        loadAdminDetails();
    }

    private JTextField createTextField(int x, int y, int width, int height, Font font) {
        JTextField field = new JTextField();
        field.setBounds(x, y, width, height);
        field.setEditable(false);
        field.setOpaque(false);
        field.setForeground(Color.BLACK);
        field.setFont(font);
        field.setBorder(null);
        return field;
    }

    private JButton createHoverButton(ImageIcon icon, int x, int y, int width, int height, String hoverText) {
        JButton button = new RoundedIconButton(icon, 25);
        button.setBounds(x, y, width, height);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setLocation(button.getX(), button.getY() - 3);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                hoverLabel.setText(hoverText);
                hoverLabel.setLocation(button.getX() + (button.getWidth() - hoverLabel.getWidth()) / 2,
                        button.getY() - hoverLabel.getHeight() - 6);
                hoverLabel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setLocation(button.getX(), button.getY() + 3);
                button.setBorder(null);
                hoverLabel.setVisible(false);
            }
        });

        button.addActionListener(e -> {
            switch (hoverText) {
                case "Admin Options":
                    new AdminNavigations(adminId, AdminUI.this).setVisible(true);
                    dispose();
                    break;
                case "Report Lost Item":
                    new AdminReportLostItems(adminId,AdminUI.this).setVisible(true);
                    dispose();
                    break;
                case "Report Found Items":
                    new AdminReportFoundItems(adminId, AdminUI.this).setVisible(true);
                    setVisible(false);
                    break;
                case "Go to Account History":
                    new AdminAccountHistory(adminId, AdminUI.this).setVisible(true);
                    setVisible(false);
                    break;
            }
        });

        return button;
    }

    private ImageIcon loadIcon(String fileName, int width, int height) {
        ImageIcon icon;
        URL resource = getClass().getResource("/kldLostAndFound/images/" + fileName);
        if (resource == null) {
            icon = new ImageIcon("src/kldLostAndFound/images/" + fileName);
        } else {
            icon = new ImageIcon(resource);
        }
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void loadAdminDetails() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
            String sql = "SELECT a.Admin_ID, a.Full_Name, a.Email, i.Image_Data " +
                         "FROM admin a " +
                         "LEFT JOIN admin_images i ON a.Admin_ID = i.Admin_ID " +
                         "WHERE a.Admin_ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, adminId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtFullName.setText(rs.getString("Full_Name"));
                txtIdNumber.setText(rs.getString("Admin_ID"));
                txtEmail.setText(rs.getString("Email"));
                txtContact.setText("N/A");
                txtAddress.setText("N/A");
                txtCourse.setText("N/A");
                txtYearSection.setText("N/A");
                txtBirthday.setText("N/A");

                byte[] imageBytes = rs.getBytes("Image_Data");
                if (imageBytes != null && imageBytes.length > 0) {
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image scaled = icon.getImage().getScaledInstance(208, 201, Image.SCALE_SMOOTH);
                    imagePanel.setImage1(new ImageIcon(scaled));
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
