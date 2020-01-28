package com.example.letran.equipmentmanagement.models;

public class Image {

    private String id;
    private String name_user;
    private String name_device;
    private String image;

    public Image(String id, String name_user, String name_device, String image) {
        this.id = id;
        this.name_user = name_user;
        this.name_device = name_device;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getName_device() {
        return name_device;
    }

    public void setName_device(String name_device) {
        this.name_device = name_device;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
