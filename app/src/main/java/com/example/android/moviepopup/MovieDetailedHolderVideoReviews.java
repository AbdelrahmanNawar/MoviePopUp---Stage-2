package com.example.android.moviepopup;

import java.util.ArrayList;

public class MovieDetailedHolderVideoReviews {
    public ArrayList<String> threeTrailersLinks;
    public ArrayList<String> reviewAuthorNames;
    public ArrayList<String> reviewContents;
    public MovieDetailedHolderVideoReviews(ArrayList threeTrailersLinks, ArrayList reviewAuthorNames, ArrayList reviewContents){
        this.threeTrailersLinks = threeTrailersLinks;
        this.reviewAuthorNames = reviewAuthorNames;
        this.reviewContents = reviewContents;

    }
}
