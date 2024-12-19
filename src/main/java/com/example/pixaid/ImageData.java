// ImageData.java
package com.example.pixaid;

public class ImageData {
    private int id;
    private String title;
    private String description;
    private String photographerName;
    private String photographerEmail;
    private String photographerCountry;
    private Double price;
    private Double donationPercent;
    private Double donationAmount;
    private String category;
    private String category2;
    private String imagePath;

    public ImageData(String title, String description, String photographerName, String photographerEmail, String photographerCountry, Double price, Double donationPercent, String category, String category2, String imagePath) {
        this.title = title;
        this.description = description;
        this.photographerName = photographerName;
        this.photographerEmail = photographerEmail;
        this.photographerCountry = photographerCountry;
        this.price = price;
        this.donationPercent = donationPercent != null ? donationPercent : 0.0;
        this.donationAmount = price * (this.donationPercent / 100);
        this.category = category;
        this.category2 = category2;
        this.imagePath = imagePath;
    }

    public ImageData(int id, String title, Double price,Double donationPercent, String imagePath) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.donationPercent = donationPercent;
        this.donationAmount = price * (this.donationPercent / 100);
        this.imagePath = imagePath;
    }

    // Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotographerName() {
        return photographerName;
    }

    public void setPhotographerName(String photographerName) {
        this.photographerName = photographerName;
    }

    public String getPhotographerEmail() {
        return photographerEmail;
    }

    public void setPhotographerEmail(String photographerEmail) {
        this.photographerEmail = photographerEmail;
    }

    public String getPhotographerCountry() {
        return photographerCountry;
    }

    public void setPhotographerCountry(String photographerCountry) {
        this.photographerCountry = photographerCountry;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDonationPercent() {
        return donationPercent;
    }

    public void setDonationPercent(Double donationPercent) {
        this.donationPercent = donationPercent;
    }

    public double getDonationAmount() {
        return donationPercent != null ? price * (donationPercent / 100) : 0.0;
    }

    public void setDonationAmount(Double donationAmount) {
        this.donationAmount = donationAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}