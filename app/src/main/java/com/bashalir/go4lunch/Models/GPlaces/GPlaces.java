package com.bashalir.go4lunch.Models.GPlaces;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GPlaces {

    @SerializedName("result")
    @Expose
    private GPlacesResult result;
    @SerializedName("status")
    @Expose
    private String status;


    public GPlacesResult getResult() {
        return result;
    }

    public void setResult(GPlacesResult result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
