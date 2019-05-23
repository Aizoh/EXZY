package com.example.app001.Model;

/**
 * This class is used to hold a panic event for the recylerview
 */
public class PanicEvent {

    int image;
    String caption, Eventtype;

    public PanicEvent(int image, String caption) {
        this.image = image;
        this.caption = caption;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getEventtype() {
        return Eventtype;
    }

    public void setEventtype(String eventtype) {
        Eventtype = eventtype;
    }
}
