package kldLostAndFound;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class RecentlyClaimed extends JFrame {

    private static final long serialVersionUID = 1L;
    private RoundedShadowLabel hoverLabel;
    private String idNumber;
    private MyAccount myAccount;

  
    public RecentlyClaimed(String idNumber, MyAccount myAccount) {
    	  this.idNumber = idNumber;
          this.myAccount = myAccount;
          
        setTitle("Recently Claimed Items - KLD Lost & Found");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 730, 562);
        setResizable(false);
        setLocationRelativeTo(null);

        // Set app icon
        ImageIcon logoIcon = loadIcon("logo1.jpg", 32, 32);
        if (logoIcon != null) {
            setIconImage(logoIcon.getImage());
        }

        // Set background
        BackgroundPanel background = new BackgroundPanel("src/kldLostAndFound/images/RecentlyClaimedbg.jpg");
        background.setLayout(null); // Set the layout to null to manually position components
        setContentPane(background);

        // Initialize hover shadow label
        hoverLabel = new RoundedShadowLabel();
        hoverLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        hoverLabel.setForeground(Color.WHITE);
        hoverLabel.setVisible(false);
        background.add(hoverLabel);
        background.setComponentZOrder(hoverLabel, 0); // Ensure it's on top

        // Upload Image Panels
        int panelWidth = 46, panelHeight = 40, startX = 52, startY = 173, gap = 5;
        for (int i = 0; i < 7; i++) {
            UploadImagePanel uploadBox = new UploadImagePanel(false);
            uploadBox.setBounds(startX, startY + i * (panelHeight + gap), panelWidth, panelHeight);
            uploadBox.setVisible(true); // Ensure it's visible
            background.add(uploadBox);
        }

        // Appeal buttons and text fields
        for (int i = 0; i < 7; i++) {
            int y = 182 + i * 45;

            TransparentTextField field1 = new TransparentTextField(0.5f);
            field1.setEditable(false);
            field1.setBounds(125, y, 182, 20);
            background.add(field1);

            TransparentTextField field2 = new TransparentTextField(0.5f);
            field2.setEditable(false);
            field2.setBounds(329, y, 182, 20);
            background.add(field2);

            RoundedButton btnAppeal = new RoundedButton("Appeal", new Color(220, 20, 60), 0.8f, 11);
            btnAppeal.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnAppeal.setBounds(569, y - 4, 83, 28);
            background.add(btnAppeal);

            // Add ActionListener for each Appeal Button
            final int buttonIndex = i; // Capture the value of i for each button
            btnAppeal.addActionListener(e -> handleAppealButtonClick(buttonIndex));
            
            RoundedButton btnSearch = new RoundedButton("Back",new Color(0, 128, 55), 0.8f, 11);
            btnSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnSearch.addActionListener(new ActionListener() {
            	@Override
    			public void actionPerformed(ActionEvent e) {
            		SearchItem searchItem = new SearchItem(idNumber, myAccount);
            		searchItem.setVisible(true); // Open the Search Item window
            			dispose();
            	}
            });
            btnSearch.setBounds(597, 25, 80, 34);
            background.add(btnSearch);
        }
    }

    /**
     * This method is called when any "Appeal" button is clicked.
     * It shows a confirmation message with a warning icon.
     * @param buttonIndex The index of the button that was clicked (0-6).
     */
    private void handleAppealButtonClick(int buttonIndex) {
        int option = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to appeal for this item?", // Message in the dialog
            "Warning", // Title of the dialog
            JOptionPane.YES_NO_OPTION, // Buttons for "Yes" and "No"
            JOptionPane.WARNING_MESSAGE // Warning icon
        );

        // Check which option was selected
        if (option == JOptionPane.YES_OPTION) {
            // User clicked "Yes"
          FalseClaim falseClaim = new FalseClaim(idNumber, myAccount);
			falseClaim.setVisible(true); // Open the False Claim window
			dispose(); // Close the current window 
        } 
    }

    // Helper method to load the image
    private ImageIcon loadIcon(String fileName, int width, int height) {
        URL resource = getClass().getResource("/kldLostAndFound/images/" + fileName);
        ImageIcon icon;

        if (resource == null) {
            System.err.println("Image not found in classpath: " + fileName + " — trying local src path...");
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
}
