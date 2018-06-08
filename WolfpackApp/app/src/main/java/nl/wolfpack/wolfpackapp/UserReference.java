package nl.wolfpack.wolfpackapp;

import java.util.Date;

class UserReference {
    public String userID;
    public String name;
    public Date birthDate;
    public String phoneNumber;
    public String email;
    public String IBAN;
    public String allergies;
    public String jobTitle;
    public String role;

    public UserReference() {}

    public UserReference(String userID, String name, String email, Date birthDate, String jobTitle, String phoneNumber, String IBAN, String allergies, String role) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.jobTitle = jobTitle;
        this.phoneNumber = phoneNumber;
        this.IBAN = IBAN;
        this.allergies = allergies;
        this.role = "Wolf";
    }
}
