// GalleryController.java
package com.example.pixaid;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GalleryController implements Initializable {

    @FXML
    private Button cancel_button;

    @FXML
    private Button dash_button_gal;

    @FXML
    private Button donate_button;

    @FXML
    private ImageView exit;

    @FXML
    private Button faq_button_gal;

    @FXML
    private Button profile_gal;

    @FXML
    private GridPane gallery_gridpane;

    @FXML
    private ScrollPane gallery_scrollpane;

    @FXML
    private Button home_button_gal;

    @FXML
    private TableView<ImageData> table_view;
    @FXML
    private TableColumn<ImageData, String> table_title;
    @FXML
    private TableColumn<ImageData, Double> table_price;
    @FXML
    private TableColumn<ImageData, Double> table_donation;

    @FXML
    private TextField tf_amount;

    @FXML
    private Label total_price;

    @FXML
    private Button upload_button_gal;
    private ObservableList<ImageData> cartList = FXCollections.observableArrayList();
    private double totalPrice = 0.0;
    private ObservableList<ImageData> photoList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBUtils.setGalleryController(this);
        galleryDisplay();
        List<ImageData> photos = DBUtils.getPhotoList();
        photoList.addAll(photos);

        table_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        table_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        table_donation.setCellValueFactory(new PropertyValueFactory<>("donationAmount"));
        table_view.setItems(cartList);

        donate_button.setOnAction(this::donate);
        cancel_button.setOnAction(this::cancel);
        exit.setOnMouseClicked(event -> System.exit(0));
        dash_button_gal.setOnAction(event -> DBUtils.changescene(event, "impactTracker.fxml"));
        faq_button_gal.setOnAction(event -> DBUtils.changescene(event, "faqq.fxml"));
        home_button_gal.setOnAction(event -> DBUtils.changescene(event, "home.fxml"));
        profile_gal.setOnAction(event -> DBUtils.changescene(event, "profile.fxml"));
        upload_button_gal.setOnAction(event -> DBUtils.changescene(event, "upload_photo.fxml"));
    }

    public void addToCart(ImageData imageData) {
        cartList.add(imageData);
        totalPrice += imageData.getPrice();
        total_price.setText(String.format("$%.2f", totalPrice));
        table_view.refresh(); // Refresh the TableView to update the donation column
    }

    public ObservableList<ImageData> getCartList() {
        return cartList;
    }

    public void setCartList(ObservableList<ImageData> cartList) {
        this.cartList = cartList;
        table_view.setItems(cartList);
        totalPrice = cartList.stream().mapToDouble(ImageData::getPrice).sum();
        total_price.setText(String.format("$%.2f", totalPrice));
    }

    @FXML
    private void donate(ActionEvent event) {
        System.out.println("Donate button clicked");

        if (tf_amount.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter the donation amount.");
            alert.show();
            return;
        }

        double enteredAmount;
        try {
            enteredAmount = Double.parseDouble(tf_amount.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid donation amount.");
            alert.show();
            return;
        }

        if (enteredAmount != totalPrice) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The entered amount does not match the total price.");
            alert.show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Donation Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Thank you for your donation.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            double totalDonation = cartList.stream()
                    .mapToDouble(imageData -> {
                        Double donationPercent = imageData.getDonationPercent();
                        return donationPercent != null ? imageData.getPrice() * (donationPercent / 100) : 0.0;
                    })
                    .sum();
            DBUtils.updateUserStatisticsOnPurchase(DBUtils.getLoggedInUsername(), totalDonation);

            ImpactTrackerController impactTracker = new ImpactTrackerController();
            impactTracker.addDonation(totalDonation);

            cartList.clear();
            totalPrice = 0.0;
            total_price.setText("$0.00");
            DBUtils.changescene(event, "gallery.fxml");
        }
    }


    @FXML
    private void cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Donation Cancel Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to cancel the donation?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            cartList.clear();
            totalPrice = 0.0;
            total_price.setText("$0.00");
            DBUtils.changescene(event, "gallery.fxml");
        }
    }

    public ObservableList<ImageData> getPhotoList() {
        return photoList;
    }

    public void galleryDisplay() {
        photoList.clear();
        photoList.addAll(DBUtils.getPhotoList());

        int row = 0;
        int column = 0;
        gallery_gridpane.getChildren().clear();
        gallery_gridpane.getRowConstraints().clear();
        gallery_gridpane.getColumnConstraints().clear();
        for (int i = 0; i < photoList.size(); i++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("image_add_to_gallery.fxml"));
                AnchorPane pane = load.load();
                PhotoAddToGalleryController photoC = load.getController();
                photoC.setData(photoList.get(i));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }
                gallery_gridpane.add(pane, column++, row);
                GridPane.setMargin(pane, new javafx.geometry.Insets(10, 10, 10, 10));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}