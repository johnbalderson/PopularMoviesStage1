package com.johnbalderson.popularmoviesstage1.model;


public class Movie {

    private String originalTitle;
    private String posterImage;
    private String plotSynopsis;
    private double userRating;
    private String releaseDate;
    private int movieId;


    public Movie(String originalTitle, String posterImage, String plotSynopsis,
                 double userRating, String releaseDate, int id) {

        this.originalTitle = originalTitle;
        this.posterImage = posterImage;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.movieId = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }


    public String getPosterImage() {
        return posterImage;
    }


    public String getPlotSynopsis() {
        return plotSynopsis;
    }


    public double getUserRating() {
        return userRating;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

    public int getMovieId() { return movieId; }


}
