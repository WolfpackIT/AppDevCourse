package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DeclarationDao{
    @Query("SELECT * FROM declaration")
    List<Declaration> getAll();

    @Query("SELECT * FROM declaration WHERE ID IN (:ids)")
    List<Declaration> get(String[] ids);

    @Query("SELECT * FROM declaration WHERE ID =:ID")
    Declaration get(String ID);

    @Query("SELECT * FROM declaration WHERE name LIKE :name LIMIT 1")
    Declaration findByName(String name);

    @Query("SELECT * FROM declaration WHERE NOT EXISTS(SELECT * FROM declarationFeedback WHERE declaration.ID=declarationID)")
    List<Declaration> getAllPending();

    @Query("SELECT * FROM declaration WHERE EXISTS(SELECT * FROM declarationFeedback WHERE declaration.ID=declarationID AND approved)")
    List<Declaration> getAllApproved();

    @Query("SELECT * FROM declaration WHERE EXISTS(SELECT * FROM declarationFeedback WHERE declaration.ID=declarationID AND not approved)")
    List<Declaration> getAllDenied();

    @Insert
    void insertAll(Declaration... declaration);

    @Delete
    void delete(Declaration declaration);

    @Update
    void update(Declaration declaration);
}
