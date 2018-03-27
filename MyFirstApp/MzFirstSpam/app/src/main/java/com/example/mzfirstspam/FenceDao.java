package com.example.mzfirstspam;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.location.Location;

import com.example.mzfirstspam.fence;
import com.google.android.gms.location.Geofence;

import java.util.List;

/**
 * Created by s147356 on 27-3-2018.
 */

public interface FenceDao {
    @Query("SELECT * FROM fence")
    List<fence> getAll();

    @Query("SELECT * FROM fence WHERE fID IN (:userIds)")
    List<fence> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM fence WHERE Lattitude LIKE :first AND "
            + "Longitude LIKE :last LIMIT 1")
    fence findByName(String first, String last);

    @Insert
    void insertAll(fence... users);

    @Insert
    public void insert(fence f);

    @Delete
    void delete(fence user);
}
