package com.example.android.moviepopup;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.moviepopup.MyRoom.AppDatabase;
import com.example.android.moviepopup.MyRoom.CreateEntity;

import java.util.List;


public class MyViewModel extends AndroidViewModel{

    private LiveData<List<CreateEntity>> entity;
    private AppDatabase database;

    public MyViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        entity = database.myDao().loadAllTasks();
    }

    public LiveData<List<CreateEntity>> getEntity() {
        return entity;
    }
    public LiveData<CreateEntity> getSingleEntity(String movieID) {
        return database.myDao().loadMovieById(movieID);
    }
}
