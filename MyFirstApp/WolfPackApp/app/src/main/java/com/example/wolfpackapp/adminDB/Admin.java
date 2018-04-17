package com.example.wolfpackapp.adminDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Entity
public class Admin {
    @PrimaryKey @NonNull
    private String uid;

    @ColumnInfo(name = "email")
    private String GMAIL;
    // Getters and setters are ignored for brevity,
    // but they're required for Room to work.


    public String getUid() {
        return uid;
    }

    public String getGMAIL() {
        return GMAIL;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setGMAIL(String mail) {
        this.GMAIL = mail;
    }
}