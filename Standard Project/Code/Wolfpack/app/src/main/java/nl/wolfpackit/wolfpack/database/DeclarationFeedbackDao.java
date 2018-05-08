package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DeclarationFeedbackDao{
    @Query("SELECT * FROM declarationFeedback")
    List<DeclarationFeedback> getAll();

    @Query("SELECT * FROM declarationFeedback WHERE ID IN (:ids)")
    List<DeclarationFeedback> get(String[] ids);

    @Query("SELECT * FROM declarationFeedback WHERE ID =:ID LIMIT 1")
    DeclarationFeedback get(String ID);

    @Query("SELECT * FROM declarationFeedback WHERE declarationID =:ID LIMIT 1")
    DeclarationFeedback findByDeclarationID(String ID);

    @Insert
    void insertAll(DeclarationFeedback... declarationFeedback);

    @Delete
    void delete(DeclarationFeedback declarationFeedback);

    @Update
    void update(DeclarationFeedback declarationFeedback);
}
