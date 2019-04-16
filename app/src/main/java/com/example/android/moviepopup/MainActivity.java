package com.example.android.moviepopup;

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
import com.orhanobut.hawk.Hawk;

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
    private ArrayList<MovieStructure> chosenSort = new ArrayList<>();
    private ArrayList<MovieStructure> tempHolder = new ArrayList<>();
    private final String TAG = "nawar";
    private final String HAWK_ARRAY_KEY = "sortedArray";
    private final String HAWK_SORTING = "sortingType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Hawk.init(this).build();


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

        if (Hawk.contains(HAWK_ARRAY_KEY) && Hawk.get(HAWK_ARRAY_KEY) != null) {
            tempHolder = Hawk.get(HAWK_ARRAY_KEY);
            myAdapter.setMovieTask(tempHolder);
        } else if (sortingType == SortingType.mostPopulare) {
            chosenSort = FetchJSONData.pMovieObjectList;
            Hawk.put(HAWK_ARRAY_KEY, chosenSort);
            Hawk.put(HAWK_SORTING, sortingType);
            myAdapter.setMovieTask(chosenSort);
        } else if (sortingType == SortingType.topRated) {
            chosenSort = FetchJSONData.trMovieObjectList;
            Hawk.put(HAWK_ARRAY_KEY, chosenSort);
            Hawk.put(HAWK_SORTING, sortingType);
            myAdapter.setMovieTask(chosenSort);
        } else if (sortingType == SortingType.favorite) {
            favoriteFlag = true;
            sortingType = SortingType.favorite;
            DatabaseObserver();
        }
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
                chosenSort = FetchJSONData.trMovieObjectList;
                Hawk.put(HAWK_ARRAY_KEY, chosenSort);
                Hawk.put(HAWK_SORTING, sortingType);
                tempHolder = Hawk.get(HAWK_ARRAY_KEY);
                myAdapter.setMovieTask(chosenSort);
                break;
            case R.id.sorting_by_most_popular:
                sortingType = SortingType.mostPopulare;
                chosenSort = FetchJSONData.pMovieObjectList;
                Hawk.put(HAWK_ARRAY_KEY, chosenSort);
                Hawk.put(HAWK_SORTING, sortingType);
                myAdapter.setMovieTask(chosenSort);
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
        if (Hawk.contains(HAWK_SORTING) && Hawk.get(HAWK_SORTING) != null) {
            sortingType = Hawk.get(HAWK_SORTING);
            intent.putExtra("sortingType", sortingType);
            if (favList != null && sortingType == SortingType.favorite){
                tempHolder = Hawk.get(HAWK_ARRAY_KEY);
                intent.putExtra("movieId", tempHolder.get(index).movieId);
            }
        } else {
            intent.putExtra("sortingType", sortingType);
            if (favList != null && sortingType == SortingType.favorite)
                intent.putExtra("movieId", favList.get(index).movieId);
        }

        startActivity(intent);
    }

    private void DatabaseObserver() {
        MyViewModel myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        myViewModel.getEntity().observe(this, createEntities -> {
            if (favoriteFlag && createEntities != null) {
                chosenSort = toMovieStructure(createEntities);
                Hawk.put(HAWK_ARRAY_KEY, chosenSort);
                Hawk.put(HAWK_SORTING, sortingType);
                myAdapter.setMovieTask(chosenSort);
                favList = createEntities;
            }
        });
    }

    public static ArrayList<MovieStructure> toMovieStructure(List<CreateEntity> createEntities) {
        ArrayList<MovieStructure> movieStructuresArray = new ArrayList<>();
        for (int i = 0; i < createEntities.size(); i++) {
            movieStructuresArray.add(new MovieStructure(createEntities.get(i)));
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
