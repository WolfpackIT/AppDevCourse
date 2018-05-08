package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ReceiptDao {
    @Query("SELECT * FROM receipt")
    List<Receipt> getAll();

    @Query("SELECT * FROM receipt WHERE ID IN (:ids)")
    List<Receipt> get(String[] ids);

    @Query("SELECT * FROM receipt WHERE ID =:ID LIMIT 1")
    Receipt get(String ID);

    @Query("SELECT * FROM receipt WHERE ID =(SELECT receiptID FROM declaration WHERE ID=:ID LIMIT 1) LIMIT 1")
    Receipt findByDeclarationID(String ID);

    @Insert
    void insertAll(Receipt... receipt);

    @Delete
    void delete(Receipt receipt);

    @Update
    void update(Receipt receipt);
}
