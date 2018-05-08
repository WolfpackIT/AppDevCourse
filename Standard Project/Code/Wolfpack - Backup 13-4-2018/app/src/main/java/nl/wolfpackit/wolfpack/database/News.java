package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import nl.wolfpackit.wolfpack.ImageTools;

@Entity
public class News{
    //fields
    @PrimaryKey @NonNull            private String ID = UUID.randomUUID().toString();
    @ColumnInfo(name = "title")     private String title;
    @ColumnInfo(name = "summery")   private String summery;
    @ColumnInfo(name = "content")   private String content;
    @ColumnInfo(name = "image")     private String imageData;
    @ColumnInfo(name = "authorID")  private String authorID;
    @ColumnInfo(name = "date")      private Long date = System.currentTimeMillis();
    @ColumnInfo(name = "views")     private int views = 0;

    @NonNull
    public String getID() {
        return ID;
    }
    public void setID(@NonNull String ID) {
        this.ID = ID;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getImageData() {
        return imageData;
    }
    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
    public String getAuthorID() {
        return authorID;
    }
    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }
    public String getSummery() {
        return summery;
    }
    public void setSummery(String summery) {
        this.summery = summery;
    }
    public Long getDate() {
        return date;
    }
    public void setDate(Long date) {
        this.date = date;
    }
    public int getViews() {
        return views;
    }
    public void setViews(int views) {
        this.views = views;
    }

    public Bitmap getImage(){
        if(this.imageData!=null){
            return ImageTools.getImageFromString(this.imageData);
        }else{
            return null;
        }
    }
    public void getAuthorImage(final ImageTools.ImageResultListener listener){
        Database.getAccount(authorID, new Database.QueryResult<Account>(){
            public void onResult(Account o){
                o.getPicture(listener, null);
            }
        });
    }

    public void update(){
        update(null);
    }
    public void update(Database.QueryResult<News> res){
        Database.getDatabase().updateNews(this, res);
    }
}
