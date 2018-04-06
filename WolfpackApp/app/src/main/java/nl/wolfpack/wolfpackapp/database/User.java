package nl.wolfpack.wolfpackapp.database;

import java.util.Date;

public class User {
    public String userId;
    public String name;
    public String email;
    public Date birthDate;
    public String jobTitle;
    public String location;
    public String phoneNumber;
    public String iban;
    public String allergies;
    public String role;

    public User() {}

    public User(String userId, String name, String email, Date birthDate, String jobTitle, String location, String phoneNumber, String iban, String allergies) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.jobTitle = jobTitle;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.iban = iban;
        this.allergies = allergies;
        this.role = "wolf";
    }
}
