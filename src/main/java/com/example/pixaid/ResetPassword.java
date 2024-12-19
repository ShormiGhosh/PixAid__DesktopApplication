package com.example.pixaid;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ResetPassword implements Initializable {
    @FXML
    private Button LOGIN_BUTTON;

    @FXML
    private Button ResetPass;

    @FXML
    private ImageView exit;

    @FXML
    private TextField pf_confirm;

    @FXML
    private PasswordField pf_newPass;
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOGIN_BUTTON.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changescene(event, "hello-view.fxml");
            }
        });
        ResetPass.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pf_confirm.getText().isEmpty() || pf_newPass.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter both new password fields!");
                    alert.show();
                    return;
                }

                if (!pf_newPass.getText().equals(pf_confirm.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Passwords do not match!");
                    alert.show();
                    return;
                }

                if (pf_newPass.getText().length() < 6) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Password must be at least 6 characters long!");
                    alert.show();
                    return;
                }
                if (!pf_newPass.getText().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Password must contain at least one special character!");
                    alert.show();
                    return;
                }

                DBUtils.UpdatePassword(event, email, pf_confirm.getText());
            }
        });
    }

    @FXML
    public void handleExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}