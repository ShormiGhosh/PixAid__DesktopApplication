package com.example.pixaid;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class ForgotPassword implements Initializable {
    @FXML
    private ImageView exit;

    @FXML
    private Button login_btn;

    @FXML
    private Button otpSend_button;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tf_code;

    @FXML
    private Button verify_button;

    public int verificationCode;

    public void handleSend(ActionEvent actionEvent) {
        String emailInput = tfEmail.getText().trim();

        if (emailInput.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter your email address!");
            alert.show();
            return;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/login_schema", "root", "@forcode5419");
            preparedStatement = connection.prepareStatement("select * from signup where email=?");
            preparedStatement.setString(1, tfEmail.getText());
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No Account With This Email");
                alert.show();
            } else {
                // Generate verification code
                verificationCode = generateVerificationCode();

                // Send email with verification code
                sendEmail(emailInput, verificationCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String recipientEmail, int verificationCode) {
        final String username = "quanta5419@gmail.com";
        final String password = "ljra mvzn eovg bgfc";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("quanta5419@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Verification Code");
            message.setText("Your verification code is: " + verificationCode);

            Transport.send(message);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Verification code sent to your email.");
            alert.show();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleVerify(ActionEvent actionEvent) {
        String enteredCode = tf_code.getText().trim();

        if (enteredCode.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter the verification code!");
            alert.show();
            return;
        }

        try {
            int enteredCodeInt = Integer.parseInt(enteredCode);

            if (enteredCodeInt == verificationCode) {
                // Proceed to the reset password page
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("reset_password.fxml"));
                    Parent root = loader.load();

                    // Get the controller for the reset password page
                    ResetPassword resetPasswordController = loader.getController();
                    resetPasswordController.setEmail(this.tfEmail.getText()); // Passing the email to the reset password page
                    Stage stage = (Stage) otpSend_button.getScene().getWindow();
                    Scene resetPasswordScene = new Scene(root);
                    stage.setScene(resetPasswordScene);
                    stage.centerOnScreen();
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Incorrect Verification Code");
                alert.show();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private Random random = new Random();  // Declare Random once
    public int generateVerificationCode() {
        int code = random.nextInt(900000) + 100000; // 6-digit code
        return code;
    }

    @FXML
    public void handleExitClick(MouseEvent mouseEvent) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changescene(event, "hello-view.fxml");
            }
        });
    }
}