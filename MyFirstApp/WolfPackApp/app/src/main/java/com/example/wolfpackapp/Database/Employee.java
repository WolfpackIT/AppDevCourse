package com.example.wolfpackapp.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Entity
public class Employee {
    @PrimaryKey @NonNull
    private String uid;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "Gmail")
    private String email;

    @ColumnInfo(name = "WP email")
    private String wpMail;

    @ColumnInfo(name = "Birtday")
    private String bday;

    @ColumnInfo(name = "Function")
    private String fun;


    // Getters and setters are ignored for brevity,
    // but they're required for Room to work.


    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getWpMail() {
        return wpMail;
    }

    public String getBday() {
        return bday;
    }

    public String getFun() {
        return fun;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWpMail(String wpMail) {
        this.wpMail = wpMail;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public void setFun(String fun) {
        this.fun = fun;
    }
}