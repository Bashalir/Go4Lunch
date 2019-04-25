package com.bashalir.go4lunch.Models;

import android.support.annotation.Nullable;

public class User {


    private String uid;
    private String username;
    @Nullable private String urlPicture;
    @Nullable private String idRestaurant;

    public User(String uid, String username, @Nullable String urlPicture, @Nullable String idRestaurant) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.idRestaurant = idRestaurant;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPhoto) {
        this.urlPicture = urlPhoto;
    }

    @Nullable
    public String getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(@Nullable String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }
}
