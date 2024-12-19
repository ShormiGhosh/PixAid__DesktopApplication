package com.example.pixaid;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button Home_profile_btn;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private Label UserName;

    @FXML
    private ImageView exit;

    @FXML
    private Button homeDashboard_btn;

    @FXML
    private Button homeUploadPhoto_btn;

    @FXML
    private Button home_gallery_btn;

    @FXML
    private Button home_faq_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private ImageView profileImageView;

    @FXML
    private AnchorPane slider;

    @FXML
    private WebView webview;

    @FXML
    private Label welcome_label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            // Pause for 2 seconds before transitioning
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.play();
            // Ensure the slider is initially hidden off-screen
            slider.setTranslateX(-237);

            // Sidebar show logic (Menu button)
            Menu.setOnMouseClicked(event -> {
                TranslateTransition showTransition = new TranslateTransition(Duration.seconds(0.4), slider);
                showTransition.setToX(0); // Move slider into view
                showTransition.play();

                showTransition.setOnFinished(e -> {
                    Menu.setVisible(false); // Hide Menu
                    MenuBack.setVisible(true); // Show MenuBack
                });
            });

            // Sidebar hide logic (MenuBack button)
            MenuBack.setOnMouseClicked(event -> {
                TranslateTransition hideTransition = new TranslateTransition(Duration.seconds(0.4), slider);
                hideTransition.setToX(-237); // Move slider out of view
                hideTransition.play();

                hideTransition.setOnFinished(e -> {
                    MenuBack.setVisible(false); // Hide MenuBack
                    Menu.setVisible(true); // Show Menu
                });
            });
            String username = DBUtils.getLoggedInUsername();
            UserName.setText(username);
            // Load profile picture if needed
            User user = DBUtils.getUserData(username);
            if (user != null && user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                profileImageView.setImage(new Image(user.getProfilePicture()));
            }
            // Exit button functionality
            exit.setOnMouseClicked(event -> {
                System.exit(0); // Close the application
            });

            Home_profile_btn.setOnAction(event -> DBUtils.changescene(event, "profile.fxml"));
            home_gallery_btn.setOnAction(event -> DBUtils.changescene(event, "gallery.fxml"));

            homeUploadPhoto_btn.setOnAction(event -> DBUtils.changescene(event, "upload_photo.fxml"));
            homeDashboard_btn.setOnAction(event -> DBUtils.changescene(event, "impactTracker.fxml"));
            home_faq_btn.setOnAction(event -> DBUtils.changescene(event, "faqq.fxml"));
            logout_btn.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Logout Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to logout?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    DBUtils.changescene(event, "hello-view.fxml");
                }
            });
        if (webview != null) {
            WebEngine webEngine = webview.getEngine();
            URL resource = getClass().getResource("image_slider.html");
            if (resource != null) {
                webEngine.load(resource.toExternalForm());
            } else {
                System.err.println("Error: image_slider.html not found.");
            }

        }
    }

    public void setUserInfo(String username, String profilePictureUrl) {
        UserName.setText(username);
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            profileImageView.setImage(new Image(profilePictureUrl));
        }
    }
}