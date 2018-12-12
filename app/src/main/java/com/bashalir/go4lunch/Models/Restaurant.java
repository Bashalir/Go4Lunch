package com.bashalir.go4lunch.Models;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {


    private String restaurantName;
    private String restaurantAddress;
    private String restaurantPhoto;
    private Integer restaurantStar;


    private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }




}

