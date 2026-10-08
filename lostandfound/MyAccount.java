package kldLostAndFound;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class MyAccount extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtFullName, txtIdNumber, txtEmail, txtContact, txtAddress, txtCourse, txtYearSection, txtBirthday;
    private RoundedShadowLabel hoverLabel;
    private String currentIdNumber;
    private CircleImagePanel imagePanel;
  
    public MyAccount(String idNumber) {
        this.currentIdNumber = idNumber;
        

        setTitle("My Account - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 794, 597);
        setResizable(false);
        setLocationRelativeTo(null); // Center the window on screen
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel contentPane = new BackgroundPanel("src/kldLostAndFound/images/myaccount.jpg");
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Font fieldFont = new Font("Tahoma", Font.BOLD, 13);

        txtFullName = createTextField(50, 348, 243, 28, fieldFont, SwingConstants.CENTER); // Centered
        contentPane.add(txtFullName);

        txtIdNumber = createTextField(384, 142, 309, 20, fieldFont, SwingConstants.LEADING); // Left-aligned
        contentPane.add(txtIdNumber);

        txtEmail = createTextField(384, 306, 309, 20, fieldFont, SwingConstants.LEADING);
        contentPane.add(txtEmail);

        txtContact = createTextField(384, 341, 309, 20, fieldFont, SwingConstants.LEADING);
        contentPane.add(txtContact);

        txtAddress = createTextField(384, 244, 309, 20, fieldFont, SwingConstants.LEADING);
        contentPane.add(txtAddress);

        txtCourse = createTextField(384, 173, 309, 20, fieldFont, SwingConstants.LEADING);
        contentPane.add(txtCourse);

        txtYearSection = createTextField(384, 204, 309, 20, fieldFont, SwingConstants.LEADING);
        contentPane.add(txtYearSection);

        txtBirthday = createTextField(384, 275, 309, 20, fieldFont, SwingConstants.LEADING);
        contentPane.add(txtBirthday);


        imagePanel = new CircleImagePanel(false);
        imagePanel.setBounds(62, 123, 208, 201);
        imagePanel.setEditable(false);
        imagePanel.setPreferredSize(new Dimension(150, 150));
        contentPane.add(imagePanel);

     

        RoundedButton btnSignOut = new RoundedButton("Sign Out", new Color(0, 128, 55), 0.8f, 11);
        btnSignOut.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnSignOut.setBounds(663, 20, 91, 36);
        btnSignOut.addActionListener(e -> {
        	
            new LogInUI(); // Or whatever your login frame is
            dispose();
        });
        contentPane.add(btnSignOut);

        hoverLabel = new RoundedShadowLabel("", 4);
        hoverLabel.setSize(150, 25);
        hoverLabel.setVisible(false);
        hoverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(hoverLabel);

        ImageIcon lostIcon = loadIcon("ReportLostItemButton.jpg", 150, 116);
        ImageIcon foundIcon = loadIcon("ReportFoundItemButton.jpg", 150, 116);
        ImageIcon listIcon = loadIcon("listbutton.jpg", 150, 116);
        ImageIcon historyIcon = loadIcon("HistoryButton.jpg", 150, 116);

        int buttonWidth = 150;
        int buttonHeight = 116;
        int spacing = 15;
        int totalWidth = (4 * buttonWidth) + (3 * spacing);
        int startX = (getWidth() - totalWidth) / 2 - 8;
        int y = 407;

        contentPane.add(createHoverButton(listIcon, startX, y, buttonWidth, buttonHeight, "View Lost & Found Items"));
        contentPane.add(createHoverButton(lostIcon, startX + (buttonWidth + spacing), y, buttonWidth, buttonHeight, "Report Lost Item"));
        contentPane.add(createHoverButton(foundIcon, startX + 2 * (buttonWidth + spacing), y, buttonWidth, buttonHeight, "Report Found Items"));
        contentPane.add(createHoverButton(historyIcon, startX + 3 * (buttonWidth + spacing), y, buttonWidth, buttonHeight, "Go to Account History"));

        loadStudentInfo(); // ✅ Load data last

        setLocationRelativeTo(null); // Center the window on screen (must be last)
    }

    private void loadStudentInfo() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
            
            // Retrieve student information
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE Student_ID = ?");
            stmt.setString(1, currentIdNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Fill in student details
                txtFullName.setText(rs.getString("Full_Name"));
                txtIdNumber.setText(rs.getString("Student_ID"));
                txtEmail.setText(rs.getString("Email"));
                txtContact.setText(rs.getString("Contact_Number"));
                txtAddress.setText(rs.getString("Address"));
                txtCourse.setText(rs.getString("Course"));
                txtYearSection.setText(rs.getString("Year_section"));
                txtBirthday.setText(rs.getString("Date_of_Birth"));
            } else {
                JOptionPane.showMessageDialog(this, "Student not found.");
                return;  // Exit if no student found
            } 

            rs.close();
            stmt.close();

            // Retrieve student image
            stmt = conn.prepareStatement("SELECT Image_Data FROM student_images WHERE Student_ID = ?");
            stmt.setString(1, currentIdNumber);
            rs = stmt.executeQuery();

            if (rs.next()) {
                byte[] imgBytes = rs.getBytes("Image_Data");

                if (imgBytes != null) {
                    // If image is found, convert byte array to BufferedImage
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgBytes));
                    imagePanel.setImage(img);  // Assuming imagePanel has setImage method to display the image
                } else {
                    // If no image found, set a default image or placeholder
                    imagePanel.setImage(null);  // Assuming you want to clear the image in the panel if no image is found
                }
            } else {
                JOptionPane.showMessageDialog(this, "No image found for the student.");
            }

            rs.close();
            stmt.close();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + sqlEx.getMessage());
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading image data.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load account info.");
        }
    }


    private JTextField createTextField(int x, int y, int width, int height, Font font, int alignment) {
        JTextField field = new JTextField();
        field.setBounds(x, y, width, height);
        field.setEditable(false);
        field.setOpaque(false);
        field.setForeground(Color.BLACK);
        field.setFont(font);
        field.setBorder(null);
        field.setHorizontalAlignment(alignment); // ← set alignment dynamically
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
                hoverLabel.setLocation(button.getX(), button.getY() - 25);
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
            case "View Lost & Found Items" -> {
                new SearchItem(currentIdNumber, MyAccount.this).setVisible(true);
                setVisible(false);
            }
            case "Report Lost Item" -> {
                new ReportForm(currentIdNumber, MyAccount.this).setVisible(true);
                setVisible(false);
            }
            case "Report Found Items" -> {
                new ReportFound(currentIdNumber, MyAccount.this).setVisible(true);
                setVisible(false);
            }
            case "Go to Account History" -> {
                new AccountHistory(currentIdNumber, MyAccount.this).setVisible(true);
                setVisible(false);
            }
        }

        });

        return button;
    }

    private ImageIcon loadIcon(String fileName, int width, int height) {
        URL resource = getClass().getResource("/kldLostAndFound/images/" + fileName);
        ImageIcon icon = (resource != null)
            ? new ImageIcon(resource)
            : new ImageIcon("src/kldLostAndFound/images/" + fileName);

        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}
