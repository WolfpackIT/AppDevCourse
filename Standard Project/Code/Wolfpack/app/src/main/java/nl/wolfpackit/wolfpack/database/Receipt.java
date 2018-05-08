package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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


    //import and export from string
    public String exportString(){
        return exportString(false);
    }
    public String exportString(boolean includeID){
        List<String> out = new ArrayList<>();
        out.add(includeID?ID:"false");
        out.add(name);
        out.add(type);
        out.add(data);
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

        setName(itr.next());
        setType(itr.next());
        setData(itr.next());
    }
}
