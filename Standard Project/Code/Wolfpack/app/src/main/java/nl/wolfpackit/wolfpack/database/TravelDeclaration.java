package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
    @ColumnInfo(name = "compensation")  private float compensation;

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
    public float getCompensation() {
        return compensation;
    }
    public void setCompensation(float compensation) {
        this.compensation = compensation;
    }

    public void update(){
        update(null);
    }
    public void update(Database.QueryResult<TravelDeclaration> res){
        Database.getDatabase().updateTravelDeclaration(this, res);
    }

    //import and export from string
    public String exportString(){
        return exportString(false);
    }
    public String exportString(boolean includeID){
        List<String> out = new ArrayList<>();
        out.add(includeID?ID:"false");
        out.add(declarationID);
        out.add(startCity);
        out.add(endCity);
        out.add(dateTimestamp+"");
        out.add(distance+"");
        out.add(compensation+"");
        return TextUtils.join(",", out);
    }

    public void importString(String string){
        String[] in = string.split(",");
        Iterator<String> itr = Arrays.asList(in).iterator();
        importString(itr);
    }
    public void importString(Iterator<String> itr){
        String id = itr.next();
        if(!id.equals("false")) setID(id);

        setDeclarationID(itr.next());
        setStartCity(itr.next());
        setEndCity(itr.next());
        setDateTimestamp(Long.parseLong(itr.next()));
        setDistance(Float.parseFloat(itr.next()));
        setCompensation(Float.parseFloat(itr.next()));


        Log.w("TD", "DATA: "+getDistance()+" x "+getCompensation());
    }
}
