package com.example.android.moviepopup.BackGroundFetching;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.moviepopup.MovieDetailedHolderVideoReviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class GetVideoAndReviews extends AsyncTaskLoader <MovieDetailedHolderVideoReviews>{
    private String movieId;
    public ArrayList<String> threeTrailersLinks = new ArrayList<>();
    public ArrayList<String> reviewAuthorNames = new ArrayList<>();
    public ArrayList<String> reviewContents = new ArrayList<>();

    public GetVideoAndReviews(@NonNull Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Nullable
    @Override
    public MovieDetailedHolderVideoReviews loadInBackground() {
        String videoString = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=bb527388b12351f469d567558bd8384d&language=en-US";
        String reviewsString = "https://api.themoviedb.org/3/movie/" + movieId +"/reviews?api_key=bb527388b12351f469d567558bd8384d&language=en-US&page=1";

        try {
            URL videoURL = new URL(videoString);
            URL reviewsURL = new URL(reviewsString);
            JsonVideoGetter(internetGetter(videoURL));
            JsonReviewGetter(internetGetter(reviewsURL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MovieDetailedHolderVideoReviews(threeTrailersLinks,reviewAuthorNames,reviewContents);
    }


    public String internetGetter(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
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

    public void JsonVideoGetter(String jsonObject) {
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
                String key = theMovie.getString("key");
                String youtubeLinkForTrailer = "https://www.youtube.com/watch?v=" + key;
                threeTrailersLinks.add(youtubeLinkForTrailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void JsonReviewGetter(String jsonObject) {
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
                String authorName = theMovie.getString("author");
                String authorsContent = theMovie.getString("content");
                reviewAuthorNames.add(authorName);
                reviewContents.add(authorsContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

