package com.bashalir.go4lunch.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class MarkerGmap  {

    private LatLng position;
    private String idGmap;
    private String idPlace;
    private Boolean selected;

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getIdGmap() {
        return idGmap;
    }

    public void setIdGmap(String idGmap) {
        this.idGmap = idGmap;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }

}
