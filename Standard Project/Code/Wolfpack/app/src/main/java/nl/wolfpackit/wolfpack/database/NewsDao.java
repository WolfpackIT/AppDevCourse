package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NewsDao {
    @Query("SELECT * FROM news")
    List<News> getAll();

    @Query("SELECT * FROM news WHERE ID IN (:ids)")
    List<News> get(String[] ids);

    @Query("SELECT * FROM news WHERE ID =:ID LIMIT 1")
    News get(String ID);

    @Insert
    void insertAll(News... news);

    @Delete
    void delete(News news);

    @Update
    void update(News news);
}
