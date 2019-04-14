package com.example.android.moviepopup;

import com.example.android.moviepopup.MyRoom.CreateEntity;

public class MovieStructure {

    public String titleMovie;
    public String imageMovie;
    public String overviewMovie;
    public String releaseDateMovie;
    public double voteAverage;
    public double popularityOfMovie;
    public String movieId;
    public String trailersLink1;
    public String trailersLink2;
    public String trailersLink3;
    public String reviewAuthorName1;
    public String reviewAuthorName2;
    public String reviewAuthorName3;
    public String reviewContent1;
    public String reviewContent2;
    public String reviewContent3;

    public MovieStructure(String titleMovie, String imageMovie, String overviewMovie, String releaseDateMovie, double voteAverage, double popularityOfMovie, String movieId) {
        this.titleMovie = titleMovie;
        this.imageMovie = imageMovie;
        this.overviewMovie = overviewMovie;
        this.releaseDateMovie = releaseDateMovie;
        this.voteAverage = voteAverage;
        this.popularityOfMovie = popularityOfMovie;
        this.movieId = movieId;
    }

    public MovieStructure(String titleMovie, String imageMovie, String overviewMovie, String releaseDateMovie, double voteAverage, double popularityOfMovie, String movieId, String trailersLink1, String trailersLink2, String trailersLink3, String reviewAuthorName1, String reviewAuthorName2, String reviewAuthorName3, String reviewContent1, String reviewContent2, String reviewContent3) {
        this.titleMovie = titleMovie;
        this.imageMovie = imageMovie;
        this.overviewMovie = overviewMovie;
        this.releaseDateMovie = releaseDateMovie;
        this.voteAverage = voteAverage;
        this.popularityOfMovie = popularityOfMovie;
        this.movieId = movieId;
        this.trailersLink1 = trailersLink1;
        this.trailersLink2 = trailersLink2;
        this.trailersLink3 = trailersLink3;
        this.reviewAuthorName1 = reviewAuthorName1;
        this.reviewAuthorName2 = reviewAuthorName2;
        this.reviewAuthorName3 = reviewAuthorName3;
        this.reviewContent1 = reviewContent1;
        this.reviewContent2 = reviewContent2;
        this.reviewContent3 = reviewContent3;
    }

    public MovieStructure(CreateEntity createEntity) {
        this.titleMovie = createEntity.titleMovie;
        this.imageMovie = createEntity.imageMovie;
        this.overviewMovie = createEntity.overviewMovie;
        this.releaseDateMovie = createEntity.releaseDateMovie;
        this.voteAverage = createEntity.voteAverage;
        this.popularityOfMovie = createEntity.popularityOfMovie;
        this.movieId = createEntity.movieId;
        this.trailersLink1 = createEntity.trailersLink1;
        this.trailersLink2 = createEntity.trailersLink2;
        this.trailersLink3 = createEntity.trailersLink3;
        this.reviewAuthorName1 = createEntity.reviewAuthorName1;
        this.reviewAuthorName2 = createEntity.reviewAuthorName2;
        this.reviewAuthorName3 = createEntity.reviewAuthorName3;
        this.reviewContent1 = createEntity.reviewContent1;
        this.reviewContent2 = createEntity.reviewContent2;
        this.reviewContent3 = createEntity.reviewContent3;
    }


}
