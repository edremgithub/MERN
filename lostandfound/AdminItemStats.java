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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;

public class AdminItemStats extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	 private RoundedTextArea text_item_1;
	 private JTextField textField;
	    private RoundedShadowLabel hoverLabel;
	    private RoundedTextField textField_2;
	    private UploadImagePanel imagePanel_1;
		  private UploadImagePanel imagePanel_3;
		  private UploadImagePanel imagePanel_4;
		  private UploadImagePanel imagePanel_5;
		  private UploadImagePanel imagePanel_6;
		  private UploadImagePanel imagePanel_2;
		  private JCheckBox chckbx_1, chckbx_2, chckbx_3, chckbx_4, chckbx_5, chckbx_6;
		  private AdminUI adminUI;
		  private String adminId;
	
	// Add variables for pagination and data management
	private int currentPage = 1;
	private int[] itemIds = new int[6];
	private RoundedTextArea[] nameFields = new RoundedTextArea[6];
	private RoundedTextArea[] dateFields = new RoundedTextArea[6];
	private RoundedTextArea[] daysFields = new RoundedTextArea[6];
	private RoundedTextArea[] statsFields = new RoundedTextArea[6];
	private RoundedTextArea[] fullNameFields = new RoundedTextArea[6];
	private UploadImagePanel[] imagePanels = new UploadImagePanel[6];

	public AdminItemStats( String adminId, AdminUI adminUI) {
		    this.adminId = adminId;
		    this.adminUI = adminUI;
		    setTitle("Admin Item Status Management  - KLD Lost & Found");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setBounds(100, 100, 730, 562);
	        setResizable(false);
	        setLocationRelativeTo(null); // Center the window on the screen
	        
	        // Set app icon		
	        setIconImage(new ImageIcon(getClass().getResource("/kldLostAndFound/images/logo1.jpg")).getImage());
	        
	        // Set background
	        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/AdminItemStats.jpg");
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
	        setPlaceholder(textField, " 🔎 Search Item");

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
	        
	        text_item_1 = new RoundedTextArea(1 , 1);
			text_item_1.setBounds(98, 211, 93, 47);
			background.add(text_item_1);
	        
	        RoundedTextArea text_item_2 = new RoundedTextArea(1, 1);
			text_item_2.setBounds(98, 259, 93, 47);
			background.add(text_item_2);
			
			RoundedTextArea text_item_3 = new RoundedTextArea(1, 1);
			text_item_3.setBounds(98, 308, 93, 47);
			background.add(text_item_3);
			
			RoundedTextArea text_item_4 = new RoundedTextArea(1, 1);
			text_item_4.setBounds(98, 356, 93, 47);
			background.add(text_item_4);
			
			RoundedTextArea text_item_5 = new RoundedTextArea(1, 1);
			text_item_5.setBounds(98, 405, 93, 47);
			background.add(text_item_5);
			
			RoundedTextArea text_item_6 = new RoundedTextArea(1, 1);
			text_item_6.setBounds(98, 454, 93, 47);
			background.add(text_item_6);
			
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
			
			RoundedTextArea text_date_6 = new RoundedTextArea(1, 1);
			text_date_6.setBounds(192, 454, 80, 47);
			background.add(text_date_6);
			
			RoundedTextArea text_days_1 = new RoundedTextArea(1, 1);
			text_days_1.setBounds(273, 211, 79, 47);
			background.add(text_days_1);
			
			RoundedTextArea text_name_1 = new RoundedTextArea(1, 1);
			text_name_1.setBounds(439, 211, 133, 47);
			background.add(text_name_1);
			
			RoundedTextArea text_days_2 = new RoundedTextArea(1, 1);
			text_days_2.setBounds(273, 259, 79, 47);
			background.add(text_days_2);
			
			RoundedTextArea text_days_3 = new RoundedTextArea(1, 1);
			text_days_3.setBounds(273, 308, 79, 47);
			background.add(text_days_3);
			
			RoundedTextArea text_days_4 = new RoundedTextArea(1, 1);
			text_days_4.setBounds(273, 356, 79, 47);
			background.add(text_days_4);
			
			RoundedTextArea text_days_5 = new RoundedTextArea(1, 1);
			text_days_5.setBounds(273, 405, 79, 47);
			background.add(text_days_5);
			
			RoundedTextArea text_days_6 = new RoundedTextArea(1, 1);
			text_days_6.setBounds(273, 454, 79, 47);
			background.add(text_days_6);
			
			RoundedTextArea text_name_2 = new RoundedTextArea(1, 1);
			text_name_2.setBounds(439, 259, 133, 47);
			background.add(text_name_2);
			
			RoundedTextArea text_name_3 = new RoundedTextArea(1, 1);
			text_name_3.setBounds(439, 308, 133, 47);
			background.add(text_name_3);
			
			RoundedTextArea text_name_4 = new RoundedTextArea(1, 1);
			text_name_4.setBounds(439, 356, 133, 47);
			background.add(text_name_4);
			
			RoundedTextArea text_name_5 = new RoundedTextArea(1, 1);
			text_name_5.setBounds(439, 405, 133, 47);
			background.add(text_name_5);
			
			RoundedTextArea text_name_6 = new RoundedTextArea(1, 1);
			text_name_6.setBounds(439, 454, 133, 47);
			background.add(text_name_6);
			
			RoundedTextArea text_stats_1 = new RoundedTextArea(1, 1);
			text_stats_1.setBounds(352, 211, 86, 47);
			background.add(text_stats_1);
			
			RoundedTextArea text_stats_2 = new RoundedTextArea(1, 1);
			text_stats_2.setBounds(352, 259, 86, 47);
			background.add(text_stats_2);
			
			RoundedTextArea text_stats_3 = new RoundedTextArea(1, 1);
			text_stats_3.setBounds(352, 308, 86, 47);
			background.add(text_stats_3);
			
			RoundedTextArea text_stats_4 = new RoundedTextArea(1, 1);
			text_stats_4.setBounds(352, 356, 86, 47);
			background.add(text_stats_4);
			
			RoundedTextArea text_stats_5 = new RoundedTextArea(1, 1);
			text_stats_5.setBounds(352, 405, 86, 47);
			background.add(text_stats_5);
			
			RoundedTextArea text_stats_5_1 = new RoundedTextArea(1, 1);
			text_stats_5_1.setBounds(352, 454, 86, 47);
			background.add(text_stats_5_1);
			
			
			
			RoundedCheckbox chckbxSelectAll = new RoundedCheckbox("Select All", new Color(0, 153, 51), 0.8f, 4);
			chckbxSelectAll.setHorizontalAlignment(SwingConstants.CENTER);
			chckbxSelectAll.setFont(new Font("Tahoma", Font.BOLD, 11));
			chckbxSelectAll.setBounds(588, 155, 95, 17);
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
			
			RoundedButton btnDelete = new RoundedButton("Archive", new Color(220, 20, 60), 0.8f, 11);
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedIdx = -1;
					for (int i = 0; i < itemIds.length; i++) {
						if (chckbx_1.isSelected() && i == 0) selectedIdx = 0;
						if (chckbx_2.isSelected() && i == 1) selectedIdx = 1;
						if (chckbx_3.isSelected() && i == 2) selectedIdx = 2;
						if (chckbx_4.isSelected() && i == 3) selectedIdx = 3;
						if (chckbx_5.isSelected() && i == 4) selectedIdx = 4;
						if (chckbx_6.isSelected() && i == 5) selectedIdx = 5;
					}
					if (selectedIdx == -1 || itemIds[selectedIdx] == -1) {
						JOptionPane.showMessageDialog(AdminItemStats.this, "Please select an item to archive.");
						return;
					}
					try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
						String sqlItem = "UPDATE verified_found_items SET archived = TRUE WHERE item_id = ?";
						java.sql.PreparedStatement psItem = conn.prepareStatement(sqlItem);
						psItem.setInt(1, itemIds[selectedIdx]);
						psItem.executeUpdate();

						String sqlImg = "UPDATE verified_found_images SET archived = TRUE WHERE item_id = ?";
						java.sql.PreparedStatement psImg = conn.prepareStatement(sqlImg);
						psImg.setInt(1, itemIds[selectedIdx]);
						psImg.executeUpdate();

						JOptionPane.showMessageDialog(AdminItemStats.this, "Item archived successfully!");
						// Optionally refresh or redirect
					} catch (java.sql.SQLException ex) {
						JOptionPane.showMessageDialog(AdminItemStats.this, "Error archiving item: " + ex.getMessage());
					}
				}
			});
			btnDelete.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnDelete.setBounds(588, 196, 95, 17);
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

			// Initialize arrays
			imagePanels[0] = imagePanel_1;
			imagePanels[1] = imagePanel_2;
			imagePanels[2] = imagePanel_3;
			imagePanels[3] = imagePanel_4;
			imagePanels[4] = imagePanel_5;
			imagePanels[5] = imagePanel_6;

			// Initialize text fields arrays
			nameFields[0] = text_item_1;
			nameFields[1] = text_item_2;
			nameFields[2] = text_item_3;
			nameFields[3] = text_item_4;
			nameFields[4] = text_item_5;
			nameFields[5] = text_item_6;

			dateFields[0] = text_date_1;
			dateFields[1] = text_date_2;
			dateFields[2] = text_date_3;
			dateFields[3] = text_date_4;
			dateFields[4] = text_date_5;
			dateFields[5] = text_date_6;

			daysFields[0] = text_days_1;
			daysFields[1] = text_days_2;
			daysFields[2] = text_days_3;
			daysFields[3] = text_days_4;
			daysFields[4] = text_days_5;
			daysFields[5] = text_days_6;

			statsFields[0] = text_stats_1;
			statsFields[1] = text_stats_2;
			statsFields[2] = text_stats_3;
			statsFields[3] = text_stats_4;
			statsFields[4] = text_stats_5;
			statsFields[5] = text_stats_5_1;

			fullNameFields[0] = text_name_1;
			fullNameFields[1] = text_name_2;
			fullNameFields[2] = text_name_3;
			fullNameFields[3] = text_name_4;
			fullNameFields[4] = text_name_5;
			fullNameFields[5] = text_name_6;

			// Add action listeners for pagination buttons
			btnLeft.addActionListener(e -> {
				if (currentPage > 1) {
					currentPage--;
					loadVerifiedItems();
				}
			});

			btnRight.addActionListener(e -> {
				currentPage++;
				loadVerifiedItems();
			});

			// Add search functionality
			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					String query = textField.getText().trim();
					if (query.isEmpty()) {
						currentPage = 1;
						loadVerifiedItems();
					} else {
						searchVerifiedItems(query);
					}
				}
			});

			// Initial load of items
			loadVerifiedItems();

			// Set all text areas and text fields to not editable
			text_item_1.setEditable(false);
			text_item_2.setEditable(false);
			text_item_3.setEditable(false);
			text_item_4.setEditable(false);
			text_item_5.setEditable(false);
			text_item_6.setEditable(false);
			text_date_1.setEditable(false);
			text_date_2.setEditable(false);
			text_date_3.setEditable(false);
			text_date_4.setEditable(false);
			text_date_5.setEditable(false);
			text_date_6.setEditable(false);
			text_days_1.setEditable(false);
			text_days_2.setEditable(false);
			text_days_3.setEditable(false);
			text_days_4.setEditable(false);
			text_days_5.setEditable(false);
			text_days_6.setEditable(false);
			text_name_1.setEditable(false);
			text_name_2.setEditable(false);
			text_name_3.setEditable(false);
			text_name_4.setEditable(false);
			text_name_5.setEditable(false);
			text_name_6.setEditable(false);
			text_stats_1.setEditable(false);
			text_stats_2.setEditable(false);
			text_stats_3.setEditable(false);
			text_stats_4.setEditable(false);
			text_stats_5.setEditable(false);
			text_stats_5_1.setEditable(false);
			textField.setEditable(true);

			// Make checkboxes mutually exclusive and update Select All
			JCheckBox[] itemCheckboxes = {chckbx_1, chckbx_2, chckbx_3, chckbx_4, chckbx_5, chckbx_6};

			for (int i = 0; i < itemCheckboxes.length; i++) {
				final int idx = i;
				itemCheckboxes[i].addActionListener(e -> {
					if (itemCheckboxes[idx].isSelected()) {
						// Unselect all others
						for (int j = 0; j < itemCheckboxes.length; j++) {
							if (j != idx) itemCheckboxes[j].setSelected(false);
						}
						// Uncheck Select All if not all are selected
						boolean allSelected = true;
						for (JCheckBox cb : itemCheckboxes) if (!cb.isSelected()) allSelected = false;
						chckbxSelectAll.setSelected(allSelected);
					} else {
						chckbxSelectAll.setSelected(false);
					}
				});
			}

			// Select All logic
			chckbxSelectAll.addActionListener(e -> {
				boolean isSelected = chckbxSelectAll.isSelected();
				for (JCheckBox cb : itemCheckboxes) cb.setSelected(isSelected);
			});
	
	RoundedButton btnView = new RoundedButton("View", new Color(255, 240, 0), 0.8f, 11);
	btnView.setFont(new Font("Tahoma", Font.BOLD, 11));
	btnView.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int selectedIdx = -1;
			for (int i = 0; i < itemCheckboxes.length; i++) {
				if (itemCheckboxes[i].isSelected()) {
					selectedIdx = i;
					break;
				}
			}
			if (selectedIdx == -1 || itemIds[selectedIdx] == -1) {
				JOptionPane.showMessageDialog(AdminItemStats.this, "Please select an item to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
				return;
			}
			openStoredItemStatus(itemIds[selectedIdx]);
		}
	});
	btnView.setBounds(588, 176, 96, 17);
	background.add(btnView);
		}

	private void loadVerifiedItems() {
		// Clear previous data
		for (int i = 0; i < 6; i++) {
			itemIds[i] = -1;
			nameFields[i].setText("");
			dateFields[i].setText("");
			daysFields[i].setText("");
			statsFields[i].setText("");
			fullNameFields[i].setText("");
			imagePanels[i].setImage(null);
		}

		String sql = """
			SELECT vfi.item_id, vfi.item_name, vfi.date_found, vfi.full_name, vfi.location_found, vfi.description, vfimg.image_data, vfi.status
			FROM verified_found_items vfi
			LEFT JOIN verified_found_images vfimg ON vfi.item_id = vfimg.item_id
			WHERE vfi.archived = FALSE
			ORDER BY vfi.date_found DESC
			LIMIT ?, 6
		""";

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "");
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, (currentPage - 1) * 6);
			ResultSet rs = stmt.executeQuery();
			int index = 0;
			while (rs.next() && index < 6) {
				itemIds[index] = rs.getInt("item_id");
				nameFields[index].setText(rs.getString("item_name"));
				dateFields[index].setText(rs.getString("date_found"));
				
				// Calculate days since found
				java.sql.Date foundDate = rs.getDate("date_found");
				if (foundDate != null) {
					long days = (System.currentTimeMillis() - foundDate.getTime()) / (1000 * 60 * 60 * 24);
					daysFields[index].setText(days + " days");
				}

				// Set status based on status column
				String status = rs.getString("status");
				if (status != null) {
					statsFields[index].setText(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
				} else {
					statsFields[index].setText("");
				}

				// Load image if available
				byte[] imgData = rs.getBytes("image_data");
				if (imgData != null) {
					ImageIcon icon = new ImageIcon(imgData);
					Image scaled = icon.getImage().getScaledInstance(imagePanels[index].getWidth(), 
						imagePanels[index].getHeight(), Image.SCALE_SMOOTH);
					imagePanels[index].setImage(new ImageIcon(scaled));
				}

				fullNameFields[index].setText(rs.getString("full_name"));

				index++;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
		}
	}

	private void searchVerifiedItems(String query) {
		// Clear previous data
		for (int i = 0; i < 6; i++) {
			itemIds[i] = -1;
			nameFields[i].setText("");
			dateFields[i].setText("");
			daysFields[i].setText("");
			statsFields[i].setText("");
			fullNameFields[i].setText("");
			imagePanels[i].setImage(null);
		}

		String sql = """
			SELECT vfi.item_id, vfi.item_name, vfi.date_found, vfi.full_name, vfi.location_found, vfi.description, vfimg.image_data, vfi.status
			FROM verified_found_items vfi
			LEFT JOIN verified_found_images vfimg ON vfi.item_id = vfimg.item_id
			WHERE vfi.archived = FALSE AND vfi.item_name LIKE ?
			ORDER BY vfi.date_found DESC
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
				dateFields[index].setText(rs.getString("date_found"));
				
				// Calculate days since found
				java.sql.Date foundDate = rs.getDate("date_found");
				if (foundDate != null) {
					long days = (System.currentTimeMillis() - foundDate.getTime()) / (1000 * 60 * 60 * 24);
					daysFields[index].setText(days + " days");
				}

				// Set status based on status column
				String status = rs.getString("status");
				if (status != null) {
					statsFields[index].setText(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
				} else {
					statsFields[index].setText("");
				}

				// Load image if available
				byte[] imgData = rs.getBytes("image_data");
				if (imgData != null) {
					ImageIcon icon = new ImageIcon(imgData);
					Image scaled = icon.getImage().getScaledInstance(imagePanels[index].getWidth(), 
						imagePanels[index].getHeight(), Image.SCALE_SMOOTH);
					imagePanels[index].setImage(new ImageIcon(scaled));
				}

				fullNameFields[index].setText(rs.getString("full_name"));

				index++;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
		}
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

// Add this helper method to fetch item details by item_id
private void openStoredItemStatus(int itemId) {
    try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/lostandfound", "root", "")) {
        String sql = "SELECT vfi.item_name, vfi.full_name, vfi.course, vfi.location_found, vfi.contact_number, vfi.date_found, vfi.description, vfimg.image_data FROM verified_found_items vfi LEFT JOIN verified_found_images vfimg ON vfi.item_id = vfimg.item_id WHERE vfi.item_id = ?";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String itemName = rs.getString("item_name");
                String fullName = rs.getString("full_name");
                String course = rs.getString("course");
                String location = rs.getString("location_found");
                String number = rs.getString("contact_number");
                String dateFound = rs.getString("date_found");
                String description = rs.getString("description");
                byte[] imageData = rs.getBytes("image_data");
                javax.swing.ImageIcon imageIcon = null;
                if (imageData != null) {
                    imageIcon = new javax.swing.ImageIcon(imageData);
                }
                AdminStoredItemStatus win = new AdminStoredItemStatus(adminId, adminUI, itemName, fullName, course, location, number, dateFound, description, imageIcon);
                win.setVisible(true);
                this.dispose();
                return;
            }
        }
    } catch (Exception ex) {
        javax.swing.JOptionPane.showMessageDialog(this, "Failed to load item details: " + ex.getMessage());
    }
}
}

