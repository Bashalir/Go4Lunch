package com.bashalir.go4lunch.Models;

import java.util.List;

public class ListRestaurant {

    private int size;
    private List<Restaurant> restaurant;

    public List<Restaurant> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(List<Restaurant> restaurant) {
        this.restaurant = restaurant;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
