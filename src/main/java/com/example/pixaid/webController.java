package com.example.pixaid;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class webController implements Initializable {
    @FXML
    private WebView webView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (webView != null) {
            WebEngine webEngine = webView.getEngine();

            // Load the HTML file
            URL resource = getClass().getResource("welcome.html");
            if (resource != null) {
                webEngine.load(resource.toExternalForm());
            } else {
                System.err.println("Error: welcome.html not found.");
            }
            // Pause for 5 seconds before transitioning
            PauseTransition delay = new PauseTransition(Duration.seconds(5.5));//5 silo, 10 lekhsi
            delay.setOnFinished(event -> showLoginPage());
            delay.play();
        }
    }
    private void showLoginPage() {
        try {
            // Load the login screen
            Stage stage1 = (Stage) webView.getScene().getWindow();
            Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
            stage1.setScene(new Scene(loginRoot));
            stage1.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
