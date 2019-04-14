package com.example.android.moviepopup;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.moviepopup.BackGroundFetching.FetchJSONData;
import com.example.android.moviepopup.MyRoom.AppDatabase;
import com.example.android.moviepopup.MyRoom.CreateEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SendingIndex, LoaderManager.LoaderCallbacks<Void> {
    public static RecyclerView myRecyclerView;
    public static GridLayoutManager myManager;
    public static MovieAdapter myAdapter;
    private static Intent intent;
    private SortingType sortingType = SortingType.mostPopulare;
    private AppDatabase mDb;
    private boolean favoriteFlag = false;
    public static List<CreateEntity> favList;
    private static final int GET_FETCH_LOADER = 2871;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportLoaderManager().restartLoader(GET_FETCH_LOADER, null, this).forceLoad();
        myRecyclerView = findViewById(R.id.movie_recycler_id);
        myManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        mDb = AppDatabase.getInstance(getApplicationContext());
        DatabaseObserver();

    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    public void wiringAdapterToRecyclerView() {
        myAdapter = new MovieAdapter(this);
        myAdapter.setMovieTask(FetchJSONData.pMovieObjectList);
        myRecyclerView.setLayoutManager(myManager);
        myRecyclerView.setAdapter(myAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sorting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sorting_by_highest_rate:
                sortingType = SortingType.topRated;
                myAdapter.setMovieTask(FetchJSONData.trMovieObjectList);
                break;
            case R.id.sorting_by_most_popular:
                sortingType = SortingType.mostPopulare;
                myAdapter.setMovieTask(FetchJSONData.pMovieObjectList);
                break;
            case R.id.sorting_by_favorites:
                favoriteFlag = true;
                sortingType = SortingType.favorite;
                DatabaseObserver();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int index) {
        intent = new Intent(this, MovieDetails.class);
        intent.putExtra("index", index);
        intent.putExtra("sortingType", sortingType);
        if (favList != null && sortingType == SortingType.favorite)
            intent.putExtra("movieId", favList.get(index).movieId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void DatabaseObserver() {
        MyViewModel myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        myViewModel.getEntity().observe(this, new Observer<List<CreateEntity>>() {
            @Override
            public void onChanged(@Nullable List<CreateEntity> createEntities) {
                if (favoriteFlag && createEntities != null) {
                    myAdapter.setMovieTask(toMovieStructure((ArrayList<CreateEntity>) createEntities));
                    favList = createEntities;
                }
            }
        });
    }

    public static ArrayList<MovieStructure> toMovieStructure(List<CreateEntity> createEntities) {
        ArrayList<MovieStructure> movieStructuresArray = new ArrayList<>();
        for (int i = 0; i < createEntities.size(); i++) {
            movieStructuresArray.add( new MovieStructure(createEntities.get(i)));
        }
        return movieStructuresArray;
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        return new FetchJSONData(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Void> loader, Void aVoid) {
        wiringAdapterToRecyclerView();
    }


    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }
}
