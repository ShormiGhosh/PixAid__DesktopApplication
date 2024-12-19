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
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController implements Initializable {
    @FXML
    private ImageView exit;

    @FXML
    private Button login_button;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tf_Username;

    @FXML
    private PasswordField tf_confirmPassword;

    @FXML
    private PasswordField tf_password;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_Username.getText().trim().isEmpty() && !tfEmail.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty() && !tf_confirmPassword.getText().trim().isEmpty()) {
                    if (!tf_confirmPassword.getText().equals(tf_password.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Passwords do not match");
                        alert.show();
                    } else if (tf_password.getText().length() < 6) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Password must be at least 6 characters long!");
                        alert.show();
                    } else if (!tf_password.getText().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Password must contain at least one special character!");
                        alert.show();
                    } else if (!isValidEmail(tfEmail.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid email format");
                        alert.show();
                    } else {
                        DBUtils.signUp(event, tfEmail.getText(), tf_Username.getText(), tf_password.getText());
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all the fields");
                    alert.show();
                }
            }
        });
        login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changescene(event,"hello-view.fxml");
            }
        });
    }
    public boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @FXML
    public void handleExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
