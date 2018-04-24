package com.example.wolfpackapp.DeclarationDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "OtherDeclarations")
public class DeclarationOther {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "otherID")
    private long oID;

    @NonNull
    @ColumnInfo(name = "OtherDeclarationIdentifier")
    private long uid;

    @ColumnInfo(name = "Description")
    private String des;

    @ColumnInfo(name = "BTW")
    private boolean btw;

    @ColumnInfo(name = "Cost")
    private double cost;

    @ColumnInfo(name = "CostBTW")
    private double btwCost;

    @ColumnInfo(name = "TotalCost")
    private double totalCost;

    @NonNull
    public long getOID() {
        return oID;
    }

    public void setOID(@NonNull long oID) {
        this.oID = oID;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getBtwCost() {
        return btwCost;
    }

    public void setBtwCost(double btwCost) {
        this.btwCost = btwCost;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isBtw() {
        return btw;
    }

    public void setBtw(boolean btw) {
        this.btw = btw;
    }

    @NonNull
    public long getUid() {
        return uid;
    }

    public void setUid(@NonNull long uid) {
        this.uid = uid;
    }


    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
