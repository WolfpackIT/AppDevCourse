package com.example.wolfpackapp.DeclarationDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Entity
public class Declaration {
    @PrimaryKey @NonNull
    private String uid;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "checked")
    private Boolean checked;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "bedrag")
    private double money;


    // Getters and setters are ignored for brevity,
    // but they're required for Room to work.


    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}