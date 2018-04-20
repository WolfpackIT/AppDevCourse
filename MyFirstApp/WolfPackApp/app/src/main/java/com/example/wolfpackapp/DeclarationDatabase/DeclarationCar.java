package com.example.wolfpackapp.DeclarationDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "carDeclarations")
public class DeclarationCar {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "CarDeclarationIdentifier")
    private long uid;

    @ColumnInfo(name = "StartPoint")
    private String van;

    @ColumnInfo(name = "EndPoint")
    private String naar;

    @ColumnInfo(name = "distance")
    private double kilometers;


    @NonNull
    public long getUid() {
        return uid;
    }

    public void setUid(@NonNull long uid) {
        this.uid = uid;
    }

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
