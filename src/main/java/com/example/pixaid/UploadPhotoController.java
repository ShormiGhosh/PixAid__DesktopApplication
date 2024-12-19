package com.example.pixaid;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UploadPhotoController implements Initializable {
    @FXML
    private TextField ImagePrice;

    @FXML
    private ChoiceBox<String> Image_category_choice;

    @FXML
    private TextField Image_category_op;

    @FXML
    private Button profile_button_up;

    @FXML
    private Button dash_button_up;

    @FXML
    private TextField donationPercent;

    @FXML
    private ImageView exit;

    @FXML
    private Button faq_button_up;

    @FXML
    private Button gallery_button_up;

    @FXML
    private Button home_button_up;

    @FXML
    private TextField tf_imageDescription;

    @FXML
    private TextField tf_imageTitle;

    @FXML
    private TextField tf_photographerCountry;

    @FXML
    private TextField tf_photographerEmail;

    @FXML
    private TextField tf_photographerName;

    @FXML
    private ImageView uploadPhoto_imageV;

    @FXML
    private Button UploadImage_button;

    @FXML
    private Button Submit_button;

    private String[] imageCategory={"street_photography","sky","flower","scenery","portrait","rural","bird","childhood"};
    private String filePath;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image_category_choice.getItems().addAll(imageCategory);
        Image_category_choice.setOnAction(this::getCategory);
        UploadImage_button.setOnAction(event -> uploadProfilePicture());
        Submit_button.setOnAction(event -> submit());
        home_button_up.setOnAction(event -> DBUtils.changescene(event,"home.fxml"));
        dash_button_up.setOnAction(event -> DBUtils.changescene(event, "impactTracker.fxml"));
        profile_button_up.setOnAction(event -> DBUtils.changescene(event,"profile.fxml"));
        gallery_button_up.setOnAction(event -> DBUtils.changescene(event,"gallery.fxml"));
        faq_button_up.setOnAction(event -> DBUtils.changescene(event,"faqq.fxml"));
        exit.setOnMouseClicked(event -> {
            System.exit(0); // Close the application
        });
    }

    public void getCategory(ActionEvent actionEvent) {
        String category=Image_category_choice.getValue();
    }

    public void cleatTextFields() {
        tf_imageTitle.clear();
        tf_imageDescription.clear();
        tf_photographerName.clear();
        tf_photographerEmail.clear();
        tf_photographerCountry.clear();
        ImagePrice.clear();
        donationPercent.clear();
        Image_category_choice.getSelectionModel().clearSelection();
        Image_category_op.clear();
        uploadPhoto_imageV.setImage(null);
    }
    private void uploadProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            filePath = selectedFile.toURI().toString(); // Correctly set the filePath variable
            uploadPhoto_imageV.setImage(new Image(filePath));
        }
    }
    public void submit() {
        String loggedInUsername = DBUtils.getLoggedInUsername();
        if (filePath == null
                || tf_imageTitle.getText().isEmpty()
                || tf_imageDescription.getText().isEmpty()
                || tf_photographerName.getText().isEmpty()
                || tf_photographerEmail.getText().isEmpty()
                || tf_photographerCountry.getText().isEmpty()
                || ImagePrice.getText().isEmpty()
                || donationPercent.getText().isEmpty()
                || Image_category_choice.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        } else {
            try {
                Double price = Double.parseDouble(ImagePrice.getText());
                Double donation = Double.parseDouble(donationPercent.getText());

                DBUtils.updateImageDetails(tf_imageTitle.getText(), tf_imageDescription.getText(), tf_photographerName.getText(), tf_photographerEmail.getText(), tf_photographerCountry.getText(), price, donation, Image_category_choice.getSelectionModel().getSelectedItem(), Image_category_op.getText(), filePath, loggedInUsername);
                DBUtils.updateUserStatisticsOnUpload(loggedInUsername);

                cleatTextFields();
                GalleryController galleryController = new GalleryController();
                galleryController.galleryDisplay();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter valid numbers for price and donation percentage");
                alert.showAndWait();
            }
        }
    }
}
