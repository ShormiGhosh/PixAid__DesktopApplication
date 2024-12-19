package com.example.pixaid;

public class User {
    private String email;
    private String username;
    private String country;
    private String phone;
    private String profession;
    private String profilePicture;

    public User(String email, String username, String country, String phone, String profession, String profilePicture) {
        this.email = email;
        this.username = username;
        this.country = country;
        this.phone = phone;
        this.profession = profession;
        this.profilePicture = profilePicture;
    }

    // Getters and setters for each field
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}