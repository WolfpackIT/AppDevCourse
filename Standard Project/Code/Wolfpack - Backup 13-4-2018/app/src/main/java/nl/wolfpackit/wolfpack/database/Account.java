package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;
import java.util.UUID;

import nl.wolfpackit.wolfpack.ImageTools;
import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.modules.BaseActivity;
import nl.wolfpackit.wolfpack.permissions.Roles;

@Entity
public class Account {
    //global class instance
    private static Account account;
    public static Account getAccount(){
        return account;
    }
    public static void createAccount(final GoogleSignInAccount googleAccount, final Runnable r){
        final Database db = Database.getDatabase();
        db.getAccount(googleAccount, new Database.QueryResult<Account>() {
            public void onResult(Account ac) {
                if(ac==null){ //if there is no account yet, create one
                    db.createAccount(googleAccount, new Database.QueryResult<Account>() {
                        public void onResult(Account ac) {//simply set the account, and continue
                            Account.account = ac;
                            r.run();
                        }
                    });
                }else{//simply set the account, and continue
                    Account.account = ac;
                    ac.googleAccount = googleAccount; //assign the google account
                    r.run();
                }
            }
        });
    }

    //the class itself
    @Ignore                         GoogleSignInAccount googleAccount;

    //    @PrimaryKey @NonNull                private String ID = UUID.randomUUID().toString();

    @PrimaryKey @NonNull @ColumnInfo(name = "googleID")
    private String googleID;
    @ColumnInfo(name = "image")         private String profilePic;
    @ColumnInfo(name = "name")          private String name;
    @ColumnInfo(name = "city")          private String city;
    @ColumnInfo(name = "phone")         private String phone;
    @ColumnInfo(name = "IBAN")          private String IBAN;
    @ColumnInfo(name = "allergies")     private String allergies;
    @ColumnInfo(name = "dateOfBirth")   private String dateOfBirth;
    @ColumnInfo(name = "role")          private int roleID;

    public Account(){}
    public void attachGoogleAccount(GoogleSignInAccount googleAccount){
        this.googleAccount = googleAccount;
        googleID = googleAccount.getId();
    }

    public Uri getPicture(final Context context){
        return getPicture(null, context);
    }
    public Uri getPicture(final ImageTools.ImageResultListener request, final Context context){
        if(googleAccount==null){
            Bitmap image = ImageTools.getImageFromString(getProfilePic());
            request.onImageRecieve(image);
            return null;
        }
        Uri url = googleAccount.getPhotoUrl();

        if(request!=null){
            ImageTools.downloadImage(url.toString(), new ImageTools.ImageResultListener() {
                public void onImageRecieve(Bitmap image){
                    if(image!=null){
                        //make the image circular
                        ImageTools.makeImageCircular(image, 25, context.getResources().getColor(R.color.profileRing), new ImageTools.ImageResultListener(){
                            public void onImageRecieve(Bitmap image) {
                                //save the latest picture
                                String imgString = ImageTools.getStringFromImage(image);
                                setProfilePic(imgString);
                                update();

                                //return the image
                                request.onImageRecieve(image);
                            }
                        });
                    }else{ //if no connection could be made, load the previously stored picture
                        image = ImageTools.getImageFromString(getProfilePic());
                        request.onImageRecieve(image);
                    }
                }
            });
        }

        return url;
    }
    public Roles getRole(){
        return Roles.getByID(roleID);
    }
    public void update(){
        Database.getDatabase().updateAccount(this, null);
    }

    //simple getters and setters
    public String getGoogleID(){
        return googleID;
    }
    public void setGoogleID(String googleID){
        this.googleID = googleID;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getIBAN(){
        return IBAN;
    }
    public void setIBAN(String IBAN){
        this.IBAN = IBAN;
    }
    public String getAllergies(){
        return allergies;
    }
    public void setAllergies(String allergies){
        this.allergies = allergies;
    }
    public int getRoleID(){
        return roleID;
    }
    public void setRoleID(int ID){
        this.roleID = ID;
    }
//    public String getID() {
//        return ID;
//    }
//    public void setID(@NonNull String ID) {
//        this.ID = ID;
//    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getProfilePic(){
        return profilePic;
    }
    public void setProfilePic(String profilePic){
        this.profilePic = profilePic;
    }

    //a simple dao for the account
    @Dao
    public interface AccountDao{
        @Query("SELECT * FROM account WHERE googleID LIKE :googleID LIMIT 1")
        Account get(String googleID);

        @Insert
        void insert(Account account);

        @Delete
        void delete(Account account);

        @Update
        void update(Account account);
    }

}
