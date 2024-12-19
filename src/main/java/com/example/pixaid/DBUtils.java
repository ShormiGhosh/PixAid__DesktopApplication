package com.example.pixaid;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    private static String loggedInUsername;

    public static void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    private static GalleryController galleryController;

    public static void setGalleryController(GalleryController controller) {
        galleryController = controller;
    }

    public static GalleryController getGalleryController() {
        return galleryController;
    }

    public static void changescene(ActionEvent event, String fxmlfile) {
        Parent root = null;
        try {
            root = FXMLLoader.load(DBUtils.class.getResource(fxmlfile));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file: " + fxmlfile);
        }

        if (root != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
    }

    public static void signUp(ActionEvent event, String email, String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUserExistCheck = null;
        PreparedStatement psEmailExistCheck = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");

            psUserExistCheck = connection.prepareStatement("select * from signup where username=?");
            psUserExistCheck.setString(1, username);
            resultSet = psUserExistCheck.executeQuery();
            if (resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists");
                alert.show();
            } else {
                psEmailExistCheck = connection.prepareStatement("select * from signup where email=?");
                psEmailExistCheck.setString(1, email);
                resultSet = psEmailExistCheck.executeQuery();
                if (resultSet.isBeforeFirst()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Email already exists");
                    alert.show();
                } else {
                    psInsert = connection.prepareStatement("insert into signup (email, username, password_hash) values(?,?,?)");
                    psInsert.setString(1, email);
                    psInsert.setString(2, username);
                    psInsert.setString(3, password);
                    psInsert.executeUpdate();
                    setLoggedInUsername(username);
                    FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource("home.fxml"));
                    Parent root = loader.load();
                    HomeController homeController = loader.getController();
                    homeController.setUserInfo(username, null); // No profile picture at sign-up
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.centerOnScreen();
                    stage.show();
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psUserExistCheck != null) {
                try {
                    psUserExistCheck.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psEmailExistCheck != null) {
                try {
                    psEmailExistCheck.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loginUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            preparedStatement = connection.prepareStatement("SELECT password_hash, profile_picture FROM signup WHERE username=?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("User Does Not Exist");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String passwordHash = resultSet.getString("password_hash");
                    String profilePicture = resultSet.getString("profile_picture");
                    if (passwordHash.equals(password)) {
                        DBUtils.setLoggedInUsername(username);
                        FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource("home.fxml"));
                        Parent root = loader.load();
                        HomeController homeController = loader.getController();
                        homeController.setUserInfo(username, profilePicture);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.centerOnScreen();
                        stage.show();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Incorrect Login Information");
                        alert.show();
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updateUserProfile(String email, String username, String country, String phone, String profession) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String query = "UPDATE signup SET email = ?, country = ?, phone_number = ?, profession = ? WHERE username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, country);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, profession);
            preparedStatement.setString(5, username);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Profile updated successfully!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to update profile. Please try again.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteUserAccount(ActionEvent event, String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String query = "DELETE FROM signup WHERE username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Account deleted successfully!");
                alert.show();
                changescene(event, "hello-view.fxml");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to delete account. Please try again.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void UpdatePassword(ActionEvent event, String email, String password) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String query = "UPDATE signup SET password_hash = ? WHERE email = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, password); // Store the password as plain text
            stmt.setString(2, email); // Ensure 'email' contains the correct value

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Password reset successful!");
                alert.show();
                changescene(event, "hello-view.fxml");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to reset password. Please try again.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updateProfilePicture(String username, String filePath) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String query = "UPDATE signup SET profile_picture = ? WHERE username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, filePath);
            preparedStatement.setString(2, username);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Profile picture updated successfully!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to update profile picture. Please try again.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static User getUserData(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        //System.out.println("username: " + username);
        try {
            // System.out.println("username: " + username);
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String query = "SELECT email, username, country, phone_number, profession, profile_picture FROM signup WHERE username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString("email"),
                        resultSet.getString("username"),
                        resultSet.getString("country"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("profession"),
                        resultSet.getString("profile_picture")
                );
            } else {
                System.out.println("No user found with username: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public static void updateImageDetails(String title, String description, String photographerName, String photographerEmail, String photographerCountry, Double price, Double donationPercent, String category, String category2, String imagePath, String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String query = "INSERT INTO photo_details (photo_title, photo_description, photographer_name, photographer_email, photographer_country, price, percentage_of_donate, category, category_2, image, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, photographerName);
            preparedStatement.setString(4, photographerEmail);
            preparedStatement.setString(5, photographerCountry);
            preparedStatement.setDouble(6, price);
            preparedStatement.setDouble(7, donationPercent);
            preparedStatement.setString(8, category);
            preparedStatement.setString(9, category2);
            preparedStatement.setString(10, imagePath);
            preparedStatement.setString(11, username);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Photo uploaded successfully! Thank you for your contribution.");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to upload photo details. Please try again.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<ImageData> getPhotoList() {
        List<ImageData> photoList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            preparedStatement = connection.prepareStatement("SELECT id, photo_title, price,percentage_of_donate, image FROM photo_details");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ImageData imageData = new ImageData(
                        resultSet.getInt("id"),
                        resultSet.getString("photo_title"),
                        resultSet.getDouble("price"),
                        resultSet.getDouble("percentage_of_donate"),
                        resultSet.getString("image")
                );
                photoList.add(imageData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (preparedStatement != null) try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (connection != null) try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return photoList;
    }

    public static ImageData getImageDetailsById(int photoId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ImageData imageData = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String query = "SELECT * FROM photo_details WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, photoId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                imageData = new ImageData(
                        resultSet.getString("photo_title"),
                        resultSet.getString("photo_description"),
                        resultSet.getString("photographer_name"),
                        resultSet.getString("photographer_email"),
                        resultSet.getString("photographer_country"),
                        resultSet.getDouble("price"),
                        resultSet.getDouble("percentage_of_donate"),
                        resultSet.getString("category"),
                        resultSet.getString("category_2"),
                        resultSet.getString("image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (preparedStatement != null) try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (connection != null) try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return imageData;
    }
    private static void ensureUserStatisticsExists(String username) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
        PreparedStatement checkStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;
        try {
            String checkQuery = "SELECT COUNT(*) FROM user_statistics2 WHERE username = ?";
            checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, username);
            resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                String insertQuery = "INSERT INTO user_statistics2 (username) VALUES (?)";
                insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, username);
                insertStatement.executeUpdate();
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (checkStatement != null) checkStatement.close();
            if (insertStatement != null) insertStatement.close();
            if (connection != null) connection.close();
        }
    }

    public static void updateUserStatisticsOnPurchase(String username, double donationAmount) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            ensureUserStatisticsExists(username);
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String updateQuery = "UPDATE user_statistics2 SET total_images_donated = total_images_donated + 1, total_donation_amount = total_donation_amount + ? WHERE username = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDouble(1, donationAmount);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            updateBadge(username);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (connection != null) try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateUserStatisticsOnUpload(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            ensureUserStatisticsExists(username);
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String updateQuery = "UPDATE user_statistics2 SET total_images_uploaded = total_images_uploaded + 1 WHERE username = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (connection != null) try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private static void updateBadge(String username) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String selectQuery = "SELECT total_donation_amount FROM user_statistics2 WHERE username = ?";
            selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, username);
            resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                double totalDonationAmount = resultSet.getDouble("total_donation_amount");
                String badge;
                if (totalDonationAmount >= 10000) {
                    badge = "Gold";
                } else if (totalDonationAmount >= 5000) {
                    badge = "Silver";
                } else if (totalDonationAmount >= 2000) {
                    badge = "Bronze";
                } else {
                    badge = "No Badges";
                }
                String updateQuery = "UPDATE user_statistics2 SET badge = ? WHERE username = ?";
                updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, badge);
                updateStatement.setString(2, username);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (selectStatement != null) selectStatement.close();
                if (updateStatement != null) updateStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getBadgeForUser(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String badge = "No Badge";
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            String selectQuery = "SELECT badge FROM user_statistics2 WHERE username = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                badge = resultSet.getString("badge");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return badge;
    }
    public static double getTotalImages(String username) {
        double totalEarnings = 0.0;
        String query = "SELECT total_images_uploaded FROM user_statistics2 WHERE username = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalEarnings = resultSet.getInt("total_images_uploaded");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalEarnings;
    }

    public static double getTotalDonationAmount(String username) {
        double totalDonationAmount = 0.0;
        String query = "SELECT total_donation_amount FROM user_statistics2 WHERE username = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalDonationAmount = resultSet.getDouble("total_donation_amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalDonationAmount;
    }
}