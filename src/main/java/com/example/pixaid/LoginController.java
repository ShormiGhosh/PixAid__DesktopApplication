package com.example.pixaid;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button loginBtn;
    @FXML
    private ImageView exit;
    @FXML
    private Button signupBtn;
    @FXML
    private Button forgotpassBtn;
    @FXML
    private CheckBox showPasswordCheckBox;
    @FXML
    private TextField tf_Showpassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tf_Showpassword.setVisible(false);
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tfUsername.getText().isEmpty()|| pfPassword.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all the fields");
                    alert.show();
                }
                else{
                    DBUtils.loginUser(event, tfUsername.getText(), pfPassword.getText());
                }
            }
        });
        signupBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changescene(event,"Signup.fxml");
            }
        });
        forgotpassBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changescene(event, "forgetPassword.fxml");
            }
        });
    }
    @FXML
    public void handleExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }
    public void showPassword(){
        if(showPasswordCheckBox.isSelected()){
            tf_Showpassword.setText(pfPassword.getText());
            tf_Showpassword.setVisible(true);
            pfPassword.setVisible(false);
        }else{
            pfPassword.setText(tf_Showpassword.getText());
            tf_Showpassword.setVisible(false);
            pfPassword.setVisible(true);
        }
    }
}
