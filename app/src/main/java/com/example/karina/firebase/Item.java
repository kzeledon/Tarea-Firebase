package com.example.karina.firebase;

/**
 * Created by Karina on 24/05/18.
 */

public class Item {

    private String name;
    private String price;
    private String photo;
    private String description;
    private String userId;
    private String id;

    public Item(String name, String price, String photo, String description, String userId, String id) {
        this.setName(name);
        this.setPrice(price);
        this.setPhoto(photo);
        this.setDescription(description);
        this.setUserId(userId);
        this.setId(id);
    }
    public Item(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
