package com.example.android.moviepopup;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviepopup.BackGroundFetching.FetchJSONData;
import com.example.android.moviepopup.BackGroundFetching.GetVideoAndReviews;
import com.example.android.moviepopup.MyRoom.AppDatabase;
import com.example.android.moviepopup.MyRoom.CreateEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetailedHolderVideoReviews> {

    private SortingType sortingType;
    private static final int GET_VIDEO_AND_REVIEW_LOADER = 2888;
    private static boolean imageButtonState = false;

    //buttons
    ImageButton firstTrailerButton;
    ImageButton secondTrailerButton;
    ImageButton thirdTrailerButton;

    //comments
    TextView firstAuthorName;
    TextView firstAuthorContent;
    TextView secondAuthorName;
    TextView secondAuthorContent;
    TextView thirdAuthorName;
    TextView thirdAuthorContent;

    ImageView image;
    TextView title;
    TextView voteAverage;
    TextView releaseDate;
    TextView overview;

    ImageButton addFavoriteButton;
    private AppDatabase mDb;
    int movieIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mDb = AppDatabase.getInstance(getApplicationContext());

        image = findViewById(R.id.image_in_details);
        title = findViewById(R.id.title_in_details);
        voteAverage = findViewById(R.id.vote_Average);
        releaseDate = findViewById(R.id.release_date_in_details);
        overview = findViewById(R.id.overview__in_details);

        firstTrailerButton = findViewById(R.id.firstTrailer);
        secondTrailerButton = findViewById(R.id.secondTrailer);
        thirdTrailerButton = findViewById(R.id.thirdTrailer);

        firstAuthorName = findViewById(R.id.firstReviewerName);
        firstAuthorContent = findViewById(R.id.firstReviewerContent);
        secondAuthorName = findViewById(R.id.secondReviewerName);
        secondAuthorContent = findViewById(R.id.secondReviewerContent);
        thirdAuthorName = findViewById(R.id.thirdReviewerName);
        thirdAuthorContent = findViewById(R.id.thirdReviewerContent);

        addFavoriteButton = findViewById(R.id.favoriteButton);

        Intent intent = getIntent();
        movieIndex = intent.getIntExtra("index", 0);
        sortingType = (SortingType) intent.getSerializableExtra("sortingType");


        switch (sortingType) {
            case mostPopulare:
                populateUI(FetchJSONData.pMovieObjectList.get(movieIndex));
                DatabaseObserver(FetchJSONData.pMovieObjectList.get(movieIndex).movieId);
                break;
            case topRated:
                populateUI(FetchJSONData.trMovieObjectList.get(movieIndex));
                DatabaseObserver(FetchJSONData.trMovieObjectList.get(movieIndex).movieId);
                break;
            case favorite:
                DatabaseObserver(intent.getStringExtra("movieId"));
                break;
        }

        if (sortingType != SortingType.favorite)
            getSupportLoaderManager().restartLoader(GET_VIDEO_AND_REVIEW_LOADER, null, this).forceLoad();
    }

    private void populateUI(MovieStructure movieStructures) {
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w185/" + movieStructures.imageMovie)
                .placeholder(R.drawable.loadingicon)
                .error(R.drawable.internetfailed)
                .into(image);
        title.setText(movieStructures.titleMovie);
        releaseDate.setText(movieStructures.releaseDateMovie);
        overview.setText(movieStructures.overviewMovie);
        voteAverage.setText(movieStructures.voteAverage + "/10");

    }

    @NonNull
    @Override
    public Loader<MovieDetailedHolderVideoReviews> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (sortingType == SortingType.mostPopulare)
            return new GetVideoAndReviews(this, FetchJSONData.pMovieObjectList.get(movieIndex).movieId);
        else if (sortingType == SortingType.topRated)
            return new GetVideoAndReviews(this, FetchJSONData.trMovieObjectList.get(movieIndex).movieId);
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieDetailedHolderVideoReviews> loader, final MovieDetailedHolderVideoReviews movieDetailedHolderVideoReviews) {
        firstTrailerButton.setOnClickListener(v -> {
            try {
                openLink(movieDetailedHolderVideoReviews.threeTrailersLinks.get(0));
            } catch (Exception e) {
                Toast.makeText(this, "NO TRAILERS", Toast.LENGTH_SHORT).show();
            }
        });

        secondTrailerButton.setOnClickListener(v -> {
            try {
                openLink(movieDetailedHolderVideoReviews.threeTrailersLinks.get(1));
            } catch (Exception e) {
                Toast.makeText(this, "ONLY ONE TRAILER", Toast.LENGTH_SHORT).show();
            }
        });
        thirdTrailerButton.setOnClickListener(v -> {
            try {
                openLink(movieDetailedHolderVideoReviews.threeTrailersLinks.get(2));
            } catch (Exception e) {
                Toast.makeText(this, "ONLY TWO TRAILERS", Toast.LENGTH_SHORT).show();
            }
        });

        TextView[] tvs = {firstAuthorName, secondAuthorName, thirdAuthorName, firstAuthorContent, secondAuthorContent, thirdAuthorContent};
        for (int i = 0; i < 6; i++) {
            int index = i % 3;
            ArrayList<String> list = i < 3 ? movieDetailedHolderVideoReviews.reviewAuthorNames : movieDetailedHolderVideoReviews.reviewContents;
            try {
                tvs[index].setText(list.get(index));
            } catch (Exception e) {
                break;
            }
        }

        if (sortingType == SortingType.mostPopulare) {
            addFavoriteButton.setOnClickListener(v -> DatabaseWork(FetchJSONData.pMovieObjectList, movieDetailedHolderVideoReviews));
        } else if (sortingType == SortingType.topRated) {
            addFavoriteButton.setOnClickListener(v -> DatabaseWork(FetchJSONData.trMovieObjectList, movieDetailedHolderVideoReviews));
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<MovieDetailedHolderVideoReviews> loader) {
    }


    public void openLink(String url) {
        Uri theLink = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, theLink);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void DatabaseWork(ArrayList<MovieStructure> movieList, MovieDetailedHolderVideoReviews movieDetailedHolderVideoReviews) {
        String trailersLink1 = null;
        String trailersLink2 = null;
        String trailersLink3 = null;
        String reviewAuthorName1 = null;
        String reviewAuthorName2 = null;
        String reviewAuthorName3 = null;
        String reviewContent1 = null;
        String reviewContent2 = null;
        String reviewContent3 = null;
        try {
            trailersLink1 = movieDetailedHolderVideoReviews.threeTrailersLinks.get(0);
        } catch (Exception e) {
        }
        try {
            trailersLink2 = movieDetailedHolderVideoReviews.threeTrailersLinks.get(1);
        } catch (Exception e) {
        }
        try {
            trailersLink3 = movieDetailedHolderVideoReviews.threeTrailersLinks.get(2);
        } catch (Exception e) {
        }
        try {
            reviewAuthorName1 = movieDetailedHolderVideoReviews.reviewAuthorNames.get(0);
        } catch (Exception e) {
        }
        try {
            reviewAuthorName2 = movieDetailedHolderVideoReviews.reviewAuthorNames.get(1);
        } catch (Exception e) {
        }
        try {
            reviewAuthorName3 = movieDetailedHolderVideoReviews.reviewAuthorNames.get(2);
        } catch (Exception e) {
        }
        try {
            reviewContent1 = movieDetailedHolderVideoReviews.reviewContents.get(0);
        } catch (Exception e) {
        }
        try {
            reviewContent2 = movieDetailedHolderVideoReviews.reviewContents.get(1);
        } catch (Exception e) {
        }
        try {
            reviewContent3 = movieDetailedHolderVideoReviews.reviewContents.get(2);

        } catch (Exception e) {
        }
        CreateEntity createEntity = new CreateEntity(movieList.get(movieIndex).titleMovie,
                movieList.get(movieIndex).imageMovie,
                movieList.get(movieIndex).overviewMovie,
                movieList.get(movieIndex).releaseDateMovie,
                movieList.get(movieIndex).voteAverage,
                movieList.get(movieIndex).popularityOfMovie,
                movieList.get(movieIndex).movieId,
                trailersLink1,
                trailersLink2,
                trailersLink3,
                reviewAuthorName1,
                reviewAuthorName2,
                reviewAuthorName3,
                reviewContent1,
                reviewContent2,
                reviewContent3);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(">>>>>>>>>>>>>", String.valueOf(MovieDetails.imageButtonState));
                if (MovieDetails.imageButtonState) {
                    mDb.myDao().deleteTask(createEntity.movieId);
                } else {
                    mDb.myDao().insertTask(createEntity);
                }
            }
        });
    }

    private void DatabaseObserver(String movieId) {
        MyViewModel myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        myViewModel.getSingleEntity(movieId).observe(this, new Observer<CreateEntity>() {
            @Override
            public void onChanged(@Nullable CreateEntity createEntity) {
                if (createEntity != null) {
                    MovieDetails.imageButtonState = true;
                    addFavoriteButton.setImageResource(R.drawable.gold_star);
                    if (sortingType == SortingType.favorite) {
                        populateUI(new MovieStructure(createEntity));
                        firstTrailerButton.setOnClickListener(v -> {
                            try {
                                openLink(createEntity.trailersLink1);
                            } catch (Exception e) {
                                Toast.makeText(MovieDetails.this, "NO TRAILERS", Toast.LENGTH_SHORT).show();
                            }
                        });

                        secondTrailerButton.setOnClickListener(v -> {
                            try {
                                openLink(createEntity.trailersLink2);
                            } catch (Exception e) {
                                Toast.makeText(MovieDetails.this, "ONLY ONE TRAILER", Toast.LENGTH_SHORT).show();
                            }
                        });
                        thirdTrailerButton.setOnClickListener(v -> {
                            try {
                                openLink(createEntity.trailersLink3);
                            } catch (Exception e) {
                                Toast.makeText(MovieDetails.this, "ONLY TWO TRAILERS", Toast.LENGTH_SHORT).show();
                            }
                        });


                        firstAuthorName.setText(createEntity.reviewAuthorName1);
                        firstAuthorContent.setText(createEntity.reviewContent1);
                        secondAuthorName.setText(createEntity.reviewAuthorName2);
                        secondAuthorContent.setText(createEntity.reviewContent2);
                        thirdAuthorName.setText(createEntity.reviewAuthorName3);
                        thirdAuthorContent.setText(createEntity.reviewContent3);

                        addFavoriteButton.setOnClickListener(v -> AppExecutors.getInstance().diskIO().execute(() -> {
                            if (MovieDetails.imageButtonState) {
                                mDb.myDao().deleteTask(createEntity.movieId);
                            } else {
                                mDb.myDao().insertTask(createEntity);
                            }
                        }));
                    }
                } else {
                    MovieDetails.imageButtonState = false;
                    addFavoriteButton.setImageResource(R.drawable.grey_star);
                }
            }
        });
    }
}
