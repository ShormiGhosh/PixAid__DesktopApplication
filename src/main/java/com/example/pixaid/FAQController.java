package com.example.pixaid;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FAQController implements Initializable {

    @FXML
    private Button dash_button_faq;

    @FXML
    private ImageView exit;

    @FXML
    private Button gallery_button_faq;

    @FXML
    private Button home_button_faq;

    @FXML
    private Button profile_button_faq;

    @FXML
    private Button upload_button_faq;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private TextArea answerArea;

    private List<FAQItem> faqItems;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exit.setOnMouseClicked(event -> {
            System.exit(0); // Close the application
        });
        dash_button_faq.setOnAction(event -> DBUtils.changescene(event, "impactTracker.fxml"));
        upload_button_faq.setOnAction(event -> DBUtils.changescene(event, "upload_photo.fxml"));
        gallery_button_faq.setOnAction(event -> DBUtils.changescene(event, "gallery.fxml"));
        profile_button_faq.setOnAction(event -> DBUtils.changescene(event, "profile.fxml"));
        home_button_faq.setOnAction(event -> DBUtils.changescene(event, "home.fxml"));

        loadFAQData("https://api.myjson.online/v1/records/5c3bd97a-116f-446a-b8c3-f676cf8c1216");

        searchButton.setOnAction(event -> searchFAQ());
    }

    private void loadFAQData(String jsonUrl) {
        try {
            URL url = new URL(jsonUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray faqsArray = jsonObject.getAsJsonArray("data");

                faqItems = new ArrayList<>();
                for (int i = 0; i < faqsArray.size(); i++) {
                    JsonObject faqObject = faqsArray.get(i).getAsJsonObject();
                    String question = faqObject.get("question").getAsString();
                    String answer = faqObject.get("answer").getAsString();
                    faqItems.add(new FAQItem(question, answer));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchFAQ() {
        String query = searchField.getText().trim().toLowerCase();
        for (FAQItem item : faqItems) {
            if (item.getQuestion().toLowerCase().contains(query)) {
                answerArea.setText(item.getAnswer());
                return;
            }
        }
        answerArea.setText("No answers found.");
        answerArea.setEditable(false);
    }
}