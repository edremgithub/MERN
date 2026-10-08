package kldLostAndFound;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AdminReportItemFoundArchive extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
    private RoundedTextArea text_1;
    private RoundedShadowLabel hoverLabel;
    private RoundedTextField textField_2;
    private UploadImagePanel imagePanel_1;
	  private UploadImagePanel imagePanel_3;
	  private UploadImagePanel imagePanel_4;
	  private UploadImagePanel imagePanel_5;
	  private UploadImagePanel imagePanel_6;
	  private UploadImagePanel imagePanel_2;
	  private JCheckBox chckbx_1, chckbx_2, chckbx_3, chckbx_4, chckbx_5, chckbx_6;
   private String adminId;
   private AdminUI adminUI;
   private int[] itemIds = new int[6];
   private UploadImagePanel[] imagePanels = new UploadImagePanel[6];
   private RoundedTextArea[] itemNameFields = new RoundedTextArea[6];
   private RoundedTextArea[] dateFields = new RoundedTextArea[6];
   private RoundedTextArea[] descFields = new RoundedTextArea[6];
   private RoundedTextArea[] fullNameFields = new RoundedTextArea[6];
   private int currentPage = 1;

	public AdminReportItemFoundArchive( String adminId, AdminUI adminUI) {
		this.adminId = adminId;
		this.adminUI = adminUI;
		 setTitle("Admin Report Item Found Archive - KLD Lost & Found");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setBounds(100, 100, 730, 562);
	        setResizable(false);
	        setLocationRelativeTo(null); // Center the window on the screen
	        
	        // Set app icon		
	        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());
	        
	        // Set background
	        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AdminReportItemFoundArchive.jpg");
	        background.setLayout(null);
	        
	        // Initialize contentPane and set background as content pane
	        contentPane = new JPanel();
	        contentPane.setLayout(null);  // Set layout as null
	        setContentPane(background);  // Set background panel as the content pane
	        
	        // Admin Options Button
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
	        background.add(btnadmin);  // Add button to background panel, not contentPane
	        
	        
	        // Upload Image Panel
	        imagePanel_2 = new UploadImagePanel(false);
	        imagePanel_2.setBounds(40, 259, 53, 47);
	        background.add(imagePanel_2);
	        
	        imagePanel_1 = new UploadImagePanel(false);
	        imagePanel_1.setBounds(40, 211, 53, 47);
	        background.add(imagePanel_1);
	        
	        imagePanel_3 = new UploadImagePanel(false);
	        imagePanel_3.setBounds(40, 308, 53, 47);
	        background.add(imagePanel_3);
	        
	        imagePanel_5 = new UploadImagePanel(false);
	        imagePanel_5.setBounds(40, 405, 53, 47);
	        background.add(imagePanel_5);
	        
	        imagePanel_4 = new UploadImagePanel(false);
	        imagePanel_4.setBounds(40, 356, 53, 47);
	        background.add(imagePanel_4);
	        
	        imagePanel_6 = new UploadImagePanel(false);
	        imagePanel_6.setBounds(40, 454, 53, 47);
	        background.add(imagePanel_6);
	        
	        // Text Field for the input
	        textField = new RoundedTextField(5);
	        textField.setBounds(75, 149, 458, 20);
	        background.add(textField);
	        textField.setColumns(10);
	        setPlaceholder(textField, " 🔎 Search Found Item");

	        // Initialize hover shadow label
	        hoverLabel = new RoundedShadowLabel();
	        hoverLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
	        hoverLabel.setForeground(Color.WHITE);
	        hoverLabel.setVisible(false);
	        background.add(hoverLabel);
	        background.setComponentZOrder(hoverLabel, 0); // Ensure it's on top

	        // Load icons for buttons
	        ImageIcon leftIcon = loadIcon("turnLeft.jpg", 22, 20);
	        ImageIcon rightIcon = loadIcon("turnRight.jpg", 22, 20);

	        // Left button (Previous)
	        JButton btnLeft = createHoverButton(leftIcon, "Previous");
	        btnLeft.setBounds(46, 149, 22, 20); // Adjust the position manually here
	        background.add(btnLeft);

	        // Right button (Next)
	        JButton btnRight = createHoverButton(rightIcon, "Next");
	        btnRight.setBounds(541, 149, 22, 20); // Adjust the position manually here
	        background.add(btnRight);
	        
	        text_1 = new RoundedTextArea(1 , 1);
			text_1.setBounds(98, 211, 93, 47);
			background.add(text_1);
	        
	        RoundedTextArea text_2 = new RoundedTextArea(1, 1);
			text_2.setBounds(98, 259, 93, 47);
			background.add(text_2);
			
			RoundedTextArea text_3 = new RoundedTextArea(1, 1);
			text_3.setBounds(98, 308, 93, 47);
			background.add(text_3);
			
			RoundedTextArea text_4 = new RoundedTextArea(1, 1);
			text_4.setBounds(98, 356, 93, 47);
			background.add(text_4);
			
			RoundedTextArea text_5 = new RoundedTextArea(1, 1);
			text_5.setBounds(98, 405, 93, 47);
			background.add(text_5);
			
			RoundedTextArea text_6 = new RoundedTextArea(1, 1);
			text_6.setBounds(98, 454, 93, 47);
			background.add(text_6);
			
			RoundedTextArea text_date_1 = new RoundedTextArea(1, 1);
			text_date_1.setBounds(192, 211, 80, 47);
			background.add(text_date_1);
			
			RoundedTextArea text_date_2 = new RoundedTextArea(1, 1);
			text_date_2.setBounds(192, 259, 80, 47);
			background.add(text_date_2);
			
			RoundedTextArea text_date_3 = new RoundedTextArea(1, 1);
			text_date_3.setBounds(192, 308, 80, 47);
			background.add(text_date_3);
			
			RoundedTextArea text_date_4 = new RoundedTextArea(1, 1);
			text_date_4.setBounds(192, 356, 80, 47);
			background.add(text_date_4);
			
			RoundedTextArea text_date_5 = new RoundedTextArea(1, 1);
			text_date_5.setBounds(192, 405, 80, 47);
			background.add(text_date_5);
			
			RoundedTextArea text_date_2_1 = new RoundedTextArea(1, 1);
			text_date_2_1.setBounds(192, 454, 80, 47);
			background.add(text_date_2_1);
			
			RoundedTextArea text_des_1 = new RoundedTextArea(1, 1);
			text_des_1.setBounds(273, 211, 168, 47);
			background.add(text_des_1);
			
			RoundedTextArea text_name_1 = new RoundedTextArea(1, 1);
			text_name_1.setBounds(442, 211, 129, 47);
			background.add(text_name_1);
			
			RoundedTextArea text_des_2 = new RoundedTextArea(1, 1);
			text_des_2.setBounds(273, 259, 168, 47);
			background.add(text_des_2);
			
			RoundedTextArea text_des_3 = new RoundedTextArea(1, 1);
			text_des_3.setBounds(273, 308, 168, 47);
			background.add(text_des_3);
			
			RoundedTextArea text_des_4 = new RoundedTextArea(1, 1);
			text_des_4.setBounds(273, 356, 168, 47);
			background.add(text_des_4);
			
			RoundedTextArea text_des_5 = new RoundedTextArea(1, 1);
			text_des_5.setBounds(273, 405, 168, 47);
			background.add(text_des_5);
			
			RoundedTextArea text_des_6 = new RoundedTextArea(1, 1);
			text_des_6.setBounds(273, 454, 168, 47);
			background.add(text_des_6);
			
			RoundedTextArea text_name_2 = new RoundedTextArea(1, 1);
			text_name_2.setBounds(442, 259, 129, 47);
			background.add(text_name_2);
			
			RoundedTextArea text_name_3 = new RoundedTextArea(1, 1);
			text_name_3.setBounds(442, 308, 129, 47);
			background.add(text_name_3);
			
			RoundedTextArea text_name_4 = new RoundedTextArea(1, 1);
			text_name_4.setBounds(442, 356, 129, 47);
			background.add(text_name_4);
			
			RoundedTextArea text_name_5 = new RoundedTextArea(1, 1);
			text_name_5.setBounds(442, 405, 129, 47);
			background.add(text_name_5);
			
			RoundedTextArea text_name_6 = new RoundedTextArea(1, 1);
			text_name_6.setBounds(442, 454, 129, 47);
			background.add(text_name_6);
			
			
			
			RoundedCheckbox chckbxSelectAll = new RoundedCheckbox("Select All", new Color(0, 153, 51), 0.8f, 4);
			chckbxSelectAll.setHorizontalAlignment(SwingConstants.CENTER);
			chckbxSelectAll.setFont(new Font("Tahoma", Font.BOLD, 11));
			chckbxSelectAll.setBounds(588, 167, 95, 17);
			chckbxSelectAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Handle select all action
					boolean isSelected = chckbxSelectAll.isSelected();
					chckbx_1.setSelected(isSelected);
					chckbx_2.setSelected(isSelected);
					chckbx_3.setSelected(isSelected);
					chckbx_4.setSelected(isSelected);
					chckbx_5.setSelected(isSelected);
					chckbx_6.setSelected(isSelected);

					  

				        Color highlight = isSelected ?  new Color (0, 150, 57): null;
				        imagePanel_1.setBorder(isSelected ? BorderFactory.createLineBorder(highlight, 2) : null);
				        imagePanel_2.setBorder(isSelected ? BorderFactory.createLineBorder(highlight, 2) : null);
				        imagePanel_3.setBorder(isSelected ? BorderFactory.createLineBorder(highlight, 2) : null);
				        imagePanel_4.setBorder(isSelected ? BorderFactory.createLineBorder(highlight, 2) : null);
				        imagePanel_5.setBorder(isSelected ? BorderFactory.createLineBorder(highlight, 2) : null);
				        imagePanel_6.setBorder(isSelected ? BorderFactory.createLineBorder(highlight, 2) : null);
				}
			});
			

			// Hover effect
			chckbxSelectAll.addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseEntered(MouseEvent e) {
			        chckbxSelectAll.setBackground(new Color(0, 153, 51).darker());
			    }

			    @Override
			    public void mouseExited(MouseEvent e) {
			        chckbxSelectAll.setBackground(new Color(0, 153, 51));
			    }
			});

			background.add(chckbxSelectAll);
			
			RoundedButton btnDelete = new RoundedButton("Delete", new Color(220, 20, 60), 0.8f, 11);
			btnDelete.setText("Delete");
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedIdx = -1;
					JCheckBox[] checkboxes = {chckbx_1, chckbx_2, chckbx_3, chckbx_4, chckbx_5, chckbx_6};
					for (int i = 0; i < checkboxes.length; i++) {
						if (checkboxes[i].isSelected()) {
							selectedIdx = i;
							break;
						}
					}
					if (selectedIdx == -1 || itemIds[selectedIdx] == -1) {
						javax.swing.JOptionPane.showMessageDialog(AdminReportItemFoundArchive.this, "Please select an item to delete.");
						return;
					}
					DeleteFoundItem deleteWindow = new DeleteFoundItem(adminId, adminUI, itemIds[selectedIdx], "found");
					deleteWindow.setVisible(true);
					dispose();
				}
			});
			btnDelete.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnDelete.setBounds(588, 189, 95, 17);
			background.add(btnDelete);
			
			chckbx_1 = new JCheckBox("");
			chckbx_1.setHorizontalAlignment(SwingConstants.CENTER);
			chckbx_1.setBounds(613, 218, 46, 36);
			background.add(chckbx_1);
			chckbx_1.setOpaque(false);
			chckbx_1.setBorderPainted(false);
			chckbx_1.setContentAreaFilled(false);
			
			 chckbx_2 = new JCheckBox("");
			chckbx_2.setHorizontalAlignment(SwingConstants.CENTER);
			chckbx_2.setBounds(613, 265, 46, 36);
			chckbx_2.setOpaque(false);
			chckbx_2.setBorderPainted(false);
			chckbx_2.setContentAreaFilled(false);
			background.add(chckbx_2);
			
			 chckbx_3 = new JCheckBox("");
			chckbx_3.setHorizontalAlignment(SwingConstants.CENTER);
			chckbx_3.setBounds(613, 314, 46, 36);
			chckbx_3.setOpaque(false);
			chckbx_3.setBorderPainted(false);
			chckbx_3.setContentAreaFilled(false);
			background.add(chckbx_3);
			
			 chckbx_4 = new JCheckBox("");
			chckbx_4.setHorizontalAlignment(SwingConstants.CENTER);
			chckbx_4.setBounds(613, 362, 46, 36);
			chckbx_4.setOpaque(false);
			chckbx_4.setBorderPainted(false);
			chckbx_4.setContentAreaFilled(false);
			background.add(chckbx_4);
			
			 chckbx_5 = new JCheckBox("");
			chckbx_5.setHorizontalAlignment(SwingConstants.CENTER);
			chckbx_5.setBounds(613, 408, 46, 36);
			chckbx_5.setOpaque(false);
			chckbx_5.setBorderPainted(false);
			chckbx_5.setContentAreaFilled(false);
			background.add(chckbx_5);
			
			 chckbx_6 = new JCheckBox("");
			chckbx_6.setHorizontalAlignment(SwingConstants.CENTER);
			chckbx_6.setBounds(613, 459, 46, 36);
			chckbx_6.setOpaque(false);
			chckbx_6.setBorderPainted(false);
			chckbx_6.setContentAreaFilled(false);
			background.add(chckbx_6);

            itemNameFields[0] = text_1;
            itemNameFields[1] = text_2;
            itemNameFields[2] = text_3;
            itemNameFields[3] = text_4;
            itemNameFields[4] = text_5;
            itemNameFields[5] = text_6;

            dateFields[0] = text_date_1;
            dateFields[1] = text_date_2;
            dateFields[2] = text_date_3;
            dateFields[3] = text_date_4;
            dateFields[4] = text_date_5;
            dateFields[5] = text_date_2_1;

            descFields[0] = text_des_1;
            descFields[1] = text_des_2;
            descFields[2] = text_des_3;
            descFields[3] = text_des_4;
            descFields[4] = text_des_5;
            descFields[5] = text_des_6;

            fullNameFields[0] = text_name_1;
            fullNameFields[1] = text_name_2;
            fullNameFields[2] = text_name_3;
            fullNameFields[3] = text_name_4;
            fullNameFields[4] = text_name_5;
            fullNameFields[5] = text_name_6;

            imagePanels[0] = imagePanel_1;
            imagePanels[1] = imagePanel_2;
            imagePanels[2] = imagePanel_3;
            imagePanels[3] = imagePanel_4;
            imagePanels[4] = imagePanel_5;
            imagePanels[5] = imagePanel_6;

            // Call loadArchivedItems to display archived items on UI load
            loadArchivedItems();
	}
			
			  // Load icon image with scaling
		    private ImageIcon loadIcon(String fileName, int width, int height) {
		        URL resource = getClass().getResource("/kldLostAndFound/images/" + fileName);
		        ImageIcon icon;

		        if (resource == null) {
		            System.err.println("Image not found: " + fileName);
		            icon = new ImageIcon("src/kldLostAndFound/images/" + fileName);
		            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
		                System.err.println("Image could not be loaded from local path either: " + fileName);
		                return null;
		            }
		        } else {
		            icon = new ImageIcon(resource);
		        }

		        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		        return new ImageIcon(scaled);
		    }

		    // Create hover button with icon
		    private JButton createHoverButton(ImageIcon icon, String hoverText) {
		        JButton button_ADMIN = new RoundedIconButton(icon, 7); // radius is 7 here, change it if needed
		        button_ADMIN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		        // Add mouse hover effects
		        button_ADMIN.addMouseListener(new MouseAdapter() {
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                button_ADMIN.setLocation(button_ADMIN.getX(), button_ADMIN.getY() - 2);
		                button_ADMIN.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		                hoverLabel.setText(hoverText);
		                hoverLabel.setFont(new Font("SansSerif", Font.BOLD, 10));

		                int labelWidth = hoverLabel.getPreferredSize().width - 45;
		                int labelHeight = hoverLabel.getPreferredSize().height - 2;
		                hoverLabel.setSize(labelWidth, labelHeight);
		                hoverLabel.setLocation(button_ADMIN.getX(), button_ADMIN.getY() - labelHeight - 3);

		                hoverLabel.setVisible(true);
		            }

		            @Override
		            public void mouseExited(MouseEvent e) {
		                button_ADMIN.setLocation(button_ADMIN.getX(), button_ADMIN.getY() + 2);
		                button_ADMIN.setBorder(null);
		                hoverLabel.setVisible(false);
		            }
		        });

		        return button_ADMIN;
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

    private void loadArchivedItems() {
        for (int i = 0; i < 6; i++) {
            itemIds[i] = -1;
            itemNameFields[i].setText("");
            dateFields[i].setText("");
            descFields[i].setText("");
            fullNameFields[i].setText("");
            imagePanels[i].setImage(null);
        }
        String sql = "SELECT item_id, item_name, full_name, date_found, description FROM reported_found_items WHERE archived = TRUE ORDER BY date_found DESC LIMIT ?, 6";
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, (currentPage - 1) * 6);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                int index = 0;
                while (rs.next() && index < 6) {
                    int itemId = rs.getInt("item_id");
                    itemIds[index] = itemId;
                    itemNameFields[index].setText(rs.getString("item_name"));
                    dateFields[index].setText(rs.getString("date_found"));
                    descFields[index].setText(rs.getString("description"));
                    fullNameFields[index].setText(rs.getString("full_name"));
                    // Load image for this item
                    try (java.sql.PreparedStatement imgStmt = conn.prepareStatement("SELECT image_data FROM reported_found_images WHERE item_id = ? AND archived = TRUE LIMIT 1")) {
                        imgStmt.setInt(1, itemId);
                        try (java.sql.ResultSet imgRs = imgStmt.executeQuery()) {
                            if (imgRs.next()) {
                                byte[] imgBytes = imgRs.getBytes("image_data");
                                if (imgBytes != null) {
                                    javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgBytes);
                                    imagePanels[index].setImage(icon);
                                } else {
                                    imagePanels[index].setImage(null);
                                }
                            } else {
                                imagePanels[index].setImage(null);
                            }
                        }
                    }
                    index++;
                }
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void searchArchivedItems(String searchQuery) {
        for (int i = 0; i < 6; i++) {
            itemIds[i] = -1;
            itemNameFields[i].setText("");
            dateFields[i].setText("");
            descFields[i].setText("");
            fullNameFields[i].setText("");
            imagePanels[i].setImage(null);
        }
        String sql = """
            SELECT item_id, item_name, full_name, date_found, description
            FROM reported_found_items
            WHERE archived = TRUE AND item_name LIKE ?
            ORDER BY date_found DESC
            LIMIT ?, 6
        """;
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setInt(2, 0);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                int index = 0;
                while (rs.next() && index < 6) {
                    itemIds[index] = rs.getInt("item_id");
                    itemNameFields[index].setText(rs.getString("item_name"));
                    dateFields[index].setText(rs.getString("date_found"));
                    descFields[index].setText(rs.getString("description"));
                    fullNameFields[index].setText(rs.getString("full_name"));
                    // If you have images, load them here as well
                    index++;
                }
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

  }
