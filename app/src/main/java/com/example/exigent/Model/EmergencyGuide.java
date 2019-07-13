package com.example.exigent.Model;
/**
 * This class is used to hold a Emergency guide information for the EmergencyGuide recylerview
 */

public class EmergencyGuide {

    int image;
    String title,description;

    public EmergencyGuide() {
    }

    public EmergencyGuide(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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



}
