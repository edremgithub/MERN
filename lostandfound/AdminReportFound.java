package kldLostAndFound;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class AdminReportFound extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField searchField;
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

    public AdminReportFound(String adminId, AdminUI adminUI) {
        this.adminId = adminId;
        this.adminUI = adminUI;
        setTitle("Admin Report Found - KLD Lost & Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 562);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());

        background = new BackgroundPanel("src/kldLostAndFound/images/AdminReportFound.jpg");
        background.setLayout(null);
        setContentPane(background);

        RoundedButton btnAdmin = new RoundedButton("Admin Options", new Color(255, 240, 0), 0.8f, 11);
        btnAdmin.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnAdmin.setBounds(566, 16, 129, 36);
        btnAdmin.setForeground(Color.BLACK);
        btnAdmin.addActionListener(e -> {
            AdminNavigations frame = new AdminNavigations(adminId, adminUI);
            frame.setVisible(true);
            dispose();
        });
        background.add(btnAdmin);

        searchField = new RoundedTextField(5);
        searchField.setBounds(75, 149, 458, 20);
        background.add(searchField);
        setPlaceholder(searchField, " 🔎 Search Found Item");

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
            final int index = i;
            btnView.addActionListener(e -> openItemDetails(index));
            background.add(btnView);
        }

        btnRight.addActionListener(e -> {
            currentPage++;
            loadFoundItems();
        });

        btnLeft.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadFoundItems();
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().trim();
                if (query.isEmpty()) {
                    currentPage = 1;
                    loadFoundItems();
                } else {
                    searchFoundItems(query);
                }
            }
        });

        loadFoundItems();
    }

    private void loadFoundItems() {
        // Clear previous data
        for (int i = 0; i < 6; i++) {
            itemIds[i] = -1;
            nameFields[i].setText("");
            fullNameFields[i].setText("");
            dateFields[i].setText("");
            descFields[i].setText("");
            imagePanels[i].setImage(null);
        }

        String sql = """
            SELECT fi.item_id, fi.item_name, fi.full_name, fi.date_found, fi.description, img.image_data
            FROM reported_found_items fi
            LEFT JOIN reported_found_images img ON fi.item_id = img.item_id
            WHERE fi.archived = FALSE
            ORDER BY fi.date_found DESC
            LIMIT ?, 6
        """;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, (currentPage - 1) * 6);
            ResultSet rs = stmt.executeQuery();
            
            // Debug print
            System.out.println("Loading found items for page " + currentPage);
            
            int index = 0;
            while (rs.next() && index < 6) {
                itemIds[index] = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                String fullName = rs.getString("full_name");
                String dateFound = rs.getString("date_found");
                String description = rs.getString("description");
                
                // Debug print
                System.out.println("Loading item: id=" + itemIds[index] + 
                                 ", name='" + itemName + "'" +
                                 ", fullName='" + fullName + "'" +
                                 ", date='" + dateFound + "'");

                nameFields[index].setText(itemName != null ? itemName : "");
                fullNameFields[index].setText(fullName != null ? fullName : "");
                dateFields[index].setText(dateFound != null ? dateFound : "");
                descFields[index].setText(description != null ? description : "");

                byte[] imgData = rs.getBytes("image_data");
                if (imgData != null) {
                    ImageIcon icon = new ImageIcon(imgData);
                    Image scaled = icon.getImage().getScaledInstance(imagePanels[index].getWidth(), imagePanels[index].getHeight(), Image.SCALE_SMOOTH);
                    imagePanels[index].setImage(new ImageIcon(scaled));
                }
                index++;
            }
            
            // Debug print
            System.out.println("Loaded " + index + " items");
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading items: " + e.getMessage());
        }
    }

    private void searchFoundItems(String query) {
        for (int i = 0; i < 6; i++) {
            itemIds[i] = -1;
            nameFields[i].setText("");
            fullNameFields[i].setText("");
            dateFields[i].setText("");
            descFields[i].setText("");
            imagePanels[i].setImage(null);
        }

        String sql = """
            SELECT fi.item_id, fi.item_name, fi.full_name, fi.date_found, fi.description, img.image_data
            FROM reported_found_items fi
            LEFT JOIN reported_found_images img ON fi.item_id = img.item_id
            WHERE fi.archived = FALSE AND fi.item_name LIKE ?
            ORDER BY fi.date_found DESC
            LIMIT 6
        """;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            int index = 0;
            while (rs.next() && index < 6) {
                itemIds[index] = rs.getInt("item_id");

                nameFields[index].setText(rs.getString("item_name"));
                fullNameFields[index].setText(rs.getString("full_name"));
                dateFields[index].setText(rs.getString("date_found"));
                descFields[index].setText(rs.getString("description"));

                byte[] imgData = rs.getBytes("image_data");
                if (imgData != null) {
                    ImageIcon icon = new ImageIcon(imgData);
                    Image scaled = icon.getImage().getScaledInstance(imagePanels[index].getWidth(), imagePanels[index].getHeight(), Image.SCALE_SMOOTH);
                    imagePanels[index].setImage(new ImageIcon(scaled));
                }
                index++;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
    }

    private void openItemDetails(int index) {
        if (itemIds[index] == -1) {
            System.out.println("No item at index " + index);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
            String sql = """
                SELECT fi.*, img.image_data 
                FROM reported_found_items fi
                LEFT JOIN reported_found_images img ON fi.item_id = img.item_id 
                WHERE fi.item_id = ?
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, itemIds[index]);
                ResultSet rs = stmt.executeQuery();
                
                // Debug print
                System.out.println("Opening details for item_id: " + itemIds[index]);
                
                if (rs.next()) {
                    String itemName = rs.getString("item_name");
                    String fullName = rs.getString("full_name");
                    String course = rs.getString("course");
                    String location = rs.getString("location_found");
                    String number = rs.getString("contact_number");
                    String dateFound = rs.getString("date_found");
                    String description = rs.getString("description");
                    String studentId = rs.getString("student_id");
                    byte[] imageData = rs.getBytes("image_data");

                    // Debug print
                    System.out.println("Item details: name='" + itemName + 
                                     "', fullName='" + fullName + 
                                     "', course='" + course + 
                                     "', location='" + location + "'");

                    ImageIcon imageIcon = null;
                    if (imageData != null) {
                        imageIcon = new ImageIcon(imageData);
                        System.out.println("Image data loaded, size: " + imageData.length + " bytes");
                    } else {
                        System.out.println("No image data found for item");
                    }

                    AdminFoundItemVerification frame = new AdminFoundItemVerification(
                        adminId,
                        adminUI,
                        studentId,
                        itemName,
                        fullName,
                        course,
                        location,
                        number,
                        dateFound,
                        description,
                        imageIcon,
                        itemIds[index]
                    );
                    frame.setVisible(true);
                    dispose();
                } else {
                    System.out.println("No item found with id: " + itemIds[index]);
                    JOptionPane.showMessageDialog(this, "Item not found in database");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load item details: " + e.getMessage());
        }
    }

    private ImageIcon loadIcon(String fileName, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/kldLostAndFound/images/" + fileName));
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
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

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }
}
