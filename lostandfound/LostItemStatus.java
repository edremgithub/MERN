package kldLostAndFound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.sql.*;

public class LostItemStatus extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private UploadImagePanel imagePanel;
    private JTextField textField_1, textField_2, textField_3, textField_4, textField_5, textField_6;
    private RoundedTextArea textArea;

    private int itemId;
    private String idNumber;
    private MyAccount myAccount;


    public LostItemStatus(int itemId , String idNumber, MyAccount myAccount) {
        this.itemId = itemId;
        this.idNumber = idNumber;
        this.myAccount = myAccount;

        setTitle("Request Claim - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 562);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/LostItemStatus.jpg");
        setContentPane(background);
        background.setLayout(null);

        imagePanel = new UploadImagePanel(false);
        imagePanel.setBounds(67, 145, 141, 141);
        background.add(imagePanel);

        RoundedButton btnYes = new RoundedButton("Yes", new Color(255, 240, 0), 0.8f, 11);
        btnYes.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new ReportFound(
        			idNumber,
        			myAccount,
        			textField_3.getText(), // Item Name
        			"", // Full Name (do not pass, leave blank)
        			textField_2.getText(), // Course
        			textField_4.getText(), // Location
        			textField_1.getText(), // Contact Number
        			textArea.getText()     // Description
        		).setVisible(true);
        		dispose();
        	}
        });
        btnYes.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnYes.setBounds(297, 410, 121, 37);
        background.add(btnYes);

        RoundedButton btnBack = new RoundedButton("Back", new Color(0, 128, 55), 0.8f, 11);
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnBack.setBounds(598, 19, 91, 36);
        btnBack.addActionListener(e -> {
        	new SearchItem(idNumber, null).setVisible(true);
            dispose();
        });
        background.add(btnBack);

        // Text Fields
        textField_3 = createTextField("Item Name", 258, 156); background.add(textField_3);
        textField_5 = createTextField("Full Name", 477, 156); background.add(textField_5);
        textField_2 = createTextField("Course", 258, 187); background.add(textField_2);
        textField_4 = createTextField("Location", 477, 187); background.add(textField_4);
        textField_1 = createTextField("Number", 259, 218); background.add(textField_1);
        textField_6 = createTextField("Date Lost", 477, 218); background.add(textField_6);

        // Description area
        textArea = new RoundedTextArea(5, 30);
        textArea.setEditable(false);
        textArea.setBounds(256, 249, 380, 43);
        background.add(textArea);

        // Load item info from database
        loadItemDetails();
    }

    private JTextField createTextField(String placeholder, int x, int y) {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setBounds(x, y, 159, 20);
        tf.setText(placeholder);
        tf.setForeground(Color.GRAY);
        return tf;
    }

    private void loadItemDetails() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT li.Item_Name, li.Full_Name, li.Course, li.Location, li.Contact_Number, li.Date_Lost, li.Item_Description, lii.Image_Data " +
                 "FROM lost_items li " +
                 "LEFT JOIN lost_item_images lii ON li.Lost_Item_ID = lii.Lost_Item_ID " +
                 "WHERE li.Lost_Item_ID = ?")) {

            stmt.setInt(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Populate text fields
                    textField_3.setText(rs.getString("Item_Name"));
                    textField_5.setText(rs.getString("Full_Name"));
                    textField_2.setText(rs.getString("Course"));
                    textField_4.setText(rs.getString("Location"));
                    textField_1.setText(rs.getString("Contact_Number"));
                    textField_6.setText(rs.getString("Date_Lost"));
                    textArea.setText(rs.getString("Item_Description"));

                    // Load image if available
                    InputStream imgStream = rs.getBinaryStream("Image_Data");
                    if (imgStream != null) {
                        ImageIcon icon = new ImageIcon(imgStream.readAllBytes());
                        Image img = icon.getImage().getScaledInstance(141, 141, Image.SCALE_SMOOTH);
                        imagePanel.setImage(new ImageIcon(img));
                        imagePanel.repaint();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load item details.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
