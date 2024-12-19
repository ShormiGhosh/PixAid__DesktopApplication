package com.example.pixaid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImpactTrackerController implements Initializable {
    @FXML
    private Button Upload_track;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Button donateNow_button;

    @FXML
    private ImageView exit;

    @FXML
    private Button faq_track;

    @FXML
    private Button gallery_track;

    @FXML
    private Button home_track;

    @FXML
    private Button profile_track;

    static class Donation {
        public String date;
        public double amount;
    }

    private List<Donation> donations = new ArrayList<>();
    private final String filePath = "src/main/resources/com/example/pixaid/donationData.json";
    private Gson gson;

    public ImpactTrackerController() {
        gson = new GsonBuilder().create();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String jsonData = readJsonFile(filePath);
        if (jsonData != null && !jsonData.isEmpty()) {
            donations = gson.fromJson(jsonData, new TypeToken<List<Donation>>(){}.getType());
        } else {
            donations = new ArrayList<>();
        }
        updateChart();
        donateNow_button.setOnAction(event -> DBUtils.changescene(event, "gallery.fxml"));
        home_track.setOnAction(event -> DBUtils.changescene(event, "home.fxml"));
        gallery_track.setOnAction(event -> DBUtils.changescene(event, "gallery.fxml"));
        faq_track.setOnAction(event -> DBUtils.changescene(event, "faqq.fxml"));
        profile_track.setOnAction(event -> DBUtils.changescene(event, "profile.fxml"));
        Upload_track.setOnAction(event -> DBUtils.changescene(event, "upload_photo.fxml"));
        exit.setOnMouseClicked(event -> System.exit(0));
    }

    private String readJsonFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    private void writeJsonFile(String filePath, List<Donation> donations) {
        try {
            String jsonData = gson.toJson(donations);
            Files.write(Paths.get(filePath), jsonData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addDonation(double amount) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        boolean found = false;

        // Read existing donations from the file
        String jsonData = readJsonFile(filePath);
        if (jsonData != null && !jsonData.isEmpty()) {
            donations = gson.fromJson(jsonData, new TypeToken<List<Donation>>(){}.getType());
        } else {
            donations = new ArrayList<>();
        }

        // Update the donation amount for today if it exists
        for (Donation donation : donations) {
            if (donation.date.equals(today)) {
                donation.amount += amount;
                found = true;
                break;
            }
        }

        // If no donation exists for today, add a new one
        if (!found) {
            Donation newDonation = new Donation();
            newDonation.date = today;
            newDonation.amount = amount;
            donations.add(newDonation);
        }

        // Write the updated donations list back to the file
        writeJsonFile(filePath, donations);
        updateChart();
    }

    private void updateChart() {
        if (barChart == null) {
            System.err.println("barChart is not initialized.");
            return;
        }

        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Donations");

        Map<String, Double> dailyTotals = new HashMap<>();

        for (Donation donation : donations) {
            dailyTotals.put(donation.date, dailyTotals.getOrDefault(donation.date, 0.0) + donation.amount);
        }

        for (Map.Entry<String, Double> entry : dailyTotals.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
    }
}