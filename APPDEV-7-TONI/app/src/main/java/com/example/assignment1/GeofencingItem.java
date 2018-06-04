package com.example.assignment1;

/**
 * Created by Wolfpack on 6/4/2018.
 */

public class GeofencingItem {
    private String longtitude;
    private String lautitde;

    public GeofencingItem(String longtitude, String lautitde) {
        this.longtitude = longtitude;
        this.lautitde = lautitde;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public String getLautitde() {
        return lautitde;
    }
}
