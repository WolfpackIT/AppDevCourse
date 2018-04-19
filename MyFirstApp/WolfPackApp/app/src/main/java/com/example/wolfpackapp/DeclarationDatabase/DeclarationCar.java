package com.example.wolfpackapp.DeclarationDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

/**
 * Created by Wolfpack on 4/19/2018.
 */
@Entity
public class DeclarationCar extends Declaration {
    @ColumnInfo(name = "StartPoint")
    private String van;

    @ColumnInfo(name = "EndPoint")
    private String naar;

    @ColumnInfo(name = "distance")
    private double kilometers;

    public String getVan() {
        return van;
    }

    public void setVan(String van) {
        this.van = van;
    }

    public String getNaar() {
        return naar;
    }

    public void setNaar(String naar) {
        this.naar = naar;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }

}
