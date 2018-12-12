package com.bashalir.go4lunch.Models;

import java.util.List;

public class ListMarkerGmap {

    private Integer size;
    private List<MarkerGmap> markerGmap;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<MarkerGmap> getMarkerGmap() {
        return markerGmap;
    }

    public void setMarkerGmap(List<MarkerGmap> markerGmap) {
        this.markerGmap = markerGmap;
    }
}
