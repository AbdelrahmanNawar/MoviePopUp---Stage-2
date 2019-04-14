package com.example.android.moviepopup.MyRoom;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MyDao {
    @Query("SELECT * FROM movieDB ORDER BY titleMovie")
    LiveData<List<CreateEntity>> loadAllTasks();

    @Insert
    void insertTask(CreateEntity createEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(CreateEntity createEntity);

    @Delete
    void deleteTask(CreateEntity createEntity);

    @Query("DELETE FROM movieDB WHERE movieId = :movieId")
    void deleteTask(String movieId);

    @Query("SELECT * FROM movieDB WHERE movieId = :movieId")
    LiveData<CreateEntity> loadMovieById(String movieId);
}

