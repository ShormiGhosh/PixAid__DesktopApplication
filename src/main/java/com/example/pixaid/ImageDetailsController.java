// ImageDetailsController.java
package com.example.pixaid;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ImageDetailsController implements Initializable {
    @FXML
    private Label Title_of_image;

    @FXML
    private Button addToCart_button;

    @FXML
    private Label category_of_image;

    @FXML
    private Button dash_details;

    @FXML
    private Label description_of_image;

    @FXML
    private ImageView exit;

    @FXML
    private Button faq_details;

    @FXML
    private Button gallery_details;

    @FXML
    private Button home_details;

    @FXML
    private Label percentage_of_donation_on_image;

    @FXML
    private Label photographer_country;

    @FXML
    private Label photographer_email;

    @FXML
    private Label photographer_name;

    @FXML
    private Label price_of_image;

    @FXML
    private Button profile_details;

    @FXML
    private ImageView showPhoto_imageV;

    private ImageData imageData;
    private ObservableList<ImageData> cartList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addToCart_button.setOnAction(this::addToCart);
        home_details.setOnAction(event -> DBUtils.changescene(event, "home.fxml"));
        dash_details.setOnAction(event -> DBUtils.changescene(event, "impactTracker.fxml"));
        profile_details.setOnAction(event -> DBUtils.changescene(event, "profile.fxml"));
        gallery_details.setOnAction(event -> DBUtils.changescene(event, "gallery.fxml"));
        faq_details.setOnAction(event -> DBUtils.changescene(event, "faqq.fxml"));
        exit.setOnMouseClicked(event -> System.exit(0));
    }

    public void loadImageDetails(int photoId) {
        imageData = DBUtils.getImageDetailsById(photoId);
        if (imageData != null) {
            Title_of_image.setText(imageData.getTitle());
            description_of_image.setText(imageData.getDescription());
            photographer_name.setText(imageData.getPhotographerName());
            photographer_email.setText(imageData.getPhotographerEmail());
            photographer_country.setText(imageData.getPhotographerCountry());
            price_of_image.setText(String.valueOf(imageData.getPrice()));
            percentage_of_donation_on_image.setText(String.valueOf(imageData.getDonationPercent()));
            category_of_image.setText(imageData.getCategory());
            showPhoto_imageV.setImage(new Image(imageData.getImagePath()));
        }
    }

    public void setCartList(ObservableList<ImageData> cartList) {
        this.cartList = cartList;
    }

    @FXML
    private void addToCart(ActionEvent event) {
        cartList.add(imageData);
        GalleryController galleryController = DBUtils.getGalleryController();
        galleryController.setCartList(cartList);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gallery.fxml"));
            Parent root = loader.load();
            GalleryController controller = loader.getController();
            controller.setCartList(cartList);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}