package kldLostAndFound;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class AdminReportLost extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private String adminId;
    private AdminUI adminUI;
    private BackgroundPanel background;

    private UploadImagePanel[] imagePanels = new UploadImagePanel[6];
    private RoundedTextArea[] nameFields = new RoundedTextArea[6];
    private RoundedTextArea[] dateFields = new RoundedTextArea[6];
    private RoundedTextArea[] descFields = new RoundedTextArea[6];
    private RoundedTextArea[] fullNameFields = new RoundedTextArea[6];
    private int[] itemIds = new int[6];

    private int currentPage = 1;

    public AdminReportLost(String adminId, AdminUI adminUI) {
        this.adminId = adminId;
        this.adminUI = adminUI; 
        setTitle("Admin Report Lost - KLD Lost & Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 562);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        background = new BackgroundPanel("src/kldLostAndFound/images/AdminReportLost.jpg");
        background.setLayout(null);
        setContentPane(background);
        
        RoundedButton btnadmin = new RoundedButton("My Account", new Color(255, 240, 0), 0.8f, 11);
        btnadmin.setText("Admin Options");
        btnadmin.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnadmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               AdminNavigations frame = new AdminNavigations(adminId, adminUI);
				frame.setVisible(true);
				dispose(); // Close the current frame
            }
        });
        btnadmin.setBounds(566, 16, 129, 36);
        btnadmin.setForeground(Color.BLACK);
        background.add(btnadmin); 

        textField = new RoundedTextField(5);
        textField.setBounds(75, 149, 458, 20);
        background.add(textField);
        setPlaceholder(textField, " 🔎 Search Lost Item");

        JButton btnLeft = createHoverButton(loadIcon("turnLeft.jpg", 22, 20), "Previous");
        btnLeft.setBounds(46, 149, 22, 20);
        background.add(btnLeft);

        JButton btnRight = createHoverButton(loadIcon("turnRight.jpg", 22, 20), "Next");
        btnRight.setBounds(541, 149, 22, 20);
        background.add(btnRight);

        for (int i = 0; i < 6; i++) {
            int y = 211 + i * 48;

            imagePanels[i] = new UploadImagePanel(false);
            imagePanels[i].setBounds(40, y, 53, 47);
            background.add(imagePanels[i]);

            nameFields[i] = new RoundedTextArea(1, 1);
            nameFields[i].setBounds(98, y, 93, 47);
            nameFields[i].setEditable(false);
            background.add(nameFields[i]);

            fullNameFields[i] = new RoundedTextArea(1, 1);
            fullNameFields[i].setBounds(442, y, 129, 47);
            fullNameFields[i].setEditable(false);
            background.add(fullNameFields[i]);

            dateFields[i] = new RoundedTextArea(1, 1);
            dateFields[i].setBounds(192, y, 80, 47);
            dateFields[i].setEditable(false);
            background.add(dateFields[i]);

            descFields[i] = new RoundedTextArea(1, 1);
            descFields[i].setBounds(273, y, 168, 47);
            descFields[i].setEditable(false);
            background.add(descFields[i]);

            RoundedButton btnView = new RoundedButton("View", new Color(0, 128, 55), 0.8f, 11);
            btnView.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnView.setBounds(594, y, 83, 28);
            final int buttonIndex = i;
            btnView.addActionListener(e -> openItemDetails(buttonIndex));
            background.add(btnView);
        }

        btnRight.addActionListener(e -> {
            currentPage++;
            loadLostItems();
        });

        btnLeft.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadLostItems();
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchQuery = textField.getText().trim();
                if (searchQuery.isEmpty()) {
                    currentPage = 1;
                    loadLostItems();
                } else {
                    searchLostItems(searchQuery);
                }
            }
        });

        loadLostItems();
    }

    private void loadLostItems() {
        // Clear previous UI data
        for (int i = 0; i < 6; i++) {
            itemIds[i] = -1;
            nameFields[i].setText("");
            fullNameFields[i].setText("");
            dateFields[i].setText("");
            descFields[i].setText("");
            imagePanels[i].setImage(null);
        }

        String url = "jdbc:mysql://localhost:3306/lostandfound";
        String user = "root";
        String password = "";

        String sql = """
            SELECT li.Lost_Item_ID, li.Item_Name, li.Full_Name, li.Date_Lost, li.Item_Description, img.Image_Data
            FROM lost_items li
            LEFT JOIN lost_item_images img ON li.Lost_Item_ID = img.Lost_Item_ID
            WHERE li.archived = FALSE
            ORDER BY li.Date_Reported DESC
            LIMIT ?, 6
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, (currentPage - 1) * 6);
            try (ResultSet rs = stmt.executeQuery()) {
                int index = 0;
                while (rs.next() && index < 6) {
                    itemIds[index] = rs.getInt("Lost_Item_ID");

                    String itemName = rs.getString("Item_Name");
                    String fullName = rs.getString("Full_Name");
                    String dateLost = rs.getString("Date_Lost");
                    String description = rs.getString("Item_Description");

                    nameFields[index].setText(itemName != null ? itemName : "");
                    fullNameFields[index].setText(fullName != null ? fullName : "");
                    dateFields[index].setText(dateLost != null ? dateLost : "");
                    descFields[index].setText(description != null ? description : "");

                    byte[] imageData = rs.getBytes("Image_Data");
                    if (imageData != null) {
                        ImageIcon icon = new ImageIcon(imageData);
                        Image scaled = icon.getImage().getScaledInstance(
                                imagePanels[index].getWidth(),
                                imagePanels[index].getHeight(),
                                Image.SCALE_SMOOTH);
                        imagePanels[index].setImage(new ImageIcon(scaled));
                    } else {
                        imagePanels[index].setImage(null); // Optional: fallback/default image
                    }
                    index++;
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openItemDetails(int index) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
            String sql = "SELECT * FROM lost_items LEFT JOIN lost_item_images USING(Lost_Item_ID) WHERE Lost_Item_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, itemIds[index]);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String itemName = rs.getString("Item_Name");
                    String fullName = rs.getString("Full_Name");
                    String course = rs.getString("Course");
                    String location = rs.getString("Location");
                    String number = rs.getString("Contact_Number");
                    String dateLost = rs.getString("Date_Lost");
                    String description = rs.getString("Item_Description");
                    byte[] imageData = rs.getBytes("Image_Data");

                    ImageIcon imageIcon = null;
                    if (imageData != null) {
                        imageIcon = new ImageIcon(imageData);
                    }

                    AdminLostItemStats statsFrame = new AdminLostItemStats(
                    	    adminId,
                    	    adminUI,
                    	    itemName,
                    	    fullName,
                    	    course,
                    	    location,
                    	    number,
                    	    dateLost,
                    	    description,		
                    	    imageIcon,
                    	    itemIds[index]
                    	);

                    statsFrame.setVisible(true);
                    dispose();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load item details: " + e.getMessage());
        }
    }

    private ImageIcon loadIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/kldLostAndFound/images/" + path));
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
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

    private void setPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void searchLostItems(String searchQuery) {
        // Clear previous UI data
        for (int i = 0; i < 6; i++) {
            itemIds[i] = -1;
            nameFields[i].setText("");
            fullNameFields[i].setText("");
            dateFields[i].setText("");
            descFields[i].setText("");
            imagePanels[i].setImage(null);
        }

        String url = "jdbc:mysql://localhost:3306/lostandfound";
        String user = "root";
        String password = "";

        // SQL query to search items based on the search query (item name)
        String sql = """
            SELECT li.Lost_Item_ID, li.Item_Name, li.Full_Name, li.Date_Lost, li.Item_Description, img.Image_Data
            FROM lost_items li
            LEFT JOIN lost_item_images img ON li.Lost_Item_ID = img.Lost_Item_ID
            WHERE li.archived = FALSE AND li.Item_Name LIKE ?
            ORDER BY li.Date_Reported DESC
            LIMIT ?, 6
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the search query with wildcards for LIKE
            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setInt(2, 0);  // No pagination in search

            try (ResultSet rs = stmt.executeQuery()) {
                int index = 0;

                // Loop through the results and display the searched items
                while (rs.next() && index < 6) {
                    itemIds[index] = rs.getInt("Lost_Item_ID");

                    String itemName = rs.getString("Item_Name");
                    String fullName = rs.getString("Full_Name");
                    String dateLost = rs.getString("Date_Lost");
                    String description = rs.getString("Item_Description");

                    nameFields[index].setText(itemName != null ? itemName : "");
                    fullNameFields[index].setText(fullName != null ? fullName : "");
                    dateFields[index].setText(dateLost != null ? dateLost : "");
                    descFields[index].setText(description != null ? description : "");

                    byte[] imageData = rs.getBytes("Image_Data");
                    if (imageData != null) {
                        ImageIcon icon = new ImageIcon(imageData);
                        Image scaled = icon.getImage().getScaledInstance(
                                imagePanels[index].getWidth(),
                                imagePanels[index].getHeight(),
                                Image.SCALE_SMOOTH);
                        imagePanels[index].setImage(new ImageIcon(scaled));
                    } else {
                        imagePanels[index].setImage(null);
                    }

                    index++;
                }

                // Clear remaining rows
                for (int i = index; i < 6; i++) {
                    itemIds[i] = -1;
                    nameFields[i].setText("");
                    fullNameFields[i].setText("");
                    dateFields[i].setText("");
                    descFields[i].setText("");
                    imagePanels[i].setImage(null);
                }

                if (index == 0) {
                    JOptionPane.showMessageDialog(this, "No matching items found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
