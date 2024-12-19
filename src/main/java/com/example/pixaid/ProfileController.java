package com.example.pixaid;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Button dash_button_prof;

    @FXML
    private AreaChart<String, Number> AreaChart_imageTotal;

    @FXML
    private AreaChart<String, Number> areaChart_donation;

    @FXML
    private Button deleteAccount_btn;

    @FXML
    private Button editProfile_btn;

    @FXML
    private ImageView exit;

    @FXML
    private Button gallery_button_prof;

    @FXML
    private Button home_button_prof;

    @FXML
    private Button faq_button_prof;

    @FXML
    private ImageView profile_Picture;

    @FXML
    private TextField tf_Email;

    @FXML
    private TextField tf_Uername;

    @FXML
    private TextField tf_country;

    @FXML
    private TextField tf_phone;

    @FXML
    private TextField tf_profession;

    @FXML
    private Button uploadPhoto_btn;

    @FXML
    private Button upload_button_prof;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label badge_label;

    private boolean isEditing = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setEditableFields(false);
        uploadPhoto_btn.setVisible(false);

        // Load user data
        loadUserData();
        updateBadgeLabel();
        loadChartData();

        editProfile_btn.setOnAction(event -> {
            if (isEditing) {
                saveProfileChanges();
                setEditableFields(false);
                uploadPhoto_btn.setVisible(false);
                editProfile_btn.setText("Edit Profile");
            } else {
                setEditableFields(true);
                uploadPhoto_btn.setVisible(true);
                editProfile_btn.setText("Save Changes");
            }
            isEditing = !isEditing;
        });

        exit.setOnMouseClicked(event -> {
            System.exit(0); // Close the application
        });

        deleteAccount_btn.setOnAction(event -> deleteAccount(event));
        uploadPhoto_btn.setOnAction(event -> uploadProfilePicture());
        upload_button_prof.setOnAction(event -> DBUtils.changescene(event, "upload_photo.fxml"));
        home_button_prof.setOnAction(event -> DBUtils.changescene(event, "home.fxml"));
        gallery_button_prof.setOnAction(event -> DBUtils.changescene(event, "gallery.fxml"));
        faq_button_prof.setOnAction(event -> DBUtils.changescene(event, "faqq.fxml"));
        dash_button_prof.setOnAction(event -> DBUtils.changescene(event, "impactTracker.fxml"));
    }

    private void setEditableFields(boolean editable) {
        tf_Email.setEditable(editable);
        tf_Uername.setEditable(editable);
        tf_country.setEditable(editable);
        tf_phone.setEditable(editable);
        tf_profession.setEditable(editable);
    }

    private void loadUserData() {
        String username = DBUtils.getLoggedInUsername();
        usernameLabel.setText(username);
        User user = DBUtils.getUserData(username);
        System.out.println(user);
        if (user != null) {
            tf_Email.setText(user.getEmail());
            tf_Uername.setText(user.getUsername());
            tf_country.setText(user.getCountry());
            tf_phone.setText(user.getPhone());
            tf_profession.setText(user.getProfession());
            if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                profile_Picture.setImage(new Image(user.getProfilePicture()));
            }
        }
    }

    private void saveProfileChanges() {
        String email = tf_Email.getText();
        String username = tf_Uername.getText();
        String country = tf_country.getText();
        String phone = tf_phone.getText();
        String profession = tf_profession.getText();
        usernameLabel.setText(username);
        DBUtils.updateUserProfile(email, username, country, phone, profession);
    }

    private void deleteAccount(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Account Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete your account?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DBUtils.deleteUserAccount(event, tf_Uername.getText());
        }
    }

    private void uploadProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String filePath = selectedFile.toURI().toString();
            profile_Picture.setImage(new Image(filePath));
            DBUtils.updateProfilePicture(tf_Uername.getText(), filePath);
        }
    }

    private void updateBadgeLabel() {
        String badge = DBUtils.getBadgeForUser(DBUtils.getLoggedInUsername());
        badge_label.setText(badge);
    }

    private void loadChartData() {
        String username = DBUtils.getLoggedInUsername();
        User user = DBUtils.getUserData(username);

        if (user != null) {
            // Load total earnings data into AreaChart_income
            XYChart.Series<String, Number> earningsSeries = new XYChart.Series<>();
            earningsSeries.setName("Total Images Uploaded");
            earningsSeries.getData().add(new XYChart.Data<>("Images", DBUtils.getTotalImages(username)));
            AreaChart_imageTotal.getData().add(earningsSeries);

            // Load total donation amount data into areaChart_donation
            XYChart.Series<String, Number> donationSeries = new XYChart.Series<>();
            donationSeries.setName("Total Donations");
            donationSeries.getData().add(new XYChart.Data<>("Donations", DBUtils.getTotalDonationAmount(username)));
            areaChart_donation.getData().add(donationSeries);
        }
    }
}