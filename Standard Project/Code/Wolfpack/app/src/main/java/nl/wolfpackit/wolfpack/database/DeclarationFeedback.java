package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.util.StringUtil;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Entity
public class DeclarationFeedback{
    //fields
    @PrimaryKey @NonNull                private String ID = UUID.randomUUID().toString();
    @ColumnInfo(name = "declarationID") private String declarationID;
    @ColumnInfo(name = "approved")      private boolean approved;
    @ColumnInfo(name = "feedback")      private String feedback;


    //getters and setters
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public boolean isApproved() {
        return approved;
    }
    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    public String getFeedback() {
        return feedback;
    }
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    public String getDeclarationID() {
        return declarationID;
    }
    public void setDeclarationID(String declarationID) {
        this.declarationID = declarationID;
    }

    public void update(){
        update(null);
    }
    public void update(Database.QueryResult<DeclarationFeedback> res){
        Database.getDatabase().updateDeclarationFeedback(this, res);
    }

    //import and export from string
    public String exportString(){
        return exportString(false);
    }
    public String exportString(boolean includeID){
        List<String> out = new ArrayList<>();
        out.add(includeID?ID:"false");
        out.add(ID);
        out.add(declarationID);
        out.add(approved+"");
        out.add(feedback);
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
        setApproved(Boolean.parseBoolean(itr.next()));
        setFeedback(itr.next());
    }
}
