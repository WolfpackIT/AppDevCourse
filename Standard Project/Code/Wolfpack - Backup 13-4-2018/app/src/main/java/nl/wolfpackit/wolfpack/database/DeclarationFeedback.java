package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
}
