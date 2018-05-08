package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity
public class TravelDeclaration {
    //fields
    @PrimaryKey @NonNull                private String ID = UUID.randomUUID().toString();
    @ColumnInfo(name = "declarationID") private String declarationID;
    @ColumnInfo(name = "startCity")     private String startCity;
    @ColumnInfo(name = "endCity")       private String endCity;
    @ColumnInfo(name = "date")          private long dateTimestamp;
    @ColumnInfo(name = "distance")      private float distance;

    //getters and setters

    @NonNull
    public String getID() {
        return ID;
    }
    public void setID(@NonNull String ID) {
        this.ID = ID;
    }
    public String getDeclarationID() {
        return declarationID;
    }
    public void setDeclarationID(String declarationID) {
        this.declarationID = declarationID;
    }
    public String getStartCity() {
        return startCity;
    }
    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }
    public String getEndCity() {
        return endCity;
    }
    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }
    public long getDateTimestamp() {
        return dateTimestamp;
    }
    public void setDateTimestamp(long dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }
    public float getDistance() {
        return distance;
    }
    public void setDistance(float distance) {
        this.distance = distance;
    }


    public void update(){
        update(null);
    }
    public void update(Database.QueryResult<TravelDeclaration> res){
        Database.getDatabase().updateTravelDeclaration(this, res);
    }
}
