package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity
public class Declaration {
    //fields
    @PrimaryKey @NonNull              private String ID = UUID.randomUUID().toString();
    @ColumnInfo(name = "name")        private String name;
    @ColumnInfo(name = "description") private String description;
    @ColumnInfo(name = "date")        private long dateTimestamp;
    @ColumnInfo(name = "hasBTW")      private boolean hasBTW;
    @ColumnInfo(name = "amount")      private float amount;
    @ColumnInfo(name = "amountBTW")   private float amountBTW;
    @ColumnInfo(name = "receiptID")   private String receiptID;
    @Ignore                           private DeclarationFeedback feedback;
    @Ignore                           private TravelDeclaration travelDeclaration;
    @Ignore                           private Receipt receipt;

    public Declaration(){}

    //getters and setters
    public String getID(){
        return this.ID;
    }
    public void setID(String ID){
        this.ID = ID;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public long getDateTimestamp() {
        return dateTimestamp;
    }
    public void setDateTimestamp(long dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }
    public boolean isHasBTW() {
        return hasBTW;
    }
    public void setHasBTW(boolean hasBTW) {
        this.hasBTW = hasBTW;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public float getAmountBTW() {
        return amountBTW;
    }
    public void setAmountBTW(float amountBTW) {
        this.amountBTW = amountBTW;
    }
    public String getReceiptID() {
        return receiptID;
    }
    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public void update(){
        update(null);
    }
    public void update(Database.QueryResult<Declaration> res){
        Database.getDatabase().updateDeclaration(this, res);
    }

    //feedback methods
    public void createFeedback(final Database.QueryResult<DeclarationFeedback> res){
        Database.createDeclarationFeedback(new Database.QueryResult<DeclarationFeedback>() {
            public void onResult(DeclarationFeedback df) {
                df.setDeclarationID(Declaration.this.ID);
                df.update(null);
                feedback = df;
                if(res!=null)
                    res.onResult(df);
            }
        });
    }
    public void getFeedback(final Database.QueryResult<DeclarationFeedback> res){
        if(feedback!=null){
            res.onResult(feedback);
        }else{
            Database.getDeclarationFeedback(this, new Database.QueryResult<DeclarationFeedback>() {
                public void onResult(DeclarationFeedback df){
                    feedback = df;
                    res.onResult(df);
                }
            });
        }
    }

    //travel declaration methods
    public void createTravelDeclaration(final Database.QueryResult<TravelDeclaration> res){
        Database.createTravelDeclaration(new Database.QueryResult<TravelDeclaration>() {
            public void onResult(TravelDeclaration td) {
                td.setDeclarationID(Declaration.this.ID);
                td.update(null);
                travelDeclaration = td;
                if(res!=null)
                    res.onResult(td);
            }
        });
    }
    public void getTravelDeclaration(final Database.QueryResult<TravelDeclaration> res){
        if(travelDeclaration!=null){
            res.onResult(travelDeclaration);
        }else{
            Database.getTravelDeclaration(this, new Database.QueryResult<TravelDeclaration>() {
                public void onResult(TravelDeclaration td){
                    travelDeclaration = td;
                    res.onResult(td);
                }
            });
        }
    }

    //receipt methods
    public void createReceipt(final Database.QueryResult<Receipt> res){
        Database.createReceipt(new Database.QueryResult<Receipt>() {
            public void onResult(Receipt r) {
                Declaration.this.receiptID = r.getID();
                Declaration.this.update();
                r.update(null);
                if(res!=null)
                    res.onResult(r);
            }
        });
    }
    public void getReceipt(final Database.QueryResult<Receipt> res){
        if(receipt!=null){
            res.onResult(receipt);
        }else{
            Database.getReceipt(this, new Database.QueryResult<Receipt>() {
                public void onResult(Receipt r){
                    receipt = r;
                    res.onResult(r);
                }
            });
        }
    }
}
