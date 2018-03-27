package com.example.mzfirstspam;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

/**
 * Created by s147356 on 27-3-2018.
 */
@Entity
public class fence {
    @PrimaryKey
    private int fID;

    @ColumnInfo(name = "Location")
    private Location loc;

    @ColumnInfo(name = "Lattitude")
    private double lat;

    @ColumnInfo(name = "Longitude")
    private double lon;

//    @ColumnInfo(name = "acc")
//    private double acc;

    // Getters and setters are ignored for brevity,
    // but they're required for Room to work.
}