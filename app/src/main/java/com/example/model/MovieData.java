package com.example.model;

/**
 * Created by Tayyab on 6/5/2017.
 */

public class MovieData {

//    private int movieId;
    private String movieTitle;
    private String posterPath;
    private String movieOverview;
    private String movieRating;
    private String releaseDate;

    /*public int getMovieId() {
        return movieId;
    }*/

    /*public void setMovieId(int movieId) {
        this.movieId = movieId;
    }*/

    public void setPath(String path) {
        this.posterPath = path;
    }

    public String getPath() {
        return this.posterPath;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
