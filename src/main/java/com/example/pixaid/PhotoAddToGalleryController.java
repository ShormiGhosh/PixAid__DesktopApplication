// PhotoAddToGalleryController.java
package com.example.pixaid;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PhotoAddToGalleryController implements Initializable {

    @FXML
    private Button addToCart_button;

    @FXML
    private AnchorPane gallery_show;

    @FXML
    private ImageView image_view;

    @FXML
    private Label label_price;

    @FXML
    private Label label_title;

    @FXML
    private Button showDetails_button;

    private ImageData imageData;
    private Image image;

    public void setData(ImageData imageData) {
        this.imageData = imageData;
        image = new Image(imageData.getImagePath(), 230, 202, false, true);
        image_view.setImage(image);
        label_title.setText(imageData.getTitle());
        label_price.setText('$' + String.valueOf(imageData.getPrice()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addToCart_button.setOnAction(this::addToCart);
        showDetails_button.setOnAction(this::showDetails);
    }

    @FXML
    private void showDetails(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Image_details.fxml"));
            Parent root = loader.load();

            ImageDetailsController controller = loader.getController();
            controller.loadImageDetails(imageData.getId());
            controller.setCartList(DBUtils.getGalleryController().getCartList());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addToCart(ActionEvent event) {
        GalleryController galleryController = DBUtils.getGalleryController();
        imageData.getDonationAmount();
        galleryController.addToCart(imageData);
    }
}