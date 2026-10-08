package kldLostAndFound;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

public class AdminUploader {

    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://localhost:3306/lostandfound";
        String dbUser = "root";
        String dbPassword = "";

        String fullName = "Super Admin";
        String email = "admin";
        String password = "admin"; // Ideally hashed!
        String imagePath = "C:/Users/Renzo/eclipse-workspace/Database2/src/kldLostAndFound/images/admin1.jpg";
;
        String imageType = "image/jpg";

        new AdminUploader().uploadAdminWithImage(dbUrl, dbUser, dbPassword, fullName, email, password, imagePath, imageType);
    }

    public void uploadAdminWithImage(String dbUrl, String dbUser, String dbPassword,
                                     String fullName, String email, String password,
                                     String imagePath, String imageType) {
        String insertAdminSQL = "INSERT INTO admin (Full_Name, Email, Password) VALUES (?, ?, ?)";
        String insertImageSQL = "INSERT INTO admin_images (Admin_ID, Image_Data, Image_Type) VALUES (?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (
                Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement adminStmt = conn.prepareStatement(insertAdminSQL, Statement.RETURN_GENERATED_KEYS);
                FileInputStream imageInput = new FileInputStream(new File(imagePath))
            ) {
                // Insert admin
                adminStmt.setString(1, fullName);
                adminStmt.setString(2, email);
                adminStmt.setString(3, password);
                adminStmt.executeUpdate();

                // Get generated Admin_ID
                ResultSet generatedKeys = adminStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int adminId = generatedKeys.getInt(1);

                    // Insert image
                    try (PreparedStatement imageStmt = conn.prepareStatement(insertImageSQL)) {
                        imageStmt.setInt(1, adminId);
                        imageStmt.setBinaryStream(2, imageInput);
                        imageStmt.setString(3, imageType);
                        imageStmt.executeUpdate();

                        System.out.println("Admin and image uploaded successfully.");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
