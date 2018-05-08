package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity
public class Receipt {
    //fields
    @PrimaryKey @NonNull            private String ID = UUID.randomUUID().toString();
    @ColumnInfo(name = "name")      private String name;
    @ColumnInfo(name = "type")      private String type;
    @ColumnInfo(name = "data")      private String data;

    //getters and setters
    @NonNull
    public String getID() {
        return ID;
    }
    public void setID(@NonNull String ID) {
        this.ID = ID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }


    public void update(){
        update(null);
    }
    public void update(Database.QueryResult<Receipt> res){
        Database.getDatabase().updateReceipt(this, res);
    }
}
