package com.example.android.moviepopup.BackGroundFetching;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.moviepopup.MainActivity;
import com.example.android.moviepopup.MovieStructure;
import com.example.android.moviepopup.SortingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class FetchJSONData extends AsyncTaskLoader<Void> {
    //Todo loader
    public static ArrayList<MovieStructure> pMovieObjectList = new ArrayList<>();
    public static ArrayList<MovieStructure> trMovieObjectList = new ArrayList<>();

    public FetchJSONData(@NonNull Context context) {
        super(context);
    }


    public String internetGetter(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public void JsonGetter (String jsonObject,SortingType sortInt) {
        JSONObject moviePackage = null;
        try {
            moviePackage = new JSONObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray singleObject = moviePackage.getJSONArray("results");
            for (int i = 0; i < singleObject.length(); i++) {
                JSONObject theMovie = singleObject.getJSONObject(i);
                String titleMovie = theMovie.getString("title");
                String imageMovie = theMovie.getString("poster_path");
                String overviewMovie = theMovie.getString("overview");
                String releaseDateMovie = theMovie.getString("release_date");
                double voteAverageMovie = theMovie.getInt("vote_average");
                double popularityOfMovie = theMovie.getDouble("popularity");
                String movieId = theMovie.getString("id");

                MovieStructure movieInfo = new MovieStructure(titleMovie,imageMovie,overviewMovie,releaseDateMovie,voteAverageMovie,popularityOfMovie,movieId);

                if(sortInt == SortingType.mostPopulare){
                    pMovieObjectList.add(movieInfo);
                }else if(sortInt == SortingType.topRated){
                    trMovieObjectList.add(movieInfo);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Nullable
    @Override
    public Void loadInBackground() {
        try {
            URL urlPopular = new URL("http://api.themoviedb.org/3/movie/popular?api_key=bb527388b12351f469d567558bd8384d");
            URL urlTopRated = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=bb527388b12351f469d567558bd8384d");
            JsonGetter(internetGetter(urlPopular), SortingType.mostPopulare);
            JsonGetter(internetGetter(urlTopRated),SortingType.topRated);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
